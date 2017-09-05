package jvm;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import attributes.Attribute;
import attributes.CodeAttribute;
import run.JVMObject;

public class Method extends ClassPart{

	public final short ACC_NATIVE = 0x0100;
	private short access_flags;
	private short name_index;
	private short descriptor_index;
	private List<Attribute> attributes;

	public Method(short access_flags, short name_index, short descriptor_index, List<Attribute> attributes, Class clazz) {
		super(clazz);
		this.access_flags = access_flags;
		this.name_index = name_index;
		this.descriptor_index = descriptor_index;
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return getConstant(descriptor_index) +" : "+getConstant(name_index) +attributes;
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

	public Object execute(JVMObject object, List<Object> args) {
		List<Attribute> attributes = getAttributes();
		if (attributes.size()>0 && (attributes.get(0) instanceof CodeAttribute)) {
			CodeAttribute codeAttribute = (CodeAttribute) attributes.get(0);
			return codeAttribute.execute(object, args);
		}
		throw new RuntimeException();
	}

	public boolean isNative() {
		return (access_flags & ACC_NATIVE) == ACC_NATIVE;
	}
}
