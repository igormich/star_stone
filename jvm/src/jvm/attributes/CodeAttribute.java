package jvm.attributes;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jvm.base.Class;
import jvm.run.CodeRunner;
import jvm.run.JVMObject;

public class CodeAttribute extends Attribute {

	public static class Exception {
		short start_pc;
		short end_pc;
		short handler_pc;
		short catch_type;

		public Exception(short start_pc, short end_pc, short handler_pc, short catch_type) {
			this.start_pc = start_pc;
			this.end_pc = end_pc;
			this.handler_pc = handler_pc;
			this.catch_type = catch_type;
		}
	}

	private final short max_stack;
	private final short max_locals;
	private final byte[] code;
	private final List<Exception> exceptionsTable;
	private final List<Attribute> attributes;

	public CodeAttribute(short max_stack, short max_locals, byte[] code, List<Exception> exceptionsTable, List<Attribute> attributes, Class clazz) {
		super(clazz);
		this.max_stack = max_stack;
		this.max_locals = max_locals;
		this.code = code;
		this.exceptionsTable = exceptionsTable;
		this.attributes = attributes;
	}

	public static CodeAttribute read(DataInputStream dis, Class clazz) throws IOException {
		short max_stack = dis.readShort();
		short max_locals = dis.readShort();
		int code_length = dis.readInt();
		byte[] code = new byte[code_length];
		dis.read(code);
		List<Exception> exceptionsTable = readExceptionsTable(dis, clazz);
		List<Attribute> attributes = Attribute.readAll(dis, clazz);
		return new CodeAttribute(max_stack, max_locals, code, exceptionsTable, attributes, clazz);
	}

	private static List<Exception> readExceptionsTable(DataInputStream dis, Class clazz) throws IOException {
		short exception_table_length = dis.readShort();
		List<Exception> result = new ArrayList<>(exception_table_length);
		for (int i = 0; i < exception_table_length; i++) {
			short start_pc = dis.readShort();
			short end_pc = dis.readShort();
			short handler_pc = dis.readShort();
			short catch_type = dis.readShort();
			result.add(new Exception(start_pc, end_pc, handler_pc, catch_type));
		}
		return result;
	}

	@Override
	public String toString() {
		return "CodeAttribute [max_stack=" + max_stack + ", max_locals=" + max_locals + "]" + attributes;
	}

	public String codeToString() {
		CodeRunner codeRunner = new CodeRunner(code, this, Collections.emptyList()); 
		return codeRunner.toString();
	}

	public Object execute(List<Object> args) {
		CodeRunner codeRunner = new CodeRunner(code, this, args); 
		return codeRunner.execute();
		
	}

	public short getMaxStack() {
		return max_stack;
	}

	public short getMaxLocals() {
		return max_locals;
	}
}