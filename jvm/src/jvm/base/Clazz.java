package jvm.base;

import java.util.ArrayList;

import jvm.run.JVMObject;

public class Clazz extends Class {
	
	private static Clazz instance = new Clazz();
	
	public Clazz() {
		constants = new ArrayList<>();
		methods = new ArrayList<>();
		constants.add(new Constant.UtfConstant("desiredAssertionStatus", this));
		constants.add(new Constant.UtfConstant("()Z", this));
		Method desiredAssertionStatus = new Method(ACC_PUBLIC, (short)1, (short)2, attributes, this);
		desiredAssertionStatus.setNative((args) ->{return 0;});

		methods.add(desiredAssertionStatus);
		constants.add(new Constant.UtfConstant("getComponentType", (Class)this));
		constants.add(new Constant.UtfConstant("()Ljava/lang/Class;", (Class)this));
		Method getComponentType = new Method(ACC_PUBLIC, (short)3, (short)4, attributes, this);
		getComponentType.setNative((args) -> getInstanceFor(ClassPool.instance.getClazz("java/lang/Object")));//TODO:
		methods.add(getComponentType);
	}

	public static JVMObject getInstanceFor(Class clazz){
		//desiredAssertionStatus:()Z
		JVMObject result = new JVMObject(instance);
		result.setField("clazz", clazz);
		return result;
	}

	@Override
	public String getName() {
		return "java/lang/Class";
	}

}
