package jvm.attributes;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jvm.base.Class;
import jvm.base.ClassPart;
import jvm.base.Constant;

public class Attribute extends ClassPart {

	
	public Attribute(Class clazz) {
		super(clazz);
	}
	public static List<Attribute> readAll(DataInputStream dis, Class clazz) throws IOException {
		short attributes_count = dis.readShort();
		List<Attribute> result = new ArrayList<>(attributes_count);
		for (int i = 0; i < attributes_count; i++) {
			result.add(readSingle(dis, clazz));
		}
		return result;
	}

	private static Attribute readSingle(DataInputStream dis, Class clazz) throws IOException {
		short name_index = dis.readShort();
	    int length = dis.readInt();//skip length
	    Constant.UtfConstant name = clazz.getConstant(name_index);
	    switch (name.toString()) {
		case "Code":
			return CodeAttribute.read(dis,clazz);
		case "LocalVariableTable":
			return LocalVariableTable.read(dis,clazz);
		case "LineNumberTable":
			return LineNumberTable.read(dis,clazz);
		case "BootstrapMethods":
			return BootstrapMethodsAttribute.read(dis,clazz);			
		default:
			//System.out.println("skip " + name);
			byte[] skip = new byte[length];
		    dis.read(skip);
		    return null;
		}
	}


}
