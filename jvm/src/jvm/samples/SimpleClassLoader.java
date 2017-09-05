package jvm.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import jvm.base.Class;
import jvm.base.ClassPool;
import jvm.run.JVMList;
import jvm.run.JVMObject;

public class SimpleClassLoader {

	public static Object print(List<Object> args) {
		JVMObject jvmObject = (JVMObject) args.get(0);
		JVMList jvml = (JVMList) jvmObject.getField("value");
		List<Integer> l = jvml.getRealList();
		String s = l.stream().map(i -> Character.valueOf((char)i.intValue()).toString()).collect(Collectors.joining());
		System.out.println(s);
		return null;
	}
	@SuppressWarnings("resource")
	public static InputStream nameResolve(String clazzName){
		clazzName+=".class";
	    ZipFile zipFile;
		try {
			zipFile = new ZipFile("rt.jar");
		    Enumeration<? extends ZipEntry> entries = zipFile.entries();
		    while(entries.hasMoreElements()){
		        ZipEntry entry = entries.nextElement();
		        if(entry.getName().equals(clazzName))
		        	return zipFile.getInputStream(entry);
		    }
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			return new FileInputStream("bin/"+clazzName);
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void main(String[] args) throws FileNotFoundException, IOException {	
		ClassPool classPool = ClassPool.instance;
		classPool.setNameResolver(SimpleClassLoader::nameResolve);
		classPool.getClazz("java/lang/String");
		//Class.read(new File("core/Object.class"), classPool);
		//Class.read(new File("core/Integer.class"), classPool);
		//Class.read(new File("core/String.class"), classPool);
		//Class.read(new File("core/Iterable.class"), classPool);
		Class clazz= Class.read(new FileInputStream("bin/jvm/samples/SimpleClass.class"), classPool);
		clazz.getAnyMethodByName("print").setNative(SimpleClassLoader::print);
		clazz.getMain().execute(Collections.emptyList());
	}

}
