package jvm.gui;

import javax.swing.JFrame;

public class Gui {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Class View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
