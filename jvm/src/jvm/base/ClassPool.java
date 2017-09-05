package jvm.base;


import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import jvm.run.JVMList;

public class ClassPool {

	public static final ClassPool instance = new ClassPool();
	Map<String,Class> pool = new HashMap<>();
	Map<String,Function<List<Object>, Object>> natives = new HashMap<>();
	private Function<String, InputStream> nameResolver;
	public ClassPool() {
		natives.put("java/lang/Object.registerNatives:()V", (args) -> null);
		natives.put("java/lang/Class.registerNatives:()V", (args) -> null);
		natives.put("java/lang/System.registerNatives:()V", (args) -> null);
		natives.put("java/lang/Class.getPrimitiveClass:(Ljava/lang/String;)Ljava/lang/Class;", (args) -> null);//TODO:
		natives.put("java/lang/Float.floatToRawIntBits:(F)I", (args) -> Float.floatToRawIntBits((float) args.get(0)));
		natives.put("java/lang/Double.doubleToRawLongBits:(D)J", (args) -> Double.doubleToRawLongBits((double) args.get(0)));
		natives.put("java/lang/Double.longBitsToDouble:(J)D", (args) -> Double.longBitsToDouble((long) args.get(0)));
		natives.put("java/lang/reflect/Array.newArray:(Ljava/lang/Class;I)Ljava/lang/Object;",
				(args) -> new JVMList((int) args.get(1)));
		natives.put("java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V", args -> JVMList.arraycopy(args));
		
	}
	public void addClass(Class clazz) {
		pool.put(clazz.getName(), clazz);
		clazz.setClassPool(this);
		for(Method method:clazz.getMethods()){
			String name = clazz.getName() + "." + method.getName() + ":" + method.getType();
			if(natives.containsKey(name)){
				method.setNative(natives.get(name));
			}
		}
		Method clinit = clazz.getMethod("<clinit>", "()V", false);
		if (clinit !=null) {
			System.out.println("<clinit>" + clazz.getName());
			clinit.execute(Collections.emptyList());
			System.out.println("</clinit>" + clazz.getName());
		}
	}
	public synchronized Class getClazz(String clazzName) {
		Class result = pool.get(clazzName);
		if (result == null) {
			try {
				InputStream is = getNameResolver().apply(clazzName);
				result = Class.read(is, this);
			}  catch (IOException e) {
				System.out.println(pool);
				e.printStackTrace();
				throw new RuntimeException(clazzName+" "+e);
			}
		}
		return result;
	}
	public Function<String, InputStream> getNameResolver() {
		return nameResolver;
	}
	public void setNameResolver(Function<String, InputStream> nameResolver) {
		this.nameResolver = nameResolver;
	}

}
