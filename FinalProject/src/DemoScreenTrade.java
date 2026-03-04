import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Map;
import java.util.Iterator;

public class DemoScreenTrade extends JFrame {

    private JLabel lbTotal, lbCash, lbProfit, lbProfitRate; // 각각 총 평가자산 / 현금 / 실현손익 / 수익률
    private JTable table; // 보유 종목 테이블
    private Holding[] holdings; // 보유 종목 배열(화면 표시용)
    private int cash = 0;

    static class Holding {
        String name; // 종목명
        int quantity; // 수량
        int avgPrice; // 평균단가
        int nowPrice; // 현재가

        Holding(String name, int quantity, int avgPrice, int nowPrice) { // 4개의 정보를 받아서 필드에 저장함 
            this.name = name;
            this.quantity = quantity;
            this.avgPrice = avgPrice;
            this.nowPrice = nowPrice;
        }
    }

    // 생성자
    public DemoScreenTrade() {
        setTitle("내 자산보기");  // 화면 제목
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 메인 안꺼지게 변경
        setSize(750, 520);
        setLocationRelativeTo(null);  // 화면 가운데 정렬
        
        // GameData에서 현금/보유종목을 가져오는 메소드(58줄)
        initFromGameData();

        Container c = getContentPane();
        c.setLayout(new BorderLayout(10, 10));
        c.setBackground(new Color(245, 247, 250));
        ((JComponent)c).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // 여백 15

        // 제목 표시(85줄)
        c.add(makeTitlePanel(), BorderLayout.NORTH);

        // 가운데 영역 (자산요약 + 보유종목 테이블)
        JPanel center = new JPanel(new BorderLayout(10, 12));
        center.setOpaque(false);
        center.add(makeAssetPanel(), BorderLayout.NORTH);
        center.add(makeHoldingsPanel(), BorderLayout.CENTER);
        c.add(center, BorderLayout.CENTER);

        // holdings 기반으로 합계 계산해서 실현손익, 수익률 라벨에 표시하는 메소드(244줄)
        refreshTotals();
    }

    // GameData → cash, holdings 배열로 변환
    private void initFromGameData() {
        GameData gd = GameData.get(); // 싱글턴 GameData 가져옴
        cash = gd.getCapital();  // 현재 현금을 cash에 저장

        // 보유 종목은 순서보다 종목 이름으로 찾는게 유리해서 list 대신 Map 사용
        Map<String, GameData.Holding> map = gd.getHoldings(); // 보유종목 Map 가져옴 (key:종목이름 , value:종목의 보유 정보)
        holdings = new Holding[map.size()]; // 화면 표시용 배열 크기를 보유 종목 개수에 맞춤

        int idx = 0;
          
        Iterator<GameData.Holding> it = map.values().iterator(); // Map 안에 들어있는 값들을 하나씩 꺼내기위한 Iterator 생성(Map에선 인덱스 개념 X ->for문 X)
        while (it.hasNext()) { // 다음 값이 없으면 종료
            GameData.Holding gh = it.next(); // Holding 하나 꺼내서 gh에 저장
            // gh 객체에서 값을 하나씩 읽음
            String name = gh.getName();
            int qty = gh.getQuantity();
            int avgPrice = gh.getAvgPrice();

            GameData.StockState state = gd.getOrCreateStock(name, avgPrice); // 해당 종목의 상태 객체를 가져옴
            int nowPrice = (int) state.getLastClose(); // 마지막 종가를 현재가로 사용

            // 배열로 변환
            holdings[idx] = new Holding(name, qty, avgPrice, nowPrice); // 배열의 idx 자리에 새 Holding 저장
            idx = idx + 1; // idx 증가
        }
    }

    // 화면 최상단 제목 부분
    private JPanel makeTitlePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false); 

        JLabel title = new JLabel("내 자산보기"); // 큰 제목 라벨
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setForeground(new Color(33, 37, 41));

        JLabel sub = new JLabel("보유 자산 요약 및 종목 현황"); // 작은 설명 라벨
        sub.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        sub.setForeground(new Color(120, 130, 140));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setOpaque(false);
        left.add(title);
        left.add(sub);

        p.add(left, BorderLayout.WEST); // 왼쪽
        return p;
    }

    // 자산 요약을 보여주는 패널
    private JPanel makeAssetPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JPanel p = new JPanel(new GridLayout(2, 2, 12, 8));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 233, 240)),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));

        // 중복 제거, 스타일 통일을 위한 라벨 만드는 메소드(135줄) 사용
        lbTotal    = makeValueLabel();
        lbCash     = makeValueLabel();
        lbProfit = makeValueLabel();
        lbProfitRate   = makeValueLabel();

        // 중복 제거, 통일성을 위한 UI을 위한 메소드(143줄)
        p.add(makeStatCell("총 평가자산", lbTotal));
        p.add(makeStatCell("현금",       lbCash));
        p.add(makeStatCell("실현손익",   lbProfit));
        p.add(makeStatCell("수익률",     lbProfitRate));

        wrapper.add(p, BorderLayout.CENTER);
        return wrapper;
    }

    // 값 표시 라벨을 일괄로 설정하기 위한 메소드
    private JLabel makeValueLabel() {
        JLabel l = new JLabel("-", JLabel.RIGHT); // 오른쪽 정렬
        l.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        l.setForeground(new Color(33, 37, 41));
        return l;
    }

    // "항목 제목 + 값" 하나의 칸으로 만드는 메소드
    private JPanel makeStatCell(String title, JLabel valueLabel) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setOpaque(false);
        JLabel t = new JLabel(title); // 항목명 라벨
        t.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        t.setForeground(new Color(90, 98, 110));

        cell.add(t, BorderLayout.WEST); // 항목명은 왼쪽
        cell.add(valueLabel, BorderLayout.EAST); // 값은 오른쪽
        return cell;
    }

    // 보유종목 테이블을 만드는 영역
    private JPanel makeHoldingsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);

        JLabel title = new JLabel("내 보유종목보기");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        title.setForeground(new Color(52, 58, 64));
        p.add(title, BorderLayout.NORTH);

        // 테이블에 들어갈 데이터 생성
        String[] cols = {"종목", "수량", "평균단가", "현재가", "평가금액", "손익", "수익률"};
        Object[][] rowData = makeTableData(); // 메소드(214줄)를 이용해서 Holding(20줄) 기반으로 테이블 행 데이터 생성

        table = new JTable(rowData, cols);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table); // 스크롤 가능하게
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 233, 240))); // 스크롤 영역 테두리
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    // 테이블 스타일
    private void styleTable(JTable table) {
        table.setRowHeight(26); // 행 높이
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        table.setGridColor(new Color(235, 238, 245)); // 격자선 색
        table.setSelectionBackground(new Color(222, 235, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setFillsViewportHeight(true); // 빈 공간 덜 어색하게 함

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 28)); // 높이 28
        header.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(90, 98, 110)); // 글자색

        // 렌더러 객체 생성
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT); // 오른쪽 정렬

        // 내장 메소드를 사용해서 컬럼 단위로 셀 렌더링 방식을 지정함
        for (int i = 0; i < table.getColumnCount(); i++) { // 테이블의 모든 컬럼을 하나씩
            String colName = table.getColumnName(i); // i번쨰 컬럼의 헤더 이름
            TableColumn col = table.getColumnModel().getColumn(i); // i번째 컬럼의 실제 컬럼 객체
            if (colName.equals("종목")) { // 현재 컬럼이 종목이면 center 렌더러방식으로
                col.setCellRenderer(center); 
            } else { // 종목이 아니면 right 렌더러 방식으로
                col.setCellRenderer(right);
            }
        }
    }

    // holdings 배열을 토대로 테이블에 넣을 실제 데이터를 만드는 메소드
    private Object[][] makeTableData() { 
        Object[][] data = new Object[holdings.length][7]; 

        for (int i = 0; i < holdings.length; i++) {
            Holding h = holdings[i]; // 현재 종목

            int eval = (int) h.quantity * h.nowPrice;  // 평가금액(수량 x 현재가)
            int cost = (int) h.quantity * h.avgPrice; // 매입금액(수량 x 평균단가)
            int pnl  = eval - cost; // 손익(평가금액-매입금액)
            
            int rate; // 수익률
            if (cost == 0) { // 매입금액이 0이면 수익률 0
                rate = 0;
            } else {
                rate = (int) pnl / cost * 100; // 수익률(손익/매입금액 x 100)
            }

            data[i][0] = h.name; // 종목명
            data[i][1] = h.quantity; // 수량
            // 금액 표시용 formatMoney() 메소드(312줄) 씀
            data[i][2] = formatMoney(h.avgPrice); // 평균단가
            data[i][3] = formatMoney(h.nowPrice); // 현재가
            data[i][4] = formatMoney(eval); // 평가금액 
            data[i][5] = formatMoney(pnl); // 손익
            // 퍼센트 형식으로 바꾸는 formatPercent() 메소드(317줄) 사용
            data[i][6] = formatPercent(rate); // 수익률
        }
        return data;
    }

    // 전체 합계를 계산해서 상단 라벨에 표시
    private void refreshTotals() {
        long evalSum = 0; // 평기 금액 합
        long pnlSum  = 0; // 손익 합

        // 각 종목의 평가금액과 손익을 더함
        for (int i = 0; i < holdings.length; i++) { // 각 보유 종목 돌면서
            Holding h = holdings[i]; 

            int eval = (int) h.quantity * h.nowPrice; // 평가 금액 계산
            int cost = (int) h.quantity * h.avgPrice; // 매입 금액 계산
            int pnl  = eval - cost; // 손익 계산
            // 합계 누적
            evalSum += eval; 
            pnlSum += pnl;
        }

        // 총 자산
        long totalAsset = cash + evalSum; // 현금 + 평가금액

        // 전체 수익률 계산 
        double rate;
        if (evalSum == 0) { // 평가 금액 0이면 손익 0
            rate = 0;
        } else {
            rate = (double) pnlSum / evalSum * 100; // 비율(손익/평가금액)  
        }

        // 금액 표시용 formatMoney() 메소드 씀
        lbTotal.setText(formatMoney(totalAsset) + " 원"); // 총 자산 라벨 표시
        lbCash.setText(formatMoney(cash) + " 원"); // 현금 라벨 표시

        // 플러스면 + 붙여서 가독성 있게 함
        String pnlStr; String rateStr;

        if (pnlSum >= 0) { // 손익이 0 이상일 떄(이득)
            pnlStr = "+" + formatMoney(pnlSum) + " 원"; // + 표시랑 formatMoney() 메소드 사용 
        } else {
            pnlStr = formatMoney(pnlSum) + " 원";
        }
       
        if (rate >= 0) { // 수익률이 0 이상일 때(이득)
            rateStr = "+" + formatPercent(rate); // + 표시랑 formatMoney() 메소드 사용 
        } else {
            rateStr = formatPercent(rate);
        }

        // 두 라벨의 문자열 세팅
        lbProfit.setText(pnlStr);
        lbProfitRate.setText(rateStr);

        // 플러스면 빨간색, 마이너스면 파란색
        Color upColor = new Color(200, 0, 0);
        Color downColor = new Color(0, 70, 200);

        if (pnlSum >= 0) { // 손익 색 처리
            lbProfit.setForeground(upColor);
        } else {
            lbProfit.setForeground(downColor);
        }
        if (rate >= 0) { // 수익률 색 처리
            lbProfitRate.setForeground(upColor);
        } else {
            lbProfitRate.setForeground(downColor);
        }


    }

    // 금액 표시를 위해 3자리마다 콤마 넣기
    private String formatMoney(long value) {
        return String.format("%,d", value);
    }

    // 퍼센트 형식으로 변환
    private String formatPercent(double value) {
        return String.format("%.2f%%", value);
    }

    public static void main(String[] args) {
        DemoScreenTrade frame = new DemoScreenTrade();
        frame.setVisible(true);
    }
}
