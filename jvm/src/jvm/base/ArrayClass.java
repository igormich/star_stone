package jvm.base;

import java.util.ArrayList;

public class ArrayClass extends Class {
	public static final ArrayClass instance = new ArrayClass();
	
	public ArrayClass() {
		constants = new ArrayList<>();
		methods = new ArrayList<>();
		constants.add(new Constant.UtfConstant("getClass", (Class)this));
		constants.add(new Constant.UtfConstant("()Ljava/lang/Class;", (Class)this));
		Method desiredAssertionStatus = new Method(ACC_PUBLIC, (short)1, (short)2, attributes, this);
		desiredAssertionStatus.setNative((args) -> Clazz.getInstanceFor(this));
		methods.add(desiredAssertionStatus);
	}
	
	@Override
	public String getName() {
		return "java/lang/Array";
	}
}
