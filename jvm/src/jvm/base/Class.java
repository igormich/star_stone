package jvm.base;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jvm.attributes.Attribute;
import jvm.attributes.BootstrapMethodsAttribute;
import jvm.attributes.CodeAttribute;

public class Class implements IAccessControl{

	protected static short minor_version;
	protected static short major_version;
	protected List<Constant> constants;
	protected short access_flags;
	protected short this_class;
	protected short super_class;
	protected List<Field> fields;
	protected List<Method> methods;
	protected List<Attribute> attributes;
	protected Map<String, Object> staticFields = new HashMap<>();
	protected ClassPool classPool;
	protected List<String> interfaces;

	public static Class read(InputStream inputStream, ClassPool classPool) throws FileNotFoundException, IOException {
		Class result = new Class();
		try (DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream))) {
			int magic = dis.readInt();
			minor_version = dis.readShort();
			major_version = dis.readShort();
			List<Constant> constants = Constant.read(dis, result);
			result.setConstants(constants);
			for (Constant constant : constants) {
				// System.out.println(constant);
			}
			result.access_flags = dis.readShort();
			result.this_class = dis.readShort();
			result.super_class = dis.readShort();
			short interfaces_count = dis.readShort();
			List<String> interfaces = new ArrayList<>();
			for (int i = 0; i < interfaces_count; i++) {
				short _interface = dis.readShort();
				interfaces.add(constants.get(_interface-1).toString());
			}
			result.setInterfaces(interfaces);
			// System.out.println(interfaces);
			List<Field> fields = Field.read(dis, result);
			result.setFields(fields);
			List<Method> methods = Method.read(dis, result);
			result.setMethods(methods);
			for (Method method : methods) {
				// System.out.println(method.getName());
				// System.out.println(method.getDescriptor());
				List<Attribute> attributes = method.getAttributes();
				if (attributes.size() > 0 && (attributes.get(0) instanceof CodeAttribute)) {
					CodeAttribute codeAttribute = (CodeAttribute) attributes.get(0);
				}
			}
			List<Attribute> attributes = Attribute.readAll(dis, result);
			result.setAttributes(attributes);

		}
		classPool.addClass(result);
		return result;
	}

	private void setInterfaces(List<String> interfaces) {
		this.interfaces = interfaces;
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

	public Method getMethod(String name, String type, boolean searcInParent) {
		for (Method method : methods) {
			if (method.getName().equals(name) && method.getType().equals(type)) {
				return method;
			}
		}
		if (searcInParent && (super_class > 0)) {
			Method result = classPool.getClazz(getConstant(super_class).toString()).getMethod(name, type,
					searcInParent);
			if (result != null)
				return result;
			for (String _interface : interfaces) {
				result = classPool.getClazz(_interface).getMethod(name, type, searcInParent);
				if (result != null)
					return result;
			}
		}
		return null;
	}
	public Method getAnyMethodByName(String name) {
		for (Method method : methods) {
			if (method.getName().equals(name)) {
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

	public List<Field> getFields() {
		return fields;
	}

	public void setStatic(String fieldName, Object value) {
		staticFields.put(fieldName, value);

	}

	public Object getStatic(String fieldName) {
		return staticFields.get(fieldName);
	}

	public BootstrapMethodsAttribute getBootstrapAtribute() {
		for (Attribute attribute : attributes) {
			if (attribute instanceof BootstrapMethodsAttribute)
				return (BootstrapMethodsAttribute) attribute;
		}
		return null;
	}

	public Method getMain() {
		return getMethod("main", "([Ljava/lang/String;)V", false);
	}

	@Override
	public short getAccess_flags() {
		return access_flags;
	}



}
