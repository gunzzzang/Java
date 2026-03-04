package practice12;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class practice12_1 extends JFrame {
	private MyPanel panel = new MyPanel();

	public practice12_1() {
		setTitle("텍스트 영역 만들기 예제");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(panel);
		
		setSize(300,300);
		setVisible(true);
	}
	class MyPanel extends JPanel {
		private Vector<Point> vStart = new Vector<Point>();
		private Vector<Point> vEnd = new Vector<Point>();

		public MyPanel() {
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					Point startp= e.getPoint();
					vStart.add(startp);
				}
				
				public void mouseReleased(MouseEvent e) {
					Point endp= e.getPoint();
					vEnd.add(endp);
					repaint();
				}
			});
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.BLUE);
			for(int i=0; i<vStart.size(); i++) {
				Point s= vStart.elementAt(i);
				Point e= vEnd.elementAt(i);
				g.drawLine((int)s.getX(), (int)s.getY(), (int)e.getX(), (int)e.getY());
			}
		}
	}

	public static void main(String[] args) {
		practice12_1 myframe = new practice12_1();

	}

}
