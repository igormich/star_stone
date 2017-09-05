package run;

import java.util.HashMap;
import java.util.Map;

import jvm.Class;
import jvm.ClassPart;

public class JVMObject extends ClassPart{
	
	public JVMObject(Class clazz) {
		super(clazz);
	}

	private Map<String,Object> fields = new HashMap<>();
	
	public void setField(String fieldName, Object value) {
		fields.put(fieldName, value);
		
	}

	public Object getField(String fieldName) {
		return fields.get(fieldName);
	}

	@Override
	public String toString() {
		return getClazz().getName() + " : " + fields;
	}
	
}
