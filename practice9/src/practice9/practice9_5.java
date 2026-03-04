package practice9;
import javax.swing.*;
import java.awt.*;

public class practice9_5 extends JFrame{
	private Color [] color= {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
			Color.CYAN, Color.BLUE, Color.MAGENTA, 
			Color.GRAY, Color.PINK, Color.LIGHT_GRAY,
			Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
			Color.CYAN, Color.BLUE};

	public practice9_5() {
		setTitle("4x4 Color 프레임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c= getContentPane();
		c.setLayout(new GridLayout(4,4));
		
		for(int i=0; i<16; i++) {
			JLabel la = new JLabel(Integer.toString(i));
			la.setOpaque(true); // 제이라벨은 기본적으로 배경색이 불투명하므로 투명으로 바꿔야함
			la.setBackground(color[i]);
			c.add(la);
		}
		
		setSize(500,200);
		setVisible(true);
		
	}

	public static void main(String[] args) {
		practice9_5 myframe = new practice9_5();

	}

}