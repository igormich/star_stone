package jvm.attributes;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jvm.base.Class;
import jvm.base.ClassPart;
import jvm.base.Constant;
import jvm.base.Constant.MethodConstant;
import jvm.base.Constant.MethodHandleConstant;

public class BootstrapMethodsAttribute extends Attribute {

	private List<BootstrapMethod> bootstrapMethods;

	public static class BootstrapMethod extends ClassPart{

		private short method_ref;
		private List<Constant> arguments;

		public BootstrapMethod(short method_ref, List<Constant> arguments, Class clazz) {
			super(clazz);
			this.method_ref = method_ref;
			this.arguments = arguments;
		}
		public MethodHandleConstant getMethod() {
			return (MethodHandleConstant) getConstant(method_ref);
		}
		public List<Constant> getArguments() {
			return arguments;
		}
	}
	public BootstrapMethodsAttribute(List<BootstrapMethod> bootstrapMethods, Class clazz) {
		super(clazz);
		this.bootstrapMethods = bootstrapMethods;
	}

	public static BootstrapMethodsAttribute read(DataInputStream dis, Class clazz) throws IOException {
		short num_bootstrap_methods = dis.readShort();
		List<BootstrapMethod> bootstrapMethods = new ArrayList<>(num_bootstrap_methods);
		for(int i=0;i<num_bootstrap_methods;i++) {
			bootstrapMethods.add(readBootstrapMethod(dis,clazz));
			
		}
		return new BootstrapMethodsAttribute(bootstrapMethods, clazz);
	}

	private static BootstrapMethod readBootstrapMethod(DataInputStream dis, Class clazz) throws IOException {
		short method_ref = dis.readShort();
		short num_arguments = dis.readShort();
		List<Constant> arguments = new ArrayList<>(num_arguments);
		//System.out.println(clazz.getConstant(method_ref));
		for(int i=0;i<num_arguments;i++) {
			short argument_ref = dis.readShort();
			arguments.add(clazz.getConstant(argument_ref));
		}
		return new BootstrapMethod(method_ref, arguments, clazz);
	}

	public BootstrapMethod getMethod(short methodIndex) {
		return bootstrapMethods.get(methodIndex);
	}

}
