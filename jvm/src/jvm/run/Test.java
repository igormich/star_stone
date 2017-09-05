package jvm.run;

import java.util.Collections;

import jvm.attributes.CodeAttribute;

public class Test {
	public static void main(String[] args) {	
		CodeRunner codeRunner = new CodeRunner(null, new CodeAttribute((short)5, (short)5, null, null, null, null), Collections.emptyList());
		/*codeRunner.stack.push(1);
		codeRunner.stack.push(2);
		codeRunner.stack.push(3);
		codeRunner.stack.push(4);
		System.out.println(codeRunner.stack);
		AsmCommand.executeByName("dup", codeRunner);
		System.out.println(codeRunner.stack);*/
		codeRunner.stack.push(262145);
		codeRunner.stack.push(19);
		System.out.println(codeRunner.stack);
		AsmCommand.executeByName("iushr", codeRunner);
		System.out.println(codeRunner.stack);
		System.out.println(262145>>19);
	}
}
