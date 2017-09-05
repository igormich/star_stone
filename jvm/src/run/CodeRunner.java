package run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import attributes.CodeAttribute;
import jvm.Class;
import jvm.Constant;
import jvm.Constant.ClassConstant;
import jvm.Constant.FieldRefConstant;
import jvm.Constant.MethodConstant;
import jvm.Method;
import jvm.Utils;

public class CodeRunner {
	static StringBuilder tabs=new StringBuilder();
	LinkedList<Object> stack;
	List<Object> local;
	int cp=0;
	private byte[] code;
	private CodeAttribute codeAttribute;
	private boolean ret = false;
	private JVMObject than;
	private int new_cp;
	
	public CodeRunner(byte[] code, CodeAttribute codeAttribute, JVMObject than, List<Object> args) {
		this.code = code;
		this.codeAttribute = codeAttribute;
		this.than = than;
		stack = new LinkedList<>();
		local = new ArrayList<>();
		local.addAll(args);
		local.addAll(Collections.nCopies(codeAttribute.getMaxLocals()-args.size(), null));
	}

	public void push(Object object) {
		if(object instanceof Long || object instanceof Double) {
			stack.push(null);
		}
		stack.push(object);
	}
	
	public int getIndex2(){
		return (short)( ((code[cp+1]&0xFF)<<8) | (code[cp+2]&0xFF));
	}
	
	public Constant getConstant2(){
		return codeAttribute.getConstant(getIndex2());
	}
	public String getConstant2P(){
		return Utils.escapeString(codeAttribute.getConstant(getIndex2()).toString());
	}
	public int getIndex1() {
		return getIndex1(1);
	}
	public int getIndex1(int shift) {
		return (short) ((short) code[cp+shift] & 0xff);
	}
	public Constant getConstant1() {
		return codeAttribute.getConstant(getIndex1());
	}
	public String getConstant1P() {
		return Utils.escapeString(codeAttribute.getConstant(getIndex1()).toString());
	}
	public void pushConstant1() {
		push(getConstant1().getValue());//TODO
	}
	public void pushConstant2() {
		push(getConstant2().getValue());//TODO
	}
	public void pushConstant2D() {
		push(getConstant2().getValue());//TODO
	}

