package practice10;
import javax.swing.*;

import practice10.practice10_7.MyMouseListener;

import java.awt.*;
import java.awt.event.*;

public class practice10_8 extends JFrame{
	private JButton [] btn = new JButton[12];
	public practice10_8() {
		setTitle("마우스 올리기 내리기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		Container c= getContentPane();
		c.setLayout(new GridLayout(3,4,5,5));
		MyMouseListener listener = new MyMouseListener();
		
		for(int i=0; i<btn.length; i++) {
			btn[i]= new JButton(Integer.toString(i));
			btn[i].setBackground(Color.YELLOW);
			add(btn[i]);
			btn[i].addMouseListener(listener);
			
		}
		
		setSize(350,250);
		setVisible(true);
		
		c.setFocusable(true);
		c.requestFocus();
		
	}
	
	class MyMouseListener extends MouseAdapter {
		private JButton first=null;
		private JButton second =null;
		public void mouseClicked(MouseEvent e) {
			
			JButton b= (JButton)e.getSource();
			if(first==null) {
				first=b;
				first.setBackground(Color.MAGENTA);
			}
			else if(second==null){
				second=b;
				second.setBackground(Color.MAGENTA);
				
				String temp=first.getText();
				first.setText(second.getText());
				second.setText(temp);
			}
			else {
				first.setBackground(Color.YELLOW);
				second.setBackground(Color.YELLOW);
				first=b;
				second=null;
				first.setBackground(Color.MAGENTA);
			}
			
			
		}
	}
	
	public static void main(String[] args) {
		practice10_8 myframe = new practice10_8();

	}

}
