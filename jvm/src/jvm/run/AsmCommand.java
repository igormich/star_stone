package jvm.run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AsmCommand {
	private static final Function<CodeRunner, String> defaultstringifyer = (cr) -> {return "";};
	public static AsmCommand[] commands = new AsmCommand[]{
		new AsmCommand("nop", cr -> {}),
		new AsmCommand("aconst_null", cr -> {cr.push(null);}),
		new AsmCommand("iconst_m1", cr -> {cr.push(-1);}),
		new AsmCommand("iconst_0", cr -> {cr.push(0);}),
		new AsmCommand("iconst_1", cr -> {cr.push(1);}),
		new AsmCommand("iconst_2", cr -> {cr.push(2);}),
		new AsmCommand("iconst_3", cr -> {cr.push(3);}),
		new AsmCommand("iconst_4", cr -> {cr.push(4);}),
		new AsmCommand("iconst_5", cr -> {cr.push(5);}),
		new AsmCommand("lconst_0", cr -> {cr.push(0L);}),
		new AsmCommand("lconst_1", cr -> {cr.push(1L);}),
		new AsmCommand("fconst_0", cr -> {cr.push(0f);}),
		new AsmCommand("fconst_1", cr -> {cr.push(1f);}),
		new AsmCommand("fconst_2", cr -> {cr.push(2f);}),
		new AsmCommand("dconst_0", cr -> {cr.push(0.);}),
		new AsmCommand("dconst_1", cr -> {cr.push(1.);}),
		new AsmCommand("bipush", cr -> {cr.push(cr.getIndex1());}, cr-> {return "\t"+cr.getIndex1();},1),
		new AsmCommand("sipush", cr -> {cr.push(cr.getIndex2());}, cr-> {return "\t"+cr.getIndex2();},2),
		new AsmCommand("ldc", cr -> {cr.pushConstant1();},
				cr-> {return "\t"+cr.getIndex1() + "\t"+cr.getConstant1P();}, 1),
		new AsmCommand("ldc_w", cr -> {cr.pushConstant2();},
				cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2P();}, 2),
		new AsmCommand("ldc2_w", cr -> {cr.pushConstant2D();},
				cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2();}, 2),
		new AsmCommand("iload", cr -> {cr.push(cr.load(cr.getIndex1()));},
				cr-> {return "\t"+cr.getIndex1();}, 1),
		new AsmCommand("lload", cr -> {cr.push(cr.load(cr.getIndex1()));},
				cr-> {return "\t"+cr.getIndex1();}, 1),
		new AsmCommand("fload", cr -> {cr.push(cr.load(cr.getIndex1()));},
				cr-> {return "\t"+cr.getIndex1();}, 1),
		new AsmCommand("dload", cr -> {cr.push(cr.load(cr.getIndex1()));},
				cr-> {return "\t"+cr.getIndex1();}, 1),
		new AsmCommand("aload", cr -> {cr.push(cr.load(cr.getIndex1()));},
				cr-> {return "\t"+cr.getIndex1();}, 1),
		new AsmCommand("iload_0", cr -> {cr.push(cr.load(0));}),
		new AsmCommand("iload_1", cr -> {cr.push(cr.load(1));}),
		new AsmCommand("iload_2", cr -> {cr.push(cr.load(2));}),
		new AsmCommand("iload_3", cr -> {cr.push(cr.load(3));}),
		new AsmCommand("lload_0", cr -> {cr.push(cr.load(0));}),
		new AsmCommand("lload_1", cr -> {cr.push(cr.load(1));}),
		new AsmCommand("lload_2", cr -> {cr.push(cr.load(2));}),
		new AsmCommand("lload_3", cr -> {cr.push(cr.load(3));}),
		new AsmCommand("fload_0", cr -> {cr.push(cr.load(0));}),
		new AsmCommand("fload_1", cr -> {cr.push(cr.load(1));}),
		new AsmCommand("fload_2", cr -> {cr.push(cr.load(2));}),
		new AsmCommand("fload_3", cr -> {cr.push(cr.load(3));}),
		new AsmCommand("dload_0", cr -> {cr.push(cr.load(0));}),
		new AsmCommand("dload_1", cr -> {cr.push(cr.load(1));}),
		new AsmCommand("dload_2", cr -> {cr.push(cr.load(2));}),
		new AsmCommand("dload_3", cr -> {cr.push(cr.load(3));}),
		new AsmCommand("aload_0", cr -> {cr.push(cr.load(0));}),
		new AsmCommand("aload_1", cr -> {cr.push(cr.load(1));}),
		new AsmCommand("aload_2", cr -> {cr.push(cr.load(2));}),
		new AsmCommand("aload_3", cr -> {cr.push(cr.load(3));}),
		new AsmCommand("iaload", cr -> {cr.load();}),
		new AsmCommand("laload", cr -> {cr.loadD();}),
		new AsmCommand("faload", cr -> {cr.load();}),
		new AsmCommand("daload", cr -> {cr.loadD();}),
		new AsmCommand("aaload", cr -> {cr.load();}),
		new AsmCommand("baload", cr -> {cr.load();}),
		new AsmCommand("caload", cr -> {cr.load();}),
		new AsmCommand("saload", cr -> {cr.load();}),
		
		new AsmCommand("istore", cr -> {cr.store(cr.getIndex1());},
				cr-> {return "\t"+cr.getIndex1();}, 1),
		new AsmCommand("lstore", cr -> {cr.storeD(cr.getIndex1());},
				cr-> {return "\t"+cr.getIndex1();}, 1),
		new AsmCommand("fstore", cr -> {cr.store(cr.getIndex1());},
				cr-> {return "\t"+cr.getIndex1();}, 1),
		new AsmCommand("dstore", cr -> {cr.storeD(cr.getIndex1());},
				cr-> {return "\t"+cr.getIndex1();}, 1),
		new AsmCommand("astore", cr -> {cr.store(cr.getIndex1());},
				cr-> {return "\t"+cr.getIndex1();}, 1),
		new AsmCommand("istore_0", cr -> {cr.store(0);}),
		new AsmCommand("istore_1", cr -> {cr.store(1);}),
		new AsmCommand("istore_2", cr -> {cr.store(2);}),
		new AsmCommand("istore_3", cr -> {cr.store(3);}),
		new AsmCommand("lstore_0", cr -> {cr.storeD(0);}),
		new AsmCommand("lstore_1", cr -> {cr.storeD(1);}),
		new AsmCommand("lstore_2", cr -> {cr.storeD(2);}),
		new AsmCommand("lstore_3", cr -> {cr.storeD(3);}),
		new AsmCommand("fstore_0", cr -> {cr.store(0);}),
		new AsmCommand("fstore_1", cr -> {cr.store(1);}),
		new AsmCommand("fstore_2", cr -> {cr.store(2);}),
		new AsmCommand("fstore_3", cr -> {cr.store(3);}),
		new AsmCommand("dstore_0", cr -> {cr.storeD(0);}),
		new AsmCommand("dstore_1", cr -> {cr.storeD(1);}),
		new AsmCommand("dstore_2", cr -> {cr.storeD(2);}),
		new AsmCommand("dstore_3", cr -> {cr.storeD(3);}),
		new AsmCommand("astore_0", cr -> {cr.store(0);}),
		new AsmCommand("astore_1", cr -> {cr.store(1);}),
		new AsmCommand("astore_2", cr -> {cr.store(2);}),
		new AsmCommand("astore_3", cr -> {cr.store(3);}),
		
		new AsmCommand("iastore", cr -> {cr.store();}),
		new AsmCommand("lastore", cr -> {cr.storeD();}),
		new AsmCommand("fastore", cr -> {cr.store();}),
		new AsmCommand("dastore", cr -> {cr.storeD();}),
		new AsmCommand("aastore", cr -> {cr.store();}),
		new AsmCommand("bastore", cr -> {cr.store();}),
		new AsmCommand("castore", cr -> {cr.store();}),
		new AsmCommand("sastore", cr -> {cr.store();}),
		
		new AsmCommand("pop", cr -> {cr.pop();}),
		new AsmCommand("pop2", cr -> {cr.pop();cr.pop();}),
		new AsmCommand("dup", cr -> {cr.dup(1);}),
		new AsmCommand("dup_x1", cr -> {cr.dup(2);}),
		new AsmCommand("dup_x2", cr -> {cr.dup(3);}),
		new AsmCommand("dup2", cr -> {cr.dup2(1);}),
		new AsmCommand("dup2_x1", cr -> {cr.dup2(2);}),
		new AsmCommand("dup2_x2", cr -> {cr.dup2(3);}),
		new AsmCommand("swap", cr -> {cr.swap();}),
		new AsmCommand("iadd", cr -> {cr.push((Integer)cr.pop() + (Integer)cr.pop());}),
		new AsmCommand("ladd", cr -> {cr.push((Long)cr.popD() + (Long)cr.popD());}),
		new AsmCommand("fadd", cr -> {cr.push((Float)cr.pop() + (Float)cr.pop());}),
		new AsmCommand("dadd", cr -> {cr.push((Double)cr.popD() + (Double)cr.popD());}),
		new AsmCommand("isub", cr -> {cr.push(-(Integer)cr.pop() + (Integer)cr.pop());}),
		new AsmCommand("lsub", cr -> {cr.push(-(Long)cr.popD() + (Long)cr.popD());}),
		new AsmCommand("fsub", cr -> {cr.push(-(Float)cr.pop() + (Float)cr.pop());}),
		new AsmCommand("dsub", cr -> {cr.push(-(Double)cr.popD() + (Double)cr.popD());}),
		new AsmCommand("imul", cr -> {cr.push((Integer)cr.pop() * (Integer)cr.pop());}),
		new AsmCommand("lmul", cr -> {cr.push((Long)cr.popD() * (Long)cr.popD());}),
		new AsmCommand("fmul", cr -> {cr.push((Float)cr.pop() * (Float)cr.pop());}),
		new AsmCommand("dmul", cr -> {cr.push((Double)cr.popD() * (Double)cr.popD());}),
		new AsmCommand("idiv", cr -> {cr.push((Integer)cr.pop() / (Integer)cr.pop());}),
		new AsmCommand("ldiv", cr -> {cr.push((Long)cr.popD() / (Long)cr.popD());}),
		new AsmCommand("fdiv", cr -> {cr.push((Float)cr.pop() / (Float)cr.pop());}),
		new AsmCommand("ddiv", cr -> {cr.push((Double)cr.popD() / (Double)cr.popD());}),
		new AsmCommand("irem", cr -> {cr.push((Integer)cr.pop() % (Integer)cr.pop());}),
		new AsmCommand("lrem", cr -> {cr.push((Long)cr.popD() % (Long)cr.popD());}),
		new AsmCommand("frem", cr -> {throw new RuntimeException();}),
		new AsmCommand("drem", cr -> {throw new RuntimeException();}),
		new AsmCommand("ineg", cr -> {cr.push(-(Integer)cr.pop());}),
		new AsmCommand("lneg", cr -> {cr.push(-(Long)cr.popD());}),
		new AsmCommand("fneg", cr -> {cr.push(-(Float)cr.pop());}),
		new AsmCommand("dneg", cr -> {cr.push(-(Double)cr.popD());}),
		new AsmCommand("ishl", cr -> {Integer i1=(Integer)cr.pop();Integer i2=(Integer)cr.pop(); 
		cr.push(i2<<i1);}),
		new AsmCommand("lshl", cr -> {Integer i1=(Integer)cr.pop();Long i2=(Long)cr.pop(); 
		cr.push(i2<<i1);}),
		new AsmCommand("ishr", cr -> {Integer i1=(Integer)cr.pop();Integer i2=(Integer)cr.pop(); 
		cr.push(i2>>i1);}),
		new AsmCommand("lshr", cr -> {Long l1=(Long)cr.pop();Long l2=(Long)cr.pop(); 
		cr.push(l2>>>l1);}),
		new AsmCommand("iushr", cr -> {Integer i1=(Integer)cr.pop();Integer i2=(Integer)cr.pop(); 
				cr.push(i2>>>i1);}),
		new AsmCommand("lushr", cr -> {cr.push((Long)cr.popD()>>>(Integer)cr.pop());}),
		new AsmCommand("iand", cr -> {cr.push((Integer)cr.pop() & (Integer)cr.pop());}),
		new AsmCommand("land", cr -> {cr.push((Long)cr.popD() & (Long)cr.popD());}),
		new AsmCommand("ior", cr -> {throw new RuntimeException();}),
		new AsmCommand("lor", cr -> {throw new RuntimeException();}),
		new AsmCommand("ixor", cr -> {throw new RuntimeException();}),
		new AsmCommand("lxor", cr -> {throw new RuntimeException();}),
		new AsmCommand("iinc", cr -> {cr.inc();},
				cr-> {return "\t"+cr.getIndex1() + "\t"+cr.getIndex1(2);}, 2),
		new AsmCommand("i2l", cr -> {cr.push(((Number)cr.pop()).longValue());}),
		new AsmCommand("i2f", cr -> {cr.push(((Number)cr.pop()).floatValue());}),
		new AsmCommand("i2d", cr -> {cr.push(((Number)cr.pop()).doubleValue());}),
		new AsmCommand("l2i", cr -> {cr.push(((Number)cr.popD()).intValue());}),
		new AsmCommand("l2f", cr -> {cr.push(((Number)cr.popD()).floatValue());}),
		new AsmCommand("l2d", cr -> {cr.push(((Number)cr.popD()).doubleValue());}),
		new AsmCommand("f2i", cr -> {cr.push(((Number)cr.popD()).intValue());}),
		new AsmCommand("f2l", cr -> {cr.push(((Number)cr.pop()).longValue());}),
		new AsmCommand("f2d", cr -> {cr.push(((Number)cr.popD()).doubleValue());}),
		new AsmCommand("d2i", cr -> {cr.push(((Number)cr.popD()).intValue());}),
		new AsmCommand("d2l", cr -> {cr.push(((Number)cr.popD()).longValue());}),
		new AsmCommand("d2f", cr -> {cr.push(((Number)cr.popD()).floatValue());}),
		new AsmCommand("i2b", cr ->{cr.push((int)((Number)cr.pop()).byteValue());}),
		new AsmCommand("i2c", cr -> {cr.push(((Number)cr.pop()).byteValue());}),
		new AsmCommand("i2s", cr -> {cr.push((int)((Number)cr.pop()).shortValue());}),
		new AsmCommand("lcmp", cr -> {throw new RuntimeException();}),
		new AsmCommand("fcmpl", cr -> {throw new RuntimeException();}),
		new AsmCommand("fcmpg", cr -> {throw new RuntimeException();}),
		new AsmCommand("dcmpl", cr -> {throw new RuntimeException();}),
		new AsmCommand("dcmpg", cr -> {throw new RuntimeException();}),
		new AsmCommand("ifeq", cr -> {if((Integer)cr.pop()==0) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("ifne", cr -> {if((Integer)cr.pop()!=0) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("iflt", cr -> {if((Integer)cr.pop()<0) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("ifge", cr -> {if((Integer)cr.pop()>=0) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("ifgt", cr -> {if((Integer)cr.pop()>0) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("ifle", cr -> {if((Integer)cr.pop()<=0) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("if_icmpeq", cr -> {if(cr.eq()) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("if_icmpne", cr -> {if(!cr.eq()) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("if_icmplt", cr -> {if(cr.cmp() > 0) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("if_icmpge", cr -> {if(cr.cmp() <= 0) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("if_icmpgt", cr -> {if(cr.cmp() < 0) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("if_icmple", cr -> {if(cr.cmp() >= 0) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("if_acmpeq", cr -> {if(cr.eq()) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("if_acmpne", cr -> {if(!cr.eq()) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("goto", cr -> {cr.jmp(cr.getIndex2());},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("jsr", cr -> {throw new RuntimeException();}),
		new AsmCommand("ret", cr -> {throw new RuntimeException();}),
		new AsmCommand("tableswitch", cr -> {throw new RuntimeException();}),
		new AsmCommand("lookupswitch", cr -> {throw new RuntimeException();}),
		new AsmCommand("ireturn", cr -> {cr.ret();}),
		new AsmCommand("lreturn", cr -> {cr.ret();}),
		new AsmCommand("freturn", cr -> {cr.ret();}),
		new AsmCommand("dreturn", cr -> {cr.ret();}),
		new AsmCommand("areturn", cr -> {cr.ret();}),
		new AsmCommand("return", cr -> {cr.ret();}),
		new AsmCommand("getstatic", cr -> {cr.getStatic(cr.getConstant2());},
			cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2();}, 2),
		new AsmCommand("putstatic", cr -> {cr.putStatic(cr.getConstant2());},
				cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2P();}, 2),
		new AsmCommand("getfield", cr -> {cr.getField(cr.getConstant2());},
				cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2P();}, 2),
		new AsmCommand("putfield", cr -> {cr.setField(cr.getConstant2());},
				cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2P();}, 2),
		new AsmCommand("invokevirtual", cr -> {cr.call(cr.getConstant2(), true);},
				cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2();}, 2),
		new AsmCommand("invokespecial", cr -> {cr.call(cr.getConstant2(), false);},
			cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2P();}, 2),
		new AsmCommand("invokestatic", cr -> {cr.callStatic(cr.getConstant2());},
				cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2P();}, 2),
		new AsmCommand("invokeinterface", cr -> {cr.call(cr.getConstant2(), true);},
				cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2P()+ "\t"+cr.getIndex1(3);}, 4),
		new AsmCommand("invokedynamic", cr -> {cr.callDynamic(cr.getConstant2(), true);},
				cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2P();}, 4),
		new AsmCommand("new", cr -> {cr.newInstance(cr.getConstant2());},
				cr-> {return "\t"+cr.getIndex2() + "\t"+cr.getConstant2P();}, 2),
		new AsmCommand("newarray", cr -> {cr.push(new JVMList((int) cr.pop()));},
				cr-> {return "\t"+cr.getIndex1();}, 1),
		new AsmCommand("anewarray", cr -> {cr.push(new JVMList((int) cr.pop()));},
				cr-> {return "\t"+cr.getConstant2P();}, 2),
		new AsmCommand("arraylength", cr -> {cr.push(((JVMList)cr.pop()).size());}),
		new AsmCommand("athrow", cr -> {throw new RuntimeException();}),
		new AsmCommand("checkcast", cr -> {/*no check*/},
				cr-> {return "\t"+cr.getConstant2P();}, 2),
		new AsmCommand("instanceof", cr -> {throw new RuntimeException();}),
		new AsmCommand("monitorenter", cr -> {throw new RuntimeException();}),
		new AsmCommand("monitorexit", cr -> {throw new RuntimeException();}),
		new AsmCommand("wide", cr -> {throw new RuntimeException();}),
		new AsmCommand("multianewarray", cr -> {throw new RuntimeException();}),
		new AsmCommand("ifnull", cr -> {if(cr.pop()==null) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("ifnonnull", cr -> {if(cr.pop()!=null) {cr.jmp(cr.getIndex2());}},cr-> {return "\t"+cr.getIndex2();}, 2),
		new AsmCommand("goto_w", cr -> {throw new RuntimeException();}),
		new AsmCommand("jsr_w", cr -> {throw new RuntimeException();}),
		new AsmCommand("breakpoint", cr -> {throw new RuntimeException();}),
		new AsmCommand("impdep1", cr -> {throw new RuntimeException();}),
		new AsmCommand("impdep2", cr -> {throw new RuntimeException();})
	};

	public static void executeByName(String name,CodeRunner cr) {
		for (AsmCommand command:commands) {
			if (command.name.equals(name)) {
				command.execute(cr);
				return;
			}
		}
	}
	
	private String name;
	private Consumer<CodeRunner> action;
	private Function<CodeRunner, String> stringifyer;
	private int shift;
	
	public AsmCommand(String name, Consumer<CodeRunner> action,Function<CodeRunner, String> stringifyer,int shift){
		this.name = name;
		this.action = action;
		this.stringifyer = stringifyer;
		this.shift = shift;	
	}
	
	public AsmCommand(String name, Consumer<CodeRunner> action){
		this(name, action, defaultstringifyer, 0);
	}
	
	public String toString(CodeRunner cr){
		return name + stringifyer.apply(cr);
	}
	
	public int execute(CodeRunner cr){
		action.accept(cr);
		return shift;
	}

	public int getShift() {
		return shift;
	}

}
