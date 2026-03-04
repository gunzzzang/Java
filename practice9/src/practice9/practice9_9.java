package practice9;
import javax.swing.*;
import java.awt.*;

public class practice9_9 extends JFrame{
	public practice9_9() {
		setTitle("자바 스윙 계산기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c= getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(new NorthPanel(),BorderLayout.NORTH);
		c.add(new SouthPanel(),BorderLayout.SOUTH);
		c.add(new CenterPanel(),BorderLayout.CENTER);
		
		setSize(300,300);
		setVisible(true);
	}

	class NorthPanel extends JPanel{
		public NorthPanel() {
			setBackground(Color.GRAY);
			add(new JLabel("별 개수"));
			add(new JTextField(10));
			add(new JButton("별 만들기"));
			
		}
	}
	
	class SouthPanel extends JPanel{
		public SouthPanel() {
			setBackground(Color.YELLOW);
			add(new JButton("Exit"));
			
		}
	}
	
	class CenterPanel extends JPanel{
		public CenterPanel() {
			setLayout(null);
			JButton bt=new JButton("Refresh");
			bt.setBounds(10,10,100,30);
			add(bt);
			
			
			for(int i=0; i<15; i++) {
				int x=(int)(Math.random()*280);
				int y=(int)(Math.random()*180);
				JLabel la= new JLabel("*");
				la.setBounds(x,y,10,10);
				add(la);
			}
		}
	}

	public static void main(String[] args) {
		practice9_9 myframe = new practice9_9();

	}
}