package jvm.run;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jvm.base.Class;
import jvm.base.ClassPart;

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

	public static Object makeString(String constant, Class clazz) {
		JVMObject result = new JVMObject(clazz.getClassPool().getClazz("java/lang/String"));
		char[] chars = constant.toCharArray();
		JVMList listC = new JVMList(chars.length);
		int i=0;
	    for (char c : chars) {
	        listC.set(i++,(int) c);
	    }
		result.setField("value", listC);
		result.setField("count", constant.length());
		result.setField("offset", 0);
		return result;
	}
	
}