	public Object load(int i) {
		return local.get(i);
	}
	public void store(int i) {
		local.set(i, pop());
	}
	public void storeD(int i) {
		local.set(i, pop());
		pop();
	}
	public Object pop() {
		return stack.pop();		
	}
	public Object popD() {
		Object result = stack.pop();
		stack.pop();
		return result;
	}
	public void dup(int pos) {
		stack.add(pos, stack.getFirst());		
	}
	public void dup2(int pos) {
		Object a = stack.get(0);
		Object b = stack.get(1);
		stack.add(pos + 1, b);		
		stack.add(pos + 1, a);	
	}
	public void swap() {
		Object a = stack.pop();
		Object b = stack.pop();
		stack.push(a);
		stack.push(b);
		
	}
	public boolean eq() {
		Object a = stack.pop();
		Object b = stack.pop();
		return a.equals(b);
	}
	public int cmp() {
		@SuppressWarnings("unchecked")
		Comparable<Object> a = (Comparable<Object>) stack.pop();
		@SuppressWarnings("unchecked")
		Comparable<Object> b = (Comparable<Object>) stack.pop();
		return a.compareTo(b);
	}
	public void load() {
		Integer index = (Integer) stack.pop();
		List<?> list = (List<?>) stack.pop();
		push(list.get(index));
	}
	public void loadD() {
		Integer index = (Integer) stack.pop();
		List<?> list = (List<?>) stack.pop();
		push(list.get(index));
	}
	public void store() {
		Object value =  stack.pop();
		Integer index = (Integer) stack.pop();
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) stack.pop();
		list.set(index.intValue(),value);
	}
	public void storeD() {
		Object value =  stack.pop();
		stack.pop();
		Integer index = (Integer) stack.pop();
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) stack.pop();
		list.set(index.intValue(),value);
	}
	public void jmp(int cp) {
		System.out.println(tabs + "jmp " +cp);
		new_cp += cp;
		
	}
	public Object execute() {
		try{
			cp = 0;
			while (!ret) {
				int commandCode = (int) code[cp] & 0xff;
				AsmCommand command = AsmCommand.commands[commandCode];
				System.out.println(tabs + command.toString(this));
				new_cp  = cp;
				command.execute(this);
				if(new_cp == cp) {
					cp+=command.getShift();
					cp++;
				} else {
					cp=new_cp;
				}
				System.out.println(tabs + "local " +local);
				System.out.println(tabs + "stack " +stack);
			}
		}catch (RuntimeException e) {
			e.printStackTrace();
			System.out.println(tabs + "local " +local);
			System.out.println(tabs + "stack " +stack);
			System.exit(0);
		}
		if(tabs.length()>0)
			tabs.setLength(tabs.length()-1);
		if(!stack.isEmpty()) {
			return stack.pop();
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		try{
			cp = 0;
			while (cp < code.length) {
				int commandCode = (int) code[cp] & 0xff;
				AsmCommand command = AsmCommand.commands[commandCode];
				result.append(command.toString(this));
				cp+=command.getShift();
				cp++;
				result.append("\r\n");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();	
	}

	public void callStatic(Constant constant) {
		MethodConstant methodConstant = (MethodConstant) constant;
		String clazzName = methodConstant.getClazzConstant().toString();
		String methodName = methodConstant.getNameAndTypeConstant().getNameConstant().toString();
		String methodType = methodConstant.getNameAndTypeConstant().getTypeConstant().toString();
		Class clazz = codeAttribute.getClazz().getClassPool().getClazz(clazzName);
		Method method = clazz.getMethod(methodName, methodType);
		//System.out.println(method);
		LinkedList<Object> args = new LinkedList<>();
		int count = getArgsCount(methodType);
		for(int i=0;i<count;i++){
			args.push(stack.pop());
			if (args.getFirst() instanceof Double || args.getFirst() instanceof Long) {
				//for long and double
				stack.pop();
			}
		}
		if (!method.isNative()) {
			tabs.append("\t");
			Object o = method.execute(null, args);
			if(!methodType.endsWith("V"))
				push(o);		
		} else {
			if(methodName.equals("print")){
				if(args.get(0) instanceof JVMObject) {
					JVMObject jvmObject = (JVMObject) args.get(0);
					List<Integer> l = (List<Integer>) jvmObject.getField("value");
					String s = l.stream().map(i -> Character.valueOf((char)i.intValue()).toString()).collect(Collectors.joining());
					System.out.println(s);
				} else {
					System.out.println(args.get(0));
				}
			}
		}
	}
	
	public void call(Constant constant) {
		MethodConstant methodConstant = (MethodConstant) constant;
		String clazzName = methodConstant.getClazzConstant().toString();
		String methodName = methodConstant.getNameAndTypeConstant().getNameConstant().toString();
		String methodType = methodConstant.getNameAndTypeConstant().getTypeConstant().toString();
		Class clazz = codeAttribute.getClazz().getClassPool().getClazz(clazzName);
		Method method = clazz.getMethod(methodName, methodType);
		//System.out.println(method);
		LinkedList<Object> args = new LinkedList<>();
		int count = getArgsCount(methodType);
		for(int i=0;i<count;i++){
			args.push(stack.pop());
			if (args.getFirst() instanceof Double || args.getFirst() instanceof Long) {
				//for long and double
				stack.pop();
			}
		}
		Object than = stack.pop();
		args.push(than);
		tabs.append("\t");
		Object o = method.execute((JVMObject) than, args);
		if(!methodType.endsWith("V"))
			push(o);	
	}

	private int getArgsCount(String type) {
		type = type.replaceAll("L.*?;", "L");
		type = type.replaceAll("\\[", "");
		return type.length()-3;
	}

	public void ret() {
		ret = true;
	}

	public void putStatic(Constant constant) {
		FieldRefConstant fieldRefConstant = (FieldRefConstant) constant;	
		String clazzName = fieldRefConstant.getClazzConstant().toString();
		String fieldName = fieldRefConstant.getNameAndTypeConstant().getNameConstant().toString();
		//String methodType = fieldRefConstant.getNameAndTypeConstant().getTypeConstant().toString();
		Class clazz = codeAttribute.getClazz().getClassPool().getClazz(clazzName);
		clazz.setStatic(fieldName, stack.pop());
	}
	public void getStatic(Constant constant) {
		FieldRefConstant fieldRefConstant = (FieldRefConstant) constant;	
		String clazzName = fieldRefConstant.getClazzConstant().toString();
		String fieldName = fieldRefConstant.getNameAndTypeConstant().getNameConstant().toString();
		//String methodType = fieldRefConstant.getNameAndTypeConstant().getTypeConstant().toString();
		Class clazz = codeAttribute.getClazz().getClassPool().getClazz(clazzName);
		Object o = clazz.getStatic(fieldName);
		push(o);
	}

	public void inc() {
		Integer i = (Integer) local.get(getIndex1());
		i += code[cp+2];
		local.set(getIndex1(), i);
	}

	public void newInstance(Constant constant) {
		ClassConstant clazzConstant = (ClassConstant) constant;
		String clazzName = clazzConstant.toString();
		Class clazz = codeAttribute.getClazz().getClassPool().getClazz(clazzName);
		push(new JVMObject(clazz)); 
	}

	public void setField(Constant constant) {
		FieldRefConstant fieldRefConstant = (FieldRefConstant) constant;	
		String clazzName = fieldRefConstant.getClazzConstant().toString();
		String fieldName = fieldRefConstant.getNameAndTypeConstant().getNameConstant().toString();
		Object value = pop();
		JVMObject jvmObject = (JVMObject) pop();
		jvmObject.setField(fieldName, value);
	}
	public void getField(Constant constant) {
		FieldRefConstant fieldRefConstant = (FieldRefConstant) constant;	
		String clazzName = fieldRefConstant.getClazzConstant().toString();
		String fieldName = fieldRefConstant.getNameAndTypeConstant().getNameConstant().toString();
		JVMObject jvmObject = (JVMObject) pop();
		push(jvmObject.getField(fieldName));
	}
}
