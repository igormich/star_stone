package jvm;

public abstract class ClassPart {
	private final Class clazz;
	public ClassPart(Class clazz) {
		this.clazz = clazz;
	}
	public Constant getConstant(int index){
		return clazz.getConstant(index);
	}
	public Class getClazz(){
		return clazz;
	}
}
