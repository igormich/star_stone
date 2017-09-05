package jvm.run;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jvm.base.ArrayClass;

public class JVMList extends JVMObject {


	private ArrayList<Object> list;
	public JVMList(int n) {
		super(ArrayClass.instance);
		list = new ArrayList<>(Collections.nCopies(n, null));
	}
	public void set(int intValue, Object value) {
		list.set(intValue, value);		
	}
	public Object get(int intValue) {
		return list.get(intValue);		
	}
	public int size() {
		return list.size();
	}
	public static Object arraycopy(List<Object> args) {
		//System.arraycopy(src, srcPos, dest, destPos, length);
		JVMList sourse = (JVMList) args.get(0);
		int srcPos = (int) args.get(1);
		JVMList dest = (JVMList) args.get(2);
		int destPos = (int) args.get(3);
		int length = (int) args.get(4);
		for(int i=0;i<length;i++) {
			dest.set(i+destPos, sourse.get(i+srcPos));
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> getRealList() {
		return (List<T>) list;
	}
}
