package jvm.base;

public interface IAccessControl {

	public static final short ACC_PUBLIC = 0x0001;
	public static final short ACC_PRIVATE	= 0x0002;
	public static final short ACC_PROTECTED = 0x0004;	
	public static final short ACC_STATIC = 0x0008;
	public static final short ACC_FINAL	= 0x0010;
	public static final short ACC_SUPER	= 0x0020;
	public static final short ACC_SYNCHRONIZED = 0x0020;
	public static final short ACC_BRIDGE = 0x0040;
	public static final short ACC_VARARGS = 0x0080;
	public static final short ACC_NATIVE = 0x0100;
	public static final short ACC_INTERFACE	= 0x0200;
	public static final short ACC_ABSTRACT= 0x0400;
	public static final short ACC_STRICT = 0x0800;
	public static final short ACC_SYNTHETIC	= 0x1000;
	public static final short ACC_ANNOTATION = 0x2000;
	public static final short ACC_ENUM	= 0x4000;
	
	default boolean isPublic() {
		return (getAccess_flags() & ACC_PUBLIC) == ACC_PUBLIC;
	}
	default boolean isPrivate() {
		return (getAccess_flags() & ACC_PRIVATE) == ACC_PRIVATE;
	}
	default boolean isProtected() {
		return (getAccess_flags() & ACC_PROTECTED) == ACC_PROTECTED;
	}
	default boolean isStatic() {
		return (getAccess_flags() & ACC_STATIC) == ACC_STATIC;
	}
	default boolean isFinal() {
		return (getAccess_flags() & ACC_FINAL) == ACC_FINAL;
	}
	default boolean isSuper() {
		return (getAccess_flags() & ACC_SUPER) == ACC_SUPER;
	}	
	default boolean isSynchronized() {
		return (getAccess_flags() & ACC_SYNCHRONIZED) == ACC_SYNCHRONIZED;
	}	
	default boolean iBridge() {
		return (getAccess_flags() & ACC_BRIDGE) == ACC_BRIDGE;
	}	
	default boolean isVarargs() {
		return (getAccess_flags() & ACC_VARARGS) == ACC_VARARGS;
	}	
	default boolean isNative() {
		return (getAccess_flags() & ACC_NATIVE) == ACC_NATIVE;
	}
	default boolean isInterface() {
		return (getAccess_flags() & ACC_INTERFACE) == ACC_INTERFACE;
	}
	default boolean isAbsrtact() {
		return (getAccess_flags() & ACC_ABSTRACT) == ACC_NATIVE;
	}
	default boolean isStrict() {
		return (getAccess_flags() & ACC_STRICT) == ACC_STRICT;
	}
	default boolean isSyntheti() {
		return (getAccess_flags() & ACC_SYNTHETIC) == ACC_SYNTHETIC;
	}
	default boolean isAnnotation() {
		return (getAccess_flags() & ACC_ANNOTATION) == ACC_ANNOTATION;
	}
	default boolean isEnum() {
		return (getAccess_flags() & ACC_ENUM) == ACC_ENUM;
	}
	public short getAccess_flags();

}
