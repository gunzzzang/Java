package practice9;
import javax.swing.*;
import java.awt.*;

public class practice9_3 extends JFrame{
	public practice9_3() {
		setTitle("GridLayout으로 10개의 버튼을 배치한 프레임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c= getContentPane();
		c.setLayout(new GridLayout(1,10));
		for(int i=0; i<10; i++) {
			c.add(new JButton(Integer.toString(i)));
		}
		
		setSize(600,200);
		setVisible(true);
	}

	public static void main(String[] args) {
		practice9_3 myframe = new practice9_3();

	}

}
