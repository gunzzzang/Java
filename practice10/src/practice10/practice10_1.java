package practice10;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class practice10_1 extends JFrame{
	private JLabel la= new JLabel("자바");
	public practice10_1() {
		setTitle("마우스 올리기 내리기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		Container c= getContentPane();
		c.setLayout(new FlowLayout());
		c.add(la);
		
		la.addMouseListener(new MyMouseListener());
		
		setSize(350,250);
		setVisible(true);
			
	}
	
	class MyMouseListener extends MouseAdapter {
		public void mouseEntered(MouseEvent e) {
			la.setText("Love Java");
		
		}

		public void mouseExited(MouseEvent e) {
			la.setText("사랑해 자바");
		}
	}
	
	public static void main(String[] args) {
		practice10_1 myframe = new practice10_1();

	}

}


