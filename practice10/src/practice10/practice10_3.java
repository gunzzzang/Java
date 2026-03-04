package practice10;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class practice10_3 extends JFrame{
	private JLabel la= new JLabel("Love Java");
	public practice10_3() {
		setTitle("left 키 이용");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		Container c= getContentPane();
		c.setLayout(new FlowLayout());
		c.add(la);
		
		c.addKeyListener(new MyKeyListener());
		
		setSize(350,250);
		setVisible(true);
		
		c.setFocusable(true);
		c.requestFocus();
		
	}
	
	class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_LEFT) {
				String text=la.getText();
				text=text.substring(1)+text.substring(0,1);
				la.setText(text);
			}
		}
	}
	
	public static void main(String[] args) {
		practice10_3 myframe = new practice10_3();

	}

}
