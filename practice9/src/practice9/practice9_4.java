package practice9;
import javax.swing.*;
import java.awt.*;

public class practice9_4 extends JFrame{
	private Color [] color= {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
			Color.CYAN, Color.BLUE, Color.MAGENTA, 
			Color.GRAY, Color.PINK, Color.LIGHT_GRAY};
	
	public practice9_4() {
		setTitle("배경색을 가진 10개의 버튼");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c= getContentPane();
		c.setLayout(new GridLayout(1,10));
		
		for(int i=0; i<10; i++) {
			JButton btn= new JButton(Integer.toString(i));
			btn.setBackground(color[i]);
			c.add(btn);
		}
		setSize(600,200);
		setVisible(true);
	}

	public static void main(String[] args) {
		practice9_4 myframe = new practice9_4();

	}

}