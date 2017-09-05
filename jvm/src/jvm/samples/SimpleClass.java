package jvm.samples;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleClass {

	native static void print(String s);
	public static void println(String s){
		print(s);
		print("\r\n");
	}
	public static void println(Integer i){
		println(Integer.toString(i));
	}
	public static void main(String[] args) {
		println("Hello from simple JVM");
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		for(Integer i:list){
			print(Integer.toHexString(i*i));
		}
		Consumer<Integer> printer = new Consumer<Integer>() {
			@Override
			public void accept(Integer i) {
				println(i);	
			}
		};
		list.forEach(printer);
		//list.forEach(System.out::println);
	}

}
