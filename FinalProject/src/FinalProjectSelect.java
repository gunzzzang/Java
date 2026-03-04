import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FinalProjectSelect extends JFrame {

	private NorthPanel northPanel; // 상단 영역, 날짜·자본 표시와 날짜 스킵, 보유종목/거래내역 버튼을 담당
	private CenterPanel centerPanel; // 중앙 영역, 종목 목록과 종목 클릭을 담당
	private SouthPanel southPanel; // 하단 영역, 최근 발생한 뉴스 표시 담당

	private int currentDay; // 현재 게임 일차
	private int capital; // 보유 자본
	private String nickname; // 닉네임

	public FinalProjectSelect(String nickname) {
		super("주식 시뮬레이터 - 종목 선택 화면");

		// GameData에서 현재 상태를 가져와서 초기 값 세팅
		this.currentDay = GameData.get().getDay();
		this.capital = GameData.get().getCapital();
		this.nickname = nickname;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setResizable(false);

		Container c = getContentPane();
		c.setLayout(new BorderLayout()); // 레이아웃 설정

		northPanel = new NorthPanel();
		c.add(northPanel, BorderLayout.NORTH);

		centerPanel = new CenterPanel();
		c.add(centerPanel, BorderLayout.CENTER);

		southPanel = new SouthPanel();
		c.add(southPanel, BorderLayout.SOUTH);

		refreshView(); // 화면 갱신하는 메소드 이용
		setVisible(true);
	}

	public FinalProjectSelect() {
		this("유저");
	}

	public int getCurrentDay() {
		return GameData.get().getDay();
	}

	// 상단 정보/뉴스/종목 가격 등등 현재 GameData 기준으로 갱신
	public void refreshView() {
		currentDay = GameData.get().getDay();
		capital = GameData.get().getCapital();

		String infoText = String.format( // 포멧을 이용한 상단 문구
				"%s님  ·  자본 %,d원  ·  %d일차", nickname, capital, currentDay);

		northPanel.setTopInfo(infoText); // 상단 라벨 문구 교체
		southPanel.refreshNews(); // 뉴스를 최신 뉴스로 갱신하는 메소드 사용

		StockRowPanel[] rows = centerPanel.getRows(); // 메소드(231번 줄)를 사용해서 centerPanel안에 있는 StockRowPanel을 가져옴
		for (int i = 0; i < rows.length; i++) {
			StockRowPanel row = rows[i];
			row.refreshFromGameData(); // GameData에서 정보를 가져와서 종목을 최신화하는 메소드(305번 줄) 사용
		}
		repaint(); // 화면 다시 그림
	}

	// 날짜 스킵
	public void skipDays(int amount) {
		if (amount <= 0)
			return; // 음수일 때 메소드 종료
		GameData.get().skipDay(amount); // GameData 안에 있는 메소드 호출
		refreshView(); // 화면 갱신 메소드(55번 줄) 사용
	}

	// 날짜 스킵 스레드버전(한꺼번에 많이 스킵할 때 계산량이 많으므로 UI 스레드가 차단되는걸 막기위해 백그라운드 스레드에서 처리함)
	public void skipDaysWithThread(int amount) {
		if (amount <= 0)
			return; // 음수일 때 메소드 종료

		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // 마우스 커서가 로딩커서로 바뀜 ->지금 백그라운드 작업 중이라는 시각적 피드백
		new Thread(() -> {
			for (int i = 0; i < amount; i++) {
				GameData.get().skipDay(1); // 하루씩 스킵

				try { // try-catch 구문 사용
					Thread.sleep(15); // 짧게 텀을 둔다
				} catch (InterruptedException e) { // 스레드 강제 중단시 예외처리
					break;
				}
			}
			refreshView(); // 화면 갱신 메소드(55번 줄) 사용
	        setCursor(Cursor.getDefaultCursor()); // 마우스 로딩커서를 기본커서로 되돌림
		}).start(); // 스레드 실행 시작
	}

	public static void main(String[] args) {
		new FinalProjectSelect();
	}
}

// 상단 영역
class NorthPanel extends JPanel {

	private JLabel infoLabel; // 자본/일차/닉네임 정보 보여줌
	private JButton skipButton; // 스킵 버튼
	private JTextField skipDaysField; // 스킵 텍스트 입력칸
	private JButton holdingButton; // 내 보유종목 버튼
	private JButton tradeButton; // 거래 내역 버튼
	private JToggleButton bgmToggle; // BGM ON/OFF 버튼

