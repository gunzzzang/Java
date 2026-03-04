package practice9;
import javax.swing.*;
import java.awt.*;

public class practice9_8 extends JFrame{
	public practice9_8() {
		setTitle("16장 카드의 뒷면...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c= getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(new NorthPanel(),BorderLayout.NORTH);
		c.add(new SouthPanel(),BorderLayout.SOUTH);
		c.add(new CenterPanel(),BorderLayout.CENTER);
		c.add(new EastPanel(),BorderLayout.EAST);
		c.add(new WestPanel(),BorderLayout.WEST);
		
		setSize(300,300);
		setVisible(true);
	}

	
	class NorthPanel extends JPanel{
		public NorthPanel() {
			setBackground(Color.YELLOW);
			add(new JLabel("숨겨진 이미지 찾기"));
		}
	}
	
	class SouthPanel extends JPanel{
		public SouthPanel() {
			setBackground(Color.YELLOW);
			add(new JButton("실행 시작"));
		}
	}
	
	class WestPanel extends JPanel{
		public WestPanel() {
			add(new JLabel("            "));
		}
	}
	
	class EastPanel extends JPanel{
		public EastPanel() {
			add(new JLabel("            "));
		}
	}
	
	class CenterPanel extends JPanel{
		public CenterPanel() {
			setLayout(new GridLayout(4,4,5,5));
			for(int i=0; i<16; i++) {
				JLabel pa = new JLabel(Integer.toString(i));
				pa.setBackground(Color.GREEN);
				pa.setOpaque(true);
				add(pa);
			}
		}
	}

	public static void main(String[] args) {
		practice9_8 myframe = new practice9_8();

	}
}