package jvm.base;

public class AccessControl extends ClassPart implements IAccessControl {

	private short access_flags;

	public AccessControl(Class clazz, short access_flags) {
		super(clazz);
		this.access_flags = access_flags;
	}

	@Override
	public short getAccess_flags() {
		return access_flags;
	}

}