	public NorthPanel() {
		setLayout(new BorderLayout());

		JPanel topSection = new JPanel(new BorderLayout());
		topSection.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12)); // 여백을 위6, 왼12, 아래6, 오른12으로 두고 설정

		infoLabel = new JLabel("자본 1000만원  ·  1일차"); // 초기 텍스트
		infoLabel.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 16f)); // 글자 크기 16
		topSection.add(infoLabel, BorderLayout.WEST); // 서쪽에 정보 라벨
		
		// BGM ON/OFF 버튼
		bgmToggle = new JToggleButton("BGM ON"); // 토글 버튼(눌림/안눌림 상태를 가지는 버튼)을 만듬

		bgmToggle.addActionListener(e -> {
		    BGMPlayer.toggle();   // 버튼 누르면 토글 호출

		    if (BGMPlayer.isPlaying()) { // 토글하고 음악이 재생중이면 true
		        bgmToggle.setText("BGM ON"); // 버튼 글자를 바꿈
		    } else {
		        bgmToggle.setText("BGM OFF");
		    }
		});

		JPanel bgmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		bgmPanel.setOpaque(false);
		bgmPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0)); 
		bgmPanel.add(bgmToggle);

		topSection.add(bgmPanel, BorderLayout.CENTER);

		JPanel skipPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		skipPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
		skipButton = new JButton("스킵");
		skipDaysField = new JTextField(8);
		skipPanel.add(skipButton);
		skipPanel.add(skipDaysField);
		topSection.add(skipPanel, BorderLayout.EAST); // 오른쪽에 스킵 버튼 라벨

		JPanel quickPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		quickPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 8, 12));
		holdingButton = new JButton("내 보유종목");
		tradeButton = new JButton("거래내역");
		quickPanel.add(holdingButton);
		quickPanel.add(tradeButton);

		add(topSection, BorderLayout.NORTH); // 상단 영역 (자본·날짜 정보와 날짜 스킵 입력/버튼)
		add(quickPanel, BorderLayout.SOUTH); // 하단 영역 (보유종목·거래내역 버튼)

		skipButton.addActionListener(e -> onClickBtnSkip()); // 스킵 버튼 누를 떄
		holdingButton.addActionListener(e -> new DemoScreenTrade().setVisible(true)); // 보유종목 버튼 누를 때 DemoScreenTrade 새 화면 띄움
																						
		tradeButton.addActionListener(e -> { // 거래 내역 버튼 누를 때
			JFrame frame = new JFrame("거래내역");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(900, 600);
			frame.add(new DemoTradeScreen()); // DemoTradeScreen 화면을 넣음
			frame.setVisible(true);
		});
	}

	public void setTopInfo(String text) {
		infoLabel.setText(text); // 상단 정보 문구(자본, 날짜 닉네임)를 갱신할 때 쓰는 메소드
	}

	private int calcSkipAmount() { // 스킵일 수 계산 메소드
		String text = skipDaysField.getText().trim(); // 스킵일수를 공백을 제거한 문자열로 가져옴
		if (text.isEmpty())
			return 1;

		try { // try-catch 구문 사용
			int value = Integer.parseInt(text); // 정수로 변환
			if (value <= 0) { // 음수 일 때
				JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), // this가 들어 있는 최상위 창을 찾아서 반환
						"1 이상의 숫자를 입력하세요.", "입력 오류", JOptionPane.WARNING_MESSAGE // 경고 메세지
				);
				return -1;
			}
			return value;
		} catch (NumberFormatException e) { // 문자열로 입력 될 때
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "숫자만 입력하세요.", "입력 오류",
					JOptionPane.WARNING_MESSAGE);
			return -1;
		}
	}

	private void onClickBtnSkip() { // 스킵 버튼 클릭 시 실행하는 메소드
		int amount = calcSkipAmount(); // 메소드(158줄)를 사용해서 값을 정수로 가져옴
		if (amount <= 0)
			return;

		// northPanel은 JFrame이 아니므로 skipDaysWithThread() 호출 할수 없기에 부모창을 FinalProjectSelect로 형변환하여
		// 메인화면의 메소드 호출
		Window win = SwingUtilities.getWindowAncestor(this); // 최상위 창이 FinalProjectSelect인지 확인
		
		if (win instanceof FinalProjectSelect) {
			((FinalProjectSelect) win).skipDaysWithThread(amount); // 날짜 스킵 메소드 실행
		}
	}
}

