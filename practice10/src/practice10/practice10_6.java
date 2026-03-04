package practice10;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class practice10_6 extends JFrame{
	private Color [] color;
	private 	JLabel la= new JLabel();
	public practice10_6() {
		setTitle("Wesst Grid 프레임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		Container c= getContentPane();
		c.setLayout(new BorderLayout());
		c.add(new WestPanel(), BorderLayout.WEST);
		c.add(new CenterPanel(), BorderLayout.CENTER);
		
		la.addMouseListener(new MyMouseListener());
		
		setSize(350,300);
		setVisible(true);
		
		c.setFocusable(true);
		c.requestFocus();
		
	}
	class WestPanel extends JPanel{
		public WestPanel() {
			Color [] color = {Color.RED,Color.WHITE,Color.BLUE,Color.YELLOW,Color.CYAN,
					Color.DARK_GRAY,Color.PINK,Color.GREEN,Color.ORANGE,Color.MAGENTA};
			setLayout(new GridLayout(10,1));
			for(int i=0; i<color.length; i++) {
				JButton btn= new JButton();
				btn.setBackground(color[i]);
				add(btn);
			}
		}
	}
	
	class CenterPanel extends JPanel{
		public CenterPanel() {
			setLayout(null);
			for(int i=0; i<10; i++) {
				JLabel la = new JLabel(Integer.toString(i));
				int x=(int)(Math.random()*150+50);
				int y=(int)(Math.random()*150+50);
				la.setLocation(x,y);
				la.setOpaque(true);
				la.setSize(20,20);
				add(la);
			}
		}
	}
	
	class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			
		
		}

	}
	
	public static void main(String[] args) {
		practice10_6 myframe = new practice10_6();

	}

}
