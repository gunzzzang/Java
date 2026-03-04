package practice9;
import javax.swing.*;
import java.awt.*;

public class practice9_6 extends JFrame{
	public practice9_6() {
		setTitle("배치관리자가 없는 컨테이너");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c= getContentPane();
		c.setLayout(null);
		
		for(int i=0; i<20; i++) {
			JLabel label= new JLabel();
			
			label.setOpaque(true);
			int r=(int)(Math.random()*256);
			int g=(int)(Math.random()*256);
			int b=(int)(Math.random()*256);
			label.setBackground(new Color(r,g,b));
			
			int x=(int)(Math.random()*240 +10);
			int y=(int)(Math.random()*240 +10);
			label.setLocation(x,y);
			label.setSize(10,10);
			c.add(label);
			
		}
		setSize(300,300);
		setVisible(true);
	}

	public static void main(String[] args) {
		practice9_6 myframe = new practice9_6();

	}

}
