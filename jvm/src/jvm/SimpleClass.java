package jvm;

import java.util.ArrayList;
import java.util.List;

public class SimpleClass {

	native static void print(String s);
	public static void println(String s){
		print(s);
		print("\r\n");
	}
	public static void main(String[] args) {
		int a = 2;
		byte b = (byte) (3 + a);
		println("Hello from simple JVM");
		println(Integer.toString(5123));
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		println(list.toString());
	}

}
