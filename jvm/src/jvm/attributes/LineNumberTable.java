package jvm.attributes;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jvm.base.Class;

public class LineNumberTable extends Attribute {

	private List<LineNumber> lineNumberTable;

	public static class LineNumber{

		private short start_pc;
		private short line_number;

		public LineNumber(short start_pc, short line_number) {
			this.start_pc = start_pc;
			this.line_number = line_number;
		}
		
	}
	public LineNumberTable(List<LineNumber> lineNumberTable, Class clazz) {
		super(clazz);
		this.lineNumberTable = lineNumberTable;
	}

	public static LineNumberTable read(DataInputStream dis, Class clazz) throws IOException {
		short line_number_table_length = dis.readShort();
		List<LineNumber> lineNumberTable = new ArrayList<>(line_number_table_length);
		for(int i=0;i<line_number_table_length;i++){
			short start_pc =  dis.readShort();
			short line_number =  dis.readShort();
			lineNumberTable.add(new LineNumber(start_pc, line_number));
		}
		return new LineNumberTable(lineNumberTable, clazz);
	}

}