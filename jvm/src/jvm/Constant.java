package jvm;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.UnsupportedDataTypeException;

public abstract class Constant extends ClassPart{

	public Constant(Class clazz) {
		super(clazz);
	}

	private static final byte CLASS = 7;
	private static final byte UTF8 = 1;
	
	private static final byte INTEGER = 3;
	private static final byte FLOAT = 4;
	private static final byte LONG = 5;
	private static final byte DOUBLE = 6;
	
	private static final byte STRING = 8;
	private static final byte METHOD = 10;
	private static final byte NAME_AND_TYPE = 12;
	private static final byte FIELD_REF = 9;
	private static final byte INTERFACE_METHOD_REF = 11;
	
	private static final byte METHOD_HANDLE = 15;
	private static final byte METHOD_TYPE = 16;
	private static final byte INVOKE_DYNAMIC = 18;

	
	public static class IntegerConstant extends Constant{
		private int value;
		public IntegerConstant(int value, Class clazz) {
			super(clazz);
			this.value = value;
		}
		@Override
		public String toString() {
			return "I"+ value;
		}
		@Override
		public Object getValue() {
			return value;
		}
	}
	public static class FloatConstant extends Constant{
		private float value;
		public FloatConstant(float value, Class clazz) {
			super(clazz);
			this.value = value;
		}
		@Override
		public String toString() {
			return "F"+ value;
		}
		@Override
		public Object getValue() {
			return value;
		}
	}
	public static class LongConstant extends Constant{
		private long value;
		public LongConstant(long value, Class clazz) {
			super(clazz);
			this.value = value;
		}
		@Override
		public String toString() {
			return "L"+ value;
		}
		@Override
		public Object getValue() {
			return value;
		}
	}
	public static class DoubleConstant extends Constant{
		private double value;
		public DoubleConstant(double value, Class clazz) {
			super(clazz);
			this.value = value;
		}
		@Override
		public String toString() {
			return "D"+ value;
		}
		@Override
		public Object getValue() {
			return value;
		}
	}
	
	public static class ClassConstant extends Constant{
		private short name_index;
		public ClassConstant(short name_index, Class clazz) {
			super(clazz);
			this.name_index = name_index;
		}
		@Override
		public String toString() {
			return getConstant(name_index).toString();
		}
		public UtfConstant getNameConstant(){
			return (UtfConstant) getConstant(name_index);
		}
		
	}
	public static class StringConstant extends Constant{
		private short string_index;
		public StringConstant(short string_index, Class clazz) {
			super(clazz);
			this.string_index = string_index;
		}
		@Override
		public String toString() {
			return getConstant(string_index).toString();
		}
		@Override
		public Object getValue() {
			return getConstant(string_index).toString();
		}
	}
	public static class UtfConstant extends Constant{
		private String data;
		public UtfConstant(byte[] data, Class clazz) {
			super(clazz);
			this.data =  new String(data);
			//System.out.println(this);
		}
		@Override
		public String toString() {
			return data;
		}
		@Override
		public Object getValue() {
			return data;
		}
	}
	public static class MethodConstant extends Constant{
		private short class_index;
		private short name_and_type_index;
		public MethodConstant(short class_index,short name_and_type_index, Class clazz) {
			super(clazz);
			this.class_index = class_index;
			this.name_and_type_index = name_and_type_index;
		}
		@Override
		public String toString() {
			return getConstant(class_index).toString() + "." + getConstant(name_and_type_index).toString();
		}
		public ClassConstant getClazzConstant(){
			return (ClassConstant) getConstant(class_index);
		}
		public NameAndTypeConstant getNameAndTypeConstant(){
			return (NameAndTypeConstant) getConstant(name_and_type_index);
		}
	}
	public static class FieldRefConstant extends MethodConstant{
		public FieldRefConstant(short class_index, short name_and_type_index, Class clazz) {
			super(class_index, name_and_type_index, clazz);
		}	
	}
	public static class InterfaceMethodRef extends MethodConstant{
		public InterfaceMethodRef(short class_index, short name_and_type_index, Class clazz) {
			super(class_index, name_and_type_index, clazz);
		}	
	}
	public static class NameAndTypeConstant extends Constant{
		private short name_index;
		private short descriptor_index;
		public NameAndTypeConstant(short name_index,short descriptor_index, Class clazz) {
			super(clazz);
			this.name_index = name_index;
			this.descriptor_index = descriptor_index;
		}
		@Override
		public String toString() {
			return getConstant(name_index).toString() + ":" + getConstant(descriptor_index).toString();
		}
		public UtfConstant getNameConstant(){
			return (UtfConstant) getConstant(name_index);
		}
		public UtfConstant getTypeConstant(){
			return (UtfConstant) getConstant(descriptor_index);
		}
	}
	public static class InvokeDynamicConstant extends Constant{
		private short bootstrap_method_attr_index;
		private short name_and_type_index;
		public InvokeDynamicConstant(short bootstrap_method_attr_index, short name_and_type_index, Class clazz) {
			super(clazz);
			this.bootstrap_method_attr_index = bootstrap_method_attr_index;
			this.name_and_type_index = name_and_type_index;
		}
	}
	public static class MethodHandleConstant extends Constant{
		private byte reference_kind;
		private short reference_index;
		public MethodHandleConstant(byte reference_kind, short reference_index, Class clazz) {
			super(clazz);
			this.reference_kind = reference_kind;
			this.reference_index = reference_index;
		}
	}
	public static class MethodTypeConstant extends Constant{
		private short descriptor_index;
		public MethodTypeConstant(short descriptor_index, Class clazz) {
			super(clazz);
			this.descriptor_index = descriptor_index;
		}
	}
	public static List<Constant> read(DataInputStream dis, Class clazz) throws IOException {
		short constant_pool_count = dis.readShort();
		List<Constant> result = new ArrayList<>(constant_pool_count); 
		//System.out.println(constant_pool_count);
		for(int i=1;i<constant_pool_count;i++){
			Constant constant = readSingle(dis, clazz);
			result.add(constant);
			if ((constant instanceof LongConstant) || (constant instanceof DoubleConstant)) {
				i++;
				result.add(null);
			}
			
			//System.out.println("ind "+i);
		}
		return result;
	}

	private static Constant readSingle(DataInputStream dis, Class clazz) throws IOException {
		byte type = dis.readByte();
		//System.out.println("type "+type);
		switch (type) {
		case INTEGER:
			int value = dis.readInt();
			return new IntegerConstant(value, clazz);
		case FLOAT:
			float fvalue = dis.readFloat();
			return new FloatConstant(fvalue, clazz);
		case LONG:
			long high_bytes = dis.readInt();
			long low_bytes = dis.readInt();
			return new LongConstant((high_bytes << 32) + low_bytes, clazz);
		case DOUBLE:
			high_bytes = dis.readInt();
			low_bytes = dis.readInt();
			return new DoubleConstant(Double.longBitsToDouble((high_bytes << 32) + low_bytes), clazz);
		case CLASS:
			short name_index = dis.readShort();
			return new ClassConstant(name_index, clazz);
		case UTF8:
			short length = dis.readShort();
			byte data[] = new byte[length];
			dis.read(data);
			return new UtfConstant(data, clazz);
		case STRING:
			short string_index = dis.readShort();
			return new StringConstant(string_index, clazz);
		case METHOD:
			short class_index = dis.readShort();
			short name_and_type_index = dis.readShort();
			return new MethodConstant(class_index, name_and_type_index, clazz);
		case FIELD_REF:
			class_index = dis.readShort();
			name_and_type_index = dis.readShort();
			return new FieldRefConstant(class_index, name_and_type_index, clazz);
		case INTERFACE_METHOD_REF:
			class_index = dis.readShort();
			name_and_type_index = dis.readShort();
			return new InterfaceMethodRef(class_index, name_and_type_index, clazz);	
		case NAME_AND_TYPE:
			name_index = dis.readShort();
			short descriptor_index = dis.readShort();
			return new NameAndTypeConstant(name_index, descriptor_index, clazz);		
		case METHOD_HANDLE:
			byte reference_kind = dis.readByte();
			short reference_index = dis.readShort();
			return new MethodHandleConstant(reference_kind, reference_index, clazz);	
		case METHOD_TYPE:
			descriptor_index = dis.readShort();
			return new MethodTypeConstant(descriptor_index, clazz);		
		case INVOKE_DYNAMIC:
			short bootstrap_method_attr_index = dis.readShort();
			name_and_type_index = dis.readShort();
			return new InvokeDynamicConstant(bootstrap_method_attr_index, name_and_type_index, clazz);
		default:
			throw new UnsupportedDataTypeException(""+type);
		}
	}

	public Object getValue() {
		return null;
	}

}
