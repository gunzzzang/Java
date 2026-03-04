package practice10;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class practice10_9 extends JFrame{
	private JLabel la= new JLabel("Love Java");
	public practice10_9() {
		setTitle("마우스 휠 굴리기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		Container c= getContentPane();
		c.setLayout(new FlowLayout());
		c.add(la);
		
		la.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int n=e.getWheelRotation(); // 마우스 휠이 위쪽인지 아래쪽인지 구분
				Font f=la.getFont();
				float size=f.getSize();
				
				if(n<0) { //up
					la.setFont(f.deriveFont(size+5f));
				}
				else { // down
					if(size>10f)
					la.setFont(f.deriveFont(size-5f));	
				}
			}
			
		});
		
		setSize(350,250);
		setVisible(true);
		
	}
	
	
	public static void main(String[] args) {
		practice10_9 myframe = new practice10_9();

	}

}
