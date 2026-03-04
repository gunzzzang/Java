package practice9;
import javax.swing.*;
import java.awt.*;

public class practice9_7 extends JFrame{
	public practice9_7() {
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
			add(new JLabel("수식"));
			add(new JTextField(20));
			
		}
	}
	
	class SouthPanel extends JPanel{
		public SouthPanel() {
			setBackground(Color.YELLOW);
			add(new JLabel("계산 결과"));
			add(new JTextField(20));
			
		}
	}
	
	class CenterPanel extends JPanel{
		private String [] keys={"C","UN","BK","/"
				,"7", "8","9","X"
				,"4", "5","6","-"
				,"1", "2","3","+"
				,"0", ".","=","%"};
		
		public CenterPanel() {
			setLayout(new GridLayout(5,4,4,4));
			for(int i=0; i<keys.length; i++) {
				add(new JButton(keys[i]));
			}
		}
	}

	public static void main(String[] args) {
		practice9_7 myframe = new practice9_7();

	}
}