// 중앙 영역
class CenterPanel extends JPanel {

	private StockRowPanel[] stockRows; // 종목 행 배열

	public CenterPanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12)); // 바깥 여백 12으로 함

		JPanel header = new JPanel(new GridLayout(1, 3));
		header.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.GRAY)); // 위, 왼쪽 오른쪽은 1 아래는 0으로 함
		header.add(makeHeaderLabel("종목"));
		header.add(makeHeaderLabel("현재가"));
		header.add(makeHeaderLabel("변동률"));
		add(header, BorderLayout.NORTH);

		String[] names = { "삼성전자", "현대차", "카카오", "네이버", "SK하이닉스" }; // 종목 이름들 배열
		int[] base = { 70000, 180000, 55000, 180000, 120000 }; // 종목의 초기값 배열

		JPanel rowsPanel = new JPanel(new GridLayout(names.length, 1)); // 종목 행들이 들어갈 패널
		rowsPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));

		stockRows = new StockRowPanel[names.length];
		for (int i = 0; i < names.length; i++) {
			stockRows[i] = new StockRowPanel(names[i], base[i]);
			rowsPanel.add(stockRows[i]);
		}

		add(rowsPanel, BorderLayout.CENTER);
	}

	private JLabel makeHeaderLabel(String text) { // 헤더 라벨 꾸미는 메소드
		JLabel la = new JLabel(text, JLabel.CENTER);
		la.setFont(la.getFont().deriveFont(Font.BOLD));
		return la;
	}

	public StockRowPanel[] getRows() { // 외부에서 종목 행 배열을 가져갈 수 있게 하는 메소드
		return stockRows;
	}
}

// 종목 표현하는 패널
class StockRowPanel extends JPanel {

	private JLabel stockNameLabel; // 종목 이름 표시라벨
	private JLabel priceLabel; // 현재가 표시라벨
	private JLabel changeLabel; // 변동률 표시라벨
	private String stockName; // 종목 이름
	private int basePrice; // 초기 값

	public StockRowPanel(String name, int basePrice) {
		setLayout(new GridLayout(1, 3));
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.GRAY));

		this.stockName = name;
		this.basePrice = basePrice;

		GameData.StockState st = GameData.get().getOrCreateStock(name, basePrice); // GameData에서 이 종목의 최신 상태를 가져옴

		ImageIcon icon = loadStockIcon(name);

		stockNameLabel = new JLabel(name, icon, JLabel.LEFT);
		stockNameLabel.setIconTextGap(6); // 아이콘-텍스트 간격
		stockNameLabel.setHorizontalAlignment(JLabel.CENTER); // 가운데 정렬
		add(stockNameLabel);

		priceLabel = new JLabel(String.format("%,d", (int) st.getLastClose()), JLabel.CENTER); // GameDate의 메소드를 이용해서 현재가를 생성																			
		add(priceLabel); // 두 번째 칸

		double change = 0; // 변동률 계산용 변수 0으로 초기화
		if (st.getPrevClose() != 0) { // GameDate의 메소드를 이용해서 이전 종가가 0이 아닐 때만 계산
			change = (st.getLastClose() - st.getPrevClose()) / st.getPrevClose() * 100; // 변동률 : (현재가 - 이전가) / 이전가 × 100
		}
		changeLabel = new JLabel(String.format("%+.2f%%", change), JLabel.CENTER); // 소수 둘쨰 자리까지 표시
		add(changeLabel); // 세 번째 칸

		stockNameLabel.addMouseListener(new MouseAdapter() { // 종목 이름을 클릭했을 때
			public void mouseClicked(MouseEvent e) {
				Window win = SwingUtilities.getWindowAncestor(StockRowPanel.this);// 최상위 창을 찾아옴
				if (win instanceof FinalProjectSelect) { // 메인 화면인지 확인
					FinalProjectSelect frame = (FinalProjectSelect) win; // 메인화면으로 형 변환
					new FinalProjectMain( // FinalProjectMain 생성
							stockName, // 종목 명 전달
							frame.getCurrentDay(), // 현재 게임 날짜 전달
							basePrice, // 기준가 전달
							frame, // 부모 프레임 전달
							StockRowPanel.this // 종목 행 전달
					);
				}
			}
		});
	}
	private ImageIcon loadStockIcon(String stockName) {
	    String fileName;
	    switch (stockName) { // 종목 이름에 따라 이미지 파일 쓰는게 다름
	        case "삼성전자": fileName = "samsung.png"; break;
	        case "현대차":   fileName = "hyundai.png"; break;
	        case "카카오":   fileName = "kakao.png"; break;
	        case "네이버":   fileName = "naver.png"; break;
	        case "SK하이닉스": fileName = "hynix.png"; break;
	        default: return null; // 다른 종목명이 들어오면 null 반환
	    }
	    ImageIcon icon = new ImageIcon("image/" + fileName);
	    Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // 가로 28, 세로 28 크기로 덜 깨진 새 이미지를 만든다
	    return new ImageIcon(img); // 최종 이미지 반환
	}

	public void refreshFromGameData() { // GameDate 기준으로 행의 가격/변동률 다시 계산하는 메소드
		GameData.StockState st = GameData.get().getOrCreateStock(stockName, basePrice); // GameData를 이용해서 최신 상태를 가져옴
		double last = st.getLastClose(); // 최신 종가
		double prev = st.getPrevClose(); // 이전 종가
		double changePct = 0; // 변동률 변수 초기화
		
		if (prev != 0) { // 0 나누기 방지
			changePct = (last - prev) / prev * 100;
		}

		updatePrice((int) last, changePct); // 메소드(316줄)를 사용해서 가격과 변동률을 최신화한다
	}

	public void updatePrice(int newPrice, double changePct) { // 가격과 변동률을 라벨에 반영
		priceLabel.setText(String.format("%,d", newPrice));
		changeLabel.setText(String.format("%+.2f%%", changePct)); // 소수 둘째자리까지
	}
}

// 하단 영역(뉴스)
class SouthPanel extends JPanel {

	private JTextArea newsTextArea; // 뉴스 내용을 여러 줄로 표시할 텍스트 영역

	public SouthPanel() {
		setLayout(new BorderLayout(6, 6));
		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // 여백 15

		JLabel laNews = new JLabel("뉴스");
		laNews.setFont(laNews.getFont().deriveFont(Font.BOLD, 16f));
		add(laNews, BorderLayout.NORTH);

		newsTextArea = new JTextArea();
		newsTextArea.setEditable(false); // 읽기 전용(수정 못함)
		newsTextArea.setLineWrap(true); // 자동 줄 바꿈
		newsTextArea.setFont(new Font("Dialog", Font.PLAIN, 13));

		JScrollPane scrollPane = new JScrollPane( // 스크롤 패널 생성
				newsTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, // 세로 스크롤바는 필요할 때만 표시
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER // 가로 스크롤바는 표시 x
		);

		scrollPane.setPreferredSize(new Dimension(0, 160));
		add(scrollPane, BorderLayout.CENTER); // 중앙에 배치

		refreshNews(); // 아래의 메소드를 이용해서 뉴스 내용 갱신
	}

	public void refreshNews() { // 뉴스 내용을 GameData 기준으로 갱신
		List<GameData.NewsEvent> list = GameData.get().getRecentNews(); // GameData에서 최근 뉴스 목록을 가져옴
		newsTextArea.setText(""); // 뉴스 초기화

		if (list.isEmpty()) { // 초기 값(뉴스가 없을 때)
			newsTextArea.setText("최근 발생한 뉴스가 없습니다.");
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			GameData.NewsEvent ev = list.get(i); // ev에 최근 뉴스를 하나씩 가져옴 

			String stockName = ev.stockName; // 종목명
			String title = ev.title; // 뉴스 제목
			double impact = ev.impact; // 영향도
			int startDay = ev.startDay; // 발생일

			String impactText = String.format("%+.0f%%", impact * 100); // 몇 퍼센트인지

			newsTextArea.append("[" + stockName + "]\n");
			newsTextArea.append(title + "\n");
			newsTextArea.append("영향도 " + impactText);
			newsTextArea.append(" | 발생일 " + startDay + "일차\n\n");
		}

		newsTextArea.setCaretPosition(0); // 뉴스를 갱신한 뒤 맨 위부터 보이게함
	}
}