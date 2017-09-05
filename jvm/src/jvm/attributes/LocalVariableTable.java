package jvm.attributes;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jvm.base.Class;

public class LocalVariableTable extends Attribute {

	private List<LocalVariable> localVariableTable;

	public static class LocalVariable {

		private short start_pc;
		private short length;
		private short name_index;
		private short descriptor_index;
		private short index;

		public LocalVariable(short start_pc, short length, short name_index, short descriptor_index, short index) {
			this.start_pc = start_pc;
			this.length = length;
			this.name_index = name_index;
			this.descriptor_index = descriptor_index;
			this.index = index;
		}
		
	}
	public LocalVariableTable(List<LocalVariable> localVariableTable, Class clazz) {
		super(clazz);
		this.localVariableTable = localVariableTable;
	}

	public static LocalVariableTable read(DataInputStream dis, Class clazz) throws IOException {
		short local_variable_table_length = dis.readShort();
		List<LocalVariable> localVariableTable = new ArrayList<>(local_variable_table_length);
		for(int i=0;i<local_variable_table_length;i++){
			short start_pc =  dis.readShort();
			short length =  dis.readShort();
			short name_index =  (short) (dis.readShort() - 1);
			short descriptor_index =  (short) (dis.readShort() - 1);
			short index =  dis.readShort();
			localVariableTable.add(new LocalVariable(start_pc, length, name_index, descriptor_index, index));
		}
		return new LocalVariableTable(localVariableTable, clazz);
	}

	@Override
	public String toString() {
		return localVariableTable.stream()
				.map(lv -> getConstant(lv.descriptor_index).toString() +" " + getConstant(lv.name_index).toString())
				.collect(Collectors.joining(";"));
	}

}