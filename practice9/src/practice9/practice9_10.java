package practice9;
import javax.swing.*;
import java.awt.*;

public class practice9_10 extends JFrame{
	public practice9_10() {
		setTitle("자바 스윙 계산기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c= getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(new WestPanel(),BorderLayout.WEST);
		c.add(new CenterPanel(),BorderLayout.CENTER);
		
		setSize(300,300);
		setVisible(true);
	}

	class WestPanel extends JPanel{
		public WestPanel() {
			setLayout(new GridLayout(10,1));
			for(int i=0; i<10; i++) {
				JLabel la= new JLabel("         ");
				la.setOpaque(true);
				int r=(int)(Math.random()*256);
				int g=(int)(Math.random()*256);
				int b=(int)(Math.random()*256);
				la.setBackground(new Color(r,g,b));
				add(la);
			}
		}
	}
	
	
	
	class CenterPanel extends JPanel{
		public CenterPanel() {
			setLayout(null);
			
			for(int i=0; i<9; i++) {
				int x=(int)(Math.random()*150+50);
				int y=(int)(Math.random()*150+50);
				JLabel la= new JLabel(Integer.toString(i));
				la.setBounds(x,y,10,10);
				la.setForeground(Color.RED); // 숫자 색 바꾸기
				add(la);
			}
		}
	}

	public static void main(String[] args) {
		practice9_10 myframe = new practice9_10();

	}
}