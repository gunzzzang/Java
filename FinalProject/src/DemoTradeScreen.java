import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class DemoTradeScreen extends JPanel {

	private JTable table; // 거래내역을 보여줄 표 형식
	private JTextField tfSearch; // 종목 검색 입력칸
	private Object[][] allData; // 거래내역을 저장할 2차원 배열
	private String[] colNames = { // 테이블에 쓸 컬럼 이름들
			"일시", "종목", "수량", "단가", "수수료", "체결금액", "실현손익" };

	public DemoTradeScreen() {
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // 여백 15
		setBackground(new Color(245, 247, 250));

		// 상단 영역(제목 + 검색)
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(new Color(250, 250, 252));
		topPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 233, 240)),
				BorderFactory.createEmptyBorder(10, 12, 10, 12)));
		add(topPanel, BorderLayout.NORTH); // 상단영역에 topPanel

		// 제목 라벨
		JLabel titleLabel = new JLabel("거래내역보기");
		titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
		titleLabel.setForeground(new Color(33, 37, 41));
		topPanel.add(titleLabel, BorderLayout.WEST); // topPanel의 왼쪽에 붙임

		// 검색 관련 패널
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2)); // 가로 6 세로 2
		filterPanel.setOpaque(false);

		JLabel lbSearch = new JLabel("종목");
		lbSearch.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

		tfSearch = new JTextField(12); // 12칸의 텍스트 필드
		tfSearch.setPreferredSize(new Dimension(130, 28));
		tfSearch.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		tfSearch.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(210, 215, 225)),
				BorderFactory.createEmptyBorder(2, 5, 2, 5)));

		JButton btnSearch = new JButton("검색");
		JButton btnReset = new JButton("초기화");

		Font btnFont = new Font("맑은 고딕", Font.PLAIN, 13);
		JButton[] btns = { btnSearch, btnReset }; // 2개의 버튼 객체를 배열에 담음(중복 코드 제거)
		for (int i = 0; i < btns.length; i++) {
			JButton b = btns[i];
			b.setFont(btnFont);
			b.setFocusPainted(false);
			b.setMargin(new Insets(5, 10, 5, 10));
		}

		filterPanel.add(lbSearch); // 종목 라벨
		filterPanel.add(tfSearch); // 검색 텍스트 필드
		filterPanel.add(btnSearch); // 검색 버튼
		filterPanel.add(btnReset); // 초기화 버튼

		topPanel.add(filterPanel, BorderLayout.EAST); // topPanel의 오른쪽에 붙임

		// GameData에서 거래내역을 가져와 배열로 변환하는 메소드(86줄) 사용
		initDataFromGameData();

		// 거래내역데이터(allData)를 표 형태로 화면에 표시할 객체를 만듬
		table = new JTable(allData, colNames);
		styleTable(table); // 테이블의 폰트 통일용 메소드(117줄) 사용

		JScrollPane scrollPane = new JScrollPane(table); // 스크롤 되게 감싸줌
		scrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(230, 233, 240)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		add(scrollPane, BorderLayout.CENTER); // 센터 영역에 배치

		tfSearch.addActionListener(e -> filterTable()); // 검색칸에서 Enter 치면 filterTable() 메소드(144줄) 사용
		btnSearch.addActionListener(e -> filterTable()); // 검색칸에서 Enter 치면 filterTable() 메소드(144줄) 사용
		btnReset.addActionListener(e -> {
			tfSearch.setText(""); // 검색 입력칸을 비움
			table.setModel(new DefaultTableModel(allData, colNames)); // Swing의 기본 구현체를 이용해서 처음 화면 상태로 되돌림
			styleTable(table); // 테이블의 폰트 통일용 메소드 사용
		});
	}

	// GameData의 거래내역 리스트를 데이터로 바꾸는 메소드
	private void initDataFromGameData() {
		List<GameData.TradeRecord> list = GameData.get().getTrades(); // 싱글턴 GameData에서 거래내역 리스트 가져옴.
		allData = new Object[list.size()][colNames.length]; // 행과 컬럼 생성

		for (int i = 0; i < list.size(); i++) { // 거래 기록을 하나씩 돌음
			GameData.TradeRecord t = list.get(i); // GameData의 TradeRecord에서 가져옴

			// 하나의 거래 정보를 가져옴
			allData[i][0] = t.getDateTime();
			allData[i][1] = t.getStockName();
			allData[i][2] = t.getQuantity();
			allData[i][3] = t.getPrice();
			allData[i][4] = t.getFee();
			allData[i][5] = t.getAmount();

			Integer pnl = t.getTradeProfit(); // 실현손익(없을 수 도 있어서 정수형 말고 Integer)
			if (pnl == null) { // 실현손익이 없으면 - 표시
				allData[i][6] = "-";
			} else {
				String sign;
				if (pnl >= 0) // 손익이 0 이상이면 + 붙이기
					sign = "+";
				else
					sign = "";

				allData[i][6] = sign + String.format("%,d", pnl); // 3자리마다 콤마 (ex 15,000)
			}
		}
	}

	// 테이블 공통 스타일 설정
	private void styleTable(JTable table) {
		table.setRowHeight(26);
		table.setFillsViewportHeight(true);
		table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		table.setGridColor(new Color(235, 238, 245));
		table.setSelectionBackground(new Color(222, 235, 255));
		table.setSelectionForeground(Color.BLACK);

		JTableHeader header = table.getTableHeader(); // 컬럼 이름 줄
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 28));
		header.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		header.setBackground(new Color(245, 247, 250));
		header.setForeground(new Color(90, 98, 110));

		// 렌더러 객체 생성
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
	
		// 내장 메소드를 사용해서 컬럼 단위로 셀 렌더링 방식을 지정함
		for (int i = 0; i < table.getColumnCount(); i++) { // 테이블의 모든 컬럼을 하나씩
		    TableColumn column = table.getColumnModel().getColumn(i); // i번째 컬럼의 실제 컬럼 객체
		    column.setCellRenderer(centerRenderer); // 모든 컬럼을 가운데 정렬
		}

	}

	// 종목명으로 검색하는 메소드
	private void filterTable() {
		String keyword = tfSearch.getText().trim().toLowerCase(); // 공백 제거 후 대문자 고려해서 소문자로 통일해서 비교
		if (keyword.isEmpty()) {
			// 검색어가 비어 있으면 원본 전체 데이터 보여줌(JTable은 TableModel를 붙여서 동작시켜야함)
			table.setModel(new DefaultTableModel(allData, colNames)); // 테이블 데이터를 통째로 교체해야 할 때 DefaultTableModel처럼 Swing 제공하는 모델을 씀
			styleTable(table);
			return;
		}

		int count = 0; // 검색 조건에 걸리는 행 개수
		for (int i = 0; i < allData.length; i++) { // 모든 행 검사
			Object[] row = allData[i]; // i번째 거래 행을 row에 저장

			String symbol = row[1].toString().toLowerCase(); // 거래행에서 종목을 꺼내서 문자열, 소문자로 변환
			if (symbol.contains(keyword)) { // contains() 메소드 사용해서 종목이 검색어를 포함하는지 확인
				count = count + 1; // 포함하면 1 증가
			}
		}

		// 걸린 개수만큼 새 배열 만들어서 복사
		Object[][] filtered = new Object[count][colNames.length]; // 검색해서 있는 행만 담을 배열 생성
		int idx = 0; // 인덱스

		for (int i = 0; i < allData.length; i++) { // 모든 행 검사
			Object[] row = allData[i]; // i번째 거래 행을 row에 저장

			String symbol = row[1].toString().toLowerCase(); // 거래행에서 종목을 꺼내서 문자열, 소문자로 변환

			if (symbol.contains(keyword)) {// contains() 메소드 사용해서 종목이 검색어를 포함하는지 확인

				for (int j = 0; j < colNames.length; j++) { // 컬럼 반복 시작
					filtered[idx][j] = row[j]; // 검색에 걸린 거래의 한 칸 값을 결과 배열로 복사한다.
				}
				idx = idx + 1; // 인덱스 증가
			}
		}
		// 테이블 데이터를 검색 결과로 교체
		table.setModel(new DefaultTableModel(filtered, colNames));
		styleTable(table); // 스타일 다시 적용
	}
}
	