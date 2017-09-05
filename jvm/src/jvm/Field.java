package jvm;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import attributes.Attribute;

public class Field extends ClassPart{

	private short access_flags;
	private short name_index;
	private short descriptor_index;
	private List<Attribute> attributes;

	public Field(short access_flags, short name_index, short descriptor_index, List<Attribute> attributes, Class clazz) {
		super(clazz);
		this.access_flags = access_flags;
		this.name_index = name_index;
		this.descriptor_index = descriptor_index;
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return getConstant(descriptor_index) +" : "+getConstant(name_index);
	}
	
	public static List<Field> read(DataInputStream dis, Class clazz) throws IOException {
		short fields_count = dis.readShort();
		List<Field> result = new ArrayList<>(fields_count);
		for (int i = 0; i < fields_count; i++) {
			result.add(readSingle(dis, clazz));
		}
		return result;
	}

	private static Field readSingle(DataInputStream dis, Class clazz) throws IOException {
		short access_flags = dis.readShort();
		//TODO:?!
		short name_index =  dis.readShort();
		short descriptor_index = dis.readShort();
		List<Attribute> attributes = Attribute.readAll(dis,clazz);
		return new Field(access_flags, name_index, descriptor_index, attributes,clazz);
	}
}
