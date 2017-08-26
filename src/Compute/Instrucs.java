package Compute;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JLabel;

public class Instrucs {
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	JLabel rule = new JLabel();
	public Instrucs() {
		rule.setText("<html>Welcome to my game.<br><br>The goal of the game<br>is to take over the square...<br><br>or gain a strong economy<br>with a GDP of over 3,500,000.<br><br>Your economy grows with /develop<br><br>Tech gives you a gain between 0 and 5K<br><br>Education, between 1 and 3<br><br>and infrastructure gives a solid two - <br><br>which is then multiplied by your amount of squares.<br><br>Train using /train<br><br>Every time you click, the AI will play.<br>Click in between your turns.<br>You can see what they're doing in the console.<br><br>Good luck!</html>");
		panel.add(rule);
		panel.setSize(600, 800);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
