package practice10;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class practice10_7 extends JFrame{
	private JButton [] btn = new JButton[5];
	public practice10_7() {
		setTitle("클릭 횟수 카운트");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		Container c= getContentPane();
		c.setLayout(new FlowLayout());
		for(int i=0; i<btn.length; i++) {
			btn[i]= new JButton("0");
			add(btn[i]);
			btn[i].addMouseListener(new MyMouseListener());
		}
		
		setSize(350,250);
		setVisible(true);
		
		c.setFocusable(true);
		c.requestFocus();
		
	}
	
	class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			JButton b= (JButton)e.getSource();
			String s= b.getText();
			int n= Integer.parseInt(s);
			b.setText(Integer.toString(n+1));
		}
	}
	
	public static void main(String[] args) {
		practice10_7 myframe = new practice10_7();

	}

}
