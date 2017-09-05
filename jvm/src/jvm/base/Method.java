package jvm.base;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import jvm.attributes.Attribute;
import jvm.attributes.CodeAttribute;
import jvm.run.JVMObject;

public class Method extends AccessControl {

	private short name_index;
	private short descriptor_index;
	private List<Attribute> attributes;
	private Function<List<Object>, Object> nativeMethod;

	public Method(short access_flags, short name_index, short descriptor_index, List<Attribute> attributes,
			Class clazz) {
		super(clazz, access_flags);
		this.name_index = name_index;
		this.descriptor_index = descriptor_index;
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return getClazz().getName() + "." + getConstant(name_index) + ":" + getConstant(descriptor_index);
	}

	public static List<Method> read(DataInputStream dis, Class clazz) throws IOException {
		short fields_count = dis.readShort();
		List<Method> result = new ArrayList<>(fields_count);
		for (int i = 0; i < fields_count; i++) {
			result.add(readSingle(dis, clazz));
		}
		return result;
	}

	private static Method readSingle(DataInputStream dis, Class clazz) throws IOException {
		short access_flags = dis.readShort();
		short name_index = (short) (dis.readShort());
		short descriptor_index = (short) (dis.readShort());
		List<Attribute> attributes = Attribute.readAll(dis, clazz);
		return new Method(access_flags, name_index, descriptor_index, attributes, clazz);
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public String getName() {
		return getConstant(name_index).toString();
	}

	public String getType() {
		return getConstant(descriptor_index).toString();
	}

	public Constant getDescriptor() {
		return getConstant(descriptor_index);
	}

	public Object execute(List<Object> args) {
		if(nativeMethod!=null){
			return nativeMethod.apply(args);
		}
		List<Attribute> attributes = getAttributes();
		for (Attribute attribute : attributes) {
			if (attribute instanceof CodeAttribute) {
				CodeAttribute codeAttribute = (CodeAttribute) attribute;
				return codeAttribute.execute(args);
			}
		}
		throw new RuntimeException(this.toString());
		//return null;
	}


	public int getArgsCount() {
		String type = getType().replaceAll("L.*?;", "L");
		type = type.replaceAll("\\[", "");
		return type.length()-3;
	}

	public void setNative(Function<List<Object>,Object> nativeMethod) {
		this.nativeMethod = nativeMethod;
	}
	
}
