package practice10;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class practice10_5 extends JFrame{
	private JLabel la= new JLabel("C");
	public practice10_5() {
		setTitle("클릭 연습");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		Container c= getContentPane();
		c.setLayout(null);
		c.add(la);
		
		la.addMouseListener(new MyMouseListener());
		la.setLocation(100,100);
		la.setSize(50,50);
		
		setSize(350,250);
		setVisible(true);
		
	}
	
	class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int x= (int)(Math.random()*300);
			int y= (int)(Math.random()*200);
			la.setLocation(x,y);
		}

	}
	
	public static void main(String[] args) {
		practice10_5 myframe = new practice10_5();

	}

}