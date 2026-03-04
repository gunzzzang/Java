import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OddEvenGame extends JFrame {
    private JLabel Number;
    private JLabel NbResult;
    private JButton btnOdd, btnEven, btnCheck, btnReset;
    private int randomNum;   //1~9 숫자
    private String selected;  // 홀 or 짝
    private boolean revealed; // 숫자 공개 여부

    public OddEvenGame() {
        setTitle("홀짝 맞추기 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(300, 300);
        setLocation(200,200);

        Container c = getContentPane();
        c.setLayout(null); 

        NbResult = new JLabel("무엇일까요?", JLabel.CENTER);
        NbResult.setBounds(50, 10, 200, 30);
        c.add(NbResult);

        // ? 박스
        Number = new JLabel("?", JLabel.CENTER);
        Number.setOpaque(true);
        Number.setBackground(Color.MAGENTA); 
        Number.setForeground(Color.WHITE); // 글자색은 하얀색
        Number.setFont(new Font("맑은 고딕", Font.BOLD, 40)); // 보류
        Number.setBounds(100, 50, 80, 60);
        c.add(Number);

        JButton btnOdd = new JButton("홀");
        btnOdd.setBounds(20, 150, 50, 30);
        c.add(btnOdd);

        JButton btnEven = new JButton("짝");
        btnEven.setBounds(80, 150, 50, 30);
        c.add(btnEven);

        JButton btnCheck = new JButton("확인");
        btnCheck.setBounds(140, 150, 60, 30);
        c.add(btnCheck);

        JButton btnReset = new JButton("다시");
        btnReset.setBounds(210, 150, 60, 30);
        c.add(btnReset);
        
        // 버튼 관련이므로 액션 리스너 사용
        btnOdd.addActionListener(new OddListener());
        btnEven.addActionListener(new EvenListener());
        btnCheck.addActionListener(new CheckListener());
        btnReset.addActionListener(new ResetListener());

        // 시작 상태 세팅
        resetGame();

        setVisible(true);
    }

    // 홀
    class OddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selected = "홀";
            NbResult.setText("'확인'을 클릭!");
        }
    }

    // 짝
    class EvenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selected = "짝";
            NbResult.setText("'확인'을 클릭!");
        }
    }

    // 확인
    class CheckListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selected == null) {
               NbResult.setText("홀이나 짝을 먼저 선택");
                return;
            }

            // 처음 확인할 때만 숫자 보여주기
            if (!revealed) { // 숫자가 안 보일 때 
               Number.setText(Integer.toString(randomNum));
                revealed = true; // 공개된 상태로 표시
            }

            boolean isOdd = (randomNum % 2 == 1); // 정답이 홀일때
            boolean userOdd = selected.equals("홀");// 사용자의 선택이 홀일떄
            String correctText;
            
			if (isOdd) {
				correctText = "홀";
			} 
			else {
				correctText = "짝";
			}
			
			if (isOdd == userOdd) {
				NbResult.setText(correctText + "!맞았어요");
			}

			else {
				NbResult.setText(correctText + "!아쉽군요");
			}
               
        }
    }

    class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            resetGame();
        }
    }

    // 리셋 함수
    private void resetGame() {
        randomNum = (int)(Math.random() * 9) + 1; // 1~9
        selected = null;
        revealed = false;
        Number.setText("?");
        NbResult.setText("무엇일까요?");
    }

    public static void main(String[] args) {
        OddEvenGame game= new OddEvenGame();
    }
}
