package practice11;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class practice11_1 extends JFrame{
	private JSlider [] sl = new JSlider[3];
	private JLabel colorLabel;
	public practice11_1() {
		setTitle("텍스트 영역 만들기 예제");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c= getContentPane();
		c.setLayout(new FlowLayout());
		
		for(int i=0; i<sl.length; i++) {
			sl[i]= new JSlider(JSlider.HORIZONTAL, 0, 250,125);
			sl[i].setPaintLabels(true);
			sl[i].setPaintTicks(true);
			sl[i].setPaintTrack(true);
			sl[i].setMajorTickSpacing(50);
			sl[i].setMinorTickSpacing(10);
			sl[i].addChangeListener(new MyChangeListener());
			c.add(sl[i]);
		}
		sl[0].setForeground(Color.RED);
		sl[1].setForeground(Color.GREEN);
		sl[2].setForeground(Color.BLUE);
		
		colorLabel = new JLabel("        SLIDER EXAMPLE        ");
		int r =sl[0].getValue();
		int g =sl[1].getValue();
		int b =sl[2].getValue();
		colorLabel.setOpaque(true);
		colorLabel.setBackground(new Color(r,g,b));
		c.add(colorLabel);
	
		
		setSize(300,230);
		setVisible(true);
	}		
	class MyChangeListener implements ChangeListener{
		public void stateChanged(ChangeEvent e) {
			int r =sl[0].getValue();
			int g =sl[1].getValue();
			int b =sl[2].getValue();
			colorLabel.setBackground(new Color(r,g,b));
		}
	}
	public static void main(String[] args) {
		practice11_1 myframe = new practice11_1();

	}

}
