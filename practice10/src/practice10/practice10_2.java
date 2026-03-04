package practice10;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class practice10_2 extends JFrame{
	public practice10_2() {
		setTitle("드래깅동안 바꾸기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		Container c= getContentPane();
		c.setBackground(Color.GREEN);
		
		MyMouseListener listener = new MyMouseListener();
		c.addMouseListener(listener);
		c.addMouseMotionListener(listener);
		
		setSize(350,250);
		setVisible(true);
		
	}
	
	class MyMouseListener extends MouseAdapter {
		public void mouseDragged(MouseEvent e) {
			Component c= (Component)e.getSource();
			c.setBackground(Color.YELLOW);
		
		}

		public void mouseReleased(MouseEvent e) {
			Component c= (Component)e.getSource();
			c.setBackground(Color.GREEN);
		}
	}
	
	public static void main(String[] args) {
		practice10_2 myframe = new practice10_2();

	}

}
