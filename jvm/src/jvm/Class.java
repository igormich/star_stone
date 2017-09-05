package jvm;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import attributes.Attribute;
import attributes.CodeAttribute;

public class Class {

	private static short minor_version;
	private static short major_version;
	private List<Constant> constants;
	private short access_flags;
	private short this_class;
	private short super_class;
	private List<Field> fields;
	private List<Method> methods;
	private List<Attribute> attributes;
	private Map<String,Object> staticFields = new HashMap<>();
	private ClassPool classPool;

	public static Class read(File file, ClassPool classPool) throws FileNotFoundException, IOException {
		Class result = new Class();
		try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
			int magic = dis.readInt();
			minor_version = dis.readShort();
			major_version = dis.readShort();
			List<Constant> constants = Constant.read(dis, result);
			result.setConstants(constants);
			for (Constant constant : constants) {
				//System.out.println(constant);
			}
			result.access_flags = dis.readShort();
			result.this_class = dis.readShort();
			result.super_class = dis.readShort();
			short interfaces_count = dis.readShort();
			List<Constant> interfaces = new ArrayList<>();
			for (int i = 0; i < interfaces_count; i++) {
				short _interface = dis.readShort();
				interfaces.add(constants.get(_interface));
			}
			//System.out.println(interfaces);
			List<Field> fields = Field.read(dis, result);
			result.setFields(fields);
			List<Method> methods = Method.read(dis, result);
			result.setMethods(methods);
			for (Method method : methods) {
				//System.out.println(method.getName());
				//System.out.println(method.getDescriptor());
				List<Attribute> attributes = method.getAttributes();
				if (attributes.size()>0 && (attributes.get(0) instanceof CodeAttribute)) {
					CodeAttribute codeAttribute = (CodeAttribute) attributes.get(0);
				}
			}
			List<Attribute> attributes = Attribute.readAll(dis, result);
			result.setAttributes(attributes);
			for (Attribute attribute : attributes) {
				//System.out.println(attribute);		
			}
		}
		classPool.addClass(result);
		return result;
	}

	private void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;	
	}

	private void setMethods(List<Method> methods) {
		this.methods = methods;	
	}

	private void setFields(List<Field> fields) {
		this.fields = fields;
	}

	private void setConstants(List<Constant> constants) {
		this.constants = constants;
	}

	public <T extends Constant> T getConstant(int index) {
		@SuppressWarnings("unchecked")
		T t = (T) constants.get(index - 1);
		return t;
	}
	
	public void setClassPool(ClassPool classPool) {
		this.classPool = classPool;		
	}
	
	public ClassPool getClassPool() {
		return classPool;
	}
	
	public Method getMethod(String name, String type) {
		for(Method method: methods){
			if(method.getName().equals(name) && method.getType().equals(type)){
				return method;
			}
		}
		return null;
	}

	public String getName() {
		return getConstant(this_class).toString();
	}

	public List<Method> getMethods() {
		return methods;
	}

	public void setStatic(String fieldName, Object value) {
		staticFields.put(fieldName, value);
		
	}

	public Object getStatic(String fieldName) {
		return staticFields.get(fieldName);
	}

}
