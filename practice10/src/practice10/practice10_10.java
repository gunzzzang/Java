package practice10;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class practice10_10 extends JFrame{
	private JLabel la= new JLabel("    ");
	public practice10_10() {
		setTitle("마우스 올리기 내리기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		Container c= getContentPane();
		c.setLayout(null);
		
		la.addMouseListener(new MyMouseListener());
		la.addMouseMotionListener(new MyMouseMotionListener());
		c.addKeyListener(new MyKeyListener());
		
		setSize(350,350);
		setVisible(true);
		
		c.setFocusable(true);
	    c.requestFocus();
		
		
	}
	
	class MyMouseListener extends MouseAdapter {
		public void mouseEntered(MouseEvent e) {
			
			
			
		}
	}
	
	class MyMouseMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
			
		}
	}
	
	class MyKeyListener extends KeyAdapter {
		private Container c;
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_M) {
				int r=(int)(Math.random()*256);
				int g=(int)(Math.random()*256);
				int b=(int)(Math.random()*256);
				la.setOpaque(true);
				la.setBackground(new Color(r,g,b)); 
				
				la.setLocation(100,100);
				la.setSize(50,50);
				
				c.add(la);
	
			}
			
		}
	}
	
	public static void main(String[] args) {
		practice10_10 myframe = new practice10_10();

	}

}