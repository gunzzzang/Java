package practice9;
import javax.swing.*;
import java.awt.*;

public class practice9_2 extends JFrame{
	public practice9_2(){
		setTitle("BorderLayout 배치 관리자 연습");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c= getContentPane();
		
		c.setBackground(Color.YELLOW);
		c.setLayout(new BorderLayout(5,7));
		c.add(new JButton("Center"), BorderLayout.CENTER);
		c.add(new JButton("North"), BorderLayout.NORTH);
		c.add(new JButton("South"), BorderLayout.SOUTH);
		c.add(new JButton("West"), BorderLayout.WEST);
		c.add(new JButton("East"), BorderLayout.EAST);
		
		setSize(400,300);
		setVisible(true);
	}

	public static void main(String[] args) {
		practice9_2 myframe = new practice9_2();

	}

}
