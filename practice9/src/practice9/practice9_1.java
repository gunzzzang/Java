package practice9;
import javax.swing.*;
import java.awt.*;

public class practice9_1 extends JFrame{
	public practice9_1(){
		setTitle("Make a Window using Swing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c= getContentPane();
		c.setBackground(Color.YELLOW);
		setSize(400,150);
		setVisible(true);
		
	}
		
	public static void main(String[] args) {
		practice9_1 myframe = new practice9_1();
	

	}

}
	

