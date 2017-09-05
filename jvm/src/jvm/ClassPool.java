package jvm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClassPool {

	Map<String,Class> pool = new HashMap<>();
	public void addClass(Class clazz) {
		pool.put(clazz.getName(), clazz);
		clazz.setClassPool(this);
		Method clinit = clazz.getMethod("<clinit>", "()V");
		if (clinit !=null) {
			clinit.execute(null, Collections.emptyList());
		}
	}
	public synchronized Class getClazz(String clazzName) {
		Class result = pool.get(clazzName);
		if (result == null) {
			try {
				result = Class.read(new File("bin/"+clazzName+".class"), this);
			}  catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

}
