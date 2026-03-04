package practice10;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class practice10_4 extends JFrame {
	private JLabel la = new JLabel();

	public practice10_4() {
		setTitle("상하좌우키로 블록 이동시키기");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(new NorthPanel(), BorderLayout.NORTH);
		c.add(new CenterPanel(), BorderLayout.CENTER);

		setSize(300, 300);
		setVisible(true);

	}

	class NorthPanel extends JPanel {
		public NorthPanel() {
			la.setText("상하좌우 키로 블록이 이동시킬 수 있습니다.");
			add(la);
			setBackground(Color.GRAY);

		}
	}

	class CenterPanel extends JPanel {
		private JLabel[] b = new JLabel[25];
		private int currentIndex = 12;

		public CenterPanel() {
			setBackground(Color.PINK);
			setLayout(new GridLayout(5, 5, 1, 1));

			for (int i = 0; i < 25; i++) {
				b[i] = new JLabel();
				b[i].setOpaque(true);
				b[i].setBackground(Color.WHITE);
				add(b[i]);
			}
			b[currentIndex].setBackground(Color.BLUE);
			addKeyListener(new MyKeyListener());
			setFocusable(true);
			requestFocus();
			
		}

		class MyKeyListener extends KeyAdapter {
			public void keyPressed(KeyEvent e) {
				b[currentIndex].setBackground(Color.WHITE);
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP: {
					if (currentIndex > 5)
						currentIndex -= 5;
					break;
				}

				case KeyEvent.VK_DOWN: {
					if (currentIndex < 20)
						currentIndex += 5;
					break;
				}
				case KeyEvent.VK_RIGHT: {
					if (currentIndex % 5 != 4)
						currentIndex++;
					break;
				}

				case KeyEvent.VK_LEFT: {
					if (currentIndex % 5 != 0)
						currentIndex--;
					break;

				}
				}
				b[currentIndex].setBackground(Color.BLUE);
			}

		}

	}

	public static void main(String[] args) {
		practice10_4 myframe = new practice10_4();

	}

}
