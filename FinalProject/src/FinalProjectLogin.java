import javax.swing.*;
import java.awt.*;

public class FinalProjectLogin extends JFrame {

	private JLabel nicknameLabel; // "닉네임" 텍스트
	private JTextField nicknameField; // 닉네임 입력칸
	private JButton startButton; // 시작 버튼
	private JLabel statusLabel; // 상태 안내 문구

	public FinalProjectLogin() {
		super("닉네임 설정");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 250);
		setLocationRelativeTo(null);
		setResizable(false); // 창 크기를 사용자가 마우스 바꾸지 못하게 잠근다

		Container c = getContentPane();
		c.setLayout(new BorderLayout());

		JLabel title = new JLabel("365 Days of Market – 주식 시뮬레이터", JLabel.CENTER); // 상단
		title.setFont(new Font("Dialog", Font.BOLD, 16));

		// 주식 이미지 삽입
		ImageIcon icon = new ImageIcon("image/stock.png");
		Image img = icon.getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH); // 가로 100, 세로 60 크기로 덜 깨진 새 이미지를 만든다
		title.setIcon(new ImageIcon(img)); // title에 이미지 삽입

		c.add(title, BorderLayout.NORTH);
		c.add(new CenterPanel(), BorderLayout.CENTER); // 중앙
		c.add(new SouthPanel(), BorderLayout.SOUTH); // 하단
		// BGM 시작
		BGMPlayer.play("resources/bgm.wav"); // 배경음악 전용 클래스에서 play()메소드 이용해서 음악 파일 재생
	}

	// 중앙 패널 클래스
	class CenterPanel extends JPanel {
		public CenterPanel() {
			setLayout(null);

			nicknameLabel = new JLabel("닉네임");
			nicknameLabel.setBounds(30, 30, 60, 25);
			nicknameLabel.setFont(new Font("Dialog", Font.BOLD, 14));
			add(nicknameLabel);

			nicknameField = new JTextField();
			nicknameField.setBounds(90, 35, 250, 25);
			add(nicknameField);

			startButton = new JButton("시작");
			startButton.setBounds(120, 80, 140, 28);
			startButton.setFont(new Font("Dialog", Font.BOLD, 16));
			add(startButton);

			startButton.addActionListener(e -> onClickBtnStart());
		}
	}

	// 하단 패널 클래스
	class SouthPanel extends JPanel {
		public SouthPanel() {
			setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
			statusLabel = new JLabel("닉네임을 입력하고 [시작]을 누르세요.");
			add(statusLabel);
		}
	}

	// 닉네임 문자열 반환 메소드
	public String getNickName() {
		return nicknameField.getText().trim(); // 문자열 앞/뒤 공백 제거하는 trim() 이용
	}

	// 상태 문구 바꾸는 메소드
	public void setStatusMessage(String msg) {
		statusLabel.setText(msg);
	}

	// 로그인 화면에서 시작 버튼 눌렀을 때의 기능
	public void onClickBtnStart() {
		String nick = getNickName(); // 닉네임 반환 메소드 이용

		if (nick.isEmpty()) { // 닉네임 안 적었을 때
			setStatusMessage("닉네임을 입력하세요.");
			JOptionPane.showMessageDialog(this, "닉네임을 입력해야 시작할 수 있습니다.", // 본문에 표시될 메세지 내용
					"알림", // 타이틀에 들어갈 텍스트
					JOptionPane.WARNING_MESSAGE); // 경고 스타일
			return;
		}
		// 중앙데이터에 닉네임 저장
		GameData.get().setNickname(nick);
		// 닉네임이 들어온 경우
		setStatusMessage("환영합니다, " + nick + "님! 게임을 시작합니다.");
		new FinalProjectSelect(nick); // 종목 선택 화면 띄우기
		dispose(); // 현재 로그인 창 닫기
	}

	public static void main(String[] args) {
		FinalProjectLogin myframe = new FinalProjectLogin();
		myframe.setVisible(true);
	}
}
