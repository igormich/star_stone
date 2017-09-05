package jvm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;

public class ClassLoader {

	public static void main(String[] args) throws FileNotFoundException, IOException {	
		ClassPool classPool = new ClassPool();
		Class object = Class.read(new File("core/Object.class"), classPool);
		Class integer = Class.read(new File("core/Integer.class"), classPool);
		Class string = Class.read(new File("core/String.class"), classPool);
		//integer.getMethods().forEach(System.out::println);
		Class clazz= Class.read(new File("bin/jvm/SimpleClass.class"), classPool);
		System.out.println(classPool.pool);
		clazz.getMethod("main","([Ljava/lang/String;)V").execute(null, Collections.singletonList(new String[]{"Abc"}));
	}

}
