import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FinalProjectMain extends JFrame {

   // 상단 컴포넌트
   private JTextField TfStockName; // 종목 이름 텍스트 필드
   private JTextField TfSkip; // 날짜 스킵 입력 텍스트 필드
   private JLabel LaPrice; // 가격 + 변동률 라벨
   private JLabel LaDayCount; // 현재 날짜 라벨
   private JLabel LaCandleType; // 일봉 표시 라벨
   private ChartPanel chartPanel; // 중앙 차트 패널

   // 상세 화면에서 가격이 바뀌었는데 선택 화면은 그대로 옛날 가격인 문제를 해결하기 위해 부모 화면이 누구인지 기억하는 변수
   private FinalProjectSelect selectFrame; // 종목 선택 화면을 가리키는 참조.
   private StockRowPanel rowPanel; // 종목 선택 화면에서 종목 행 참조 (ex, [삼성전자] 70,000 +1.2%)

   private int dayCount; // 메인 화면 기준 날짜 값
   private String stockName; // 종목명
   private int basePrice; // 초기 가격

   // 모든 가격/변동 정보를 담는 객체
   private GameData.StockState stockState;

   // FinalProjectSelect에서 넘어올 떄 호출 되는 생성자, 정보를 모두 전달 받음
   public FinalProjectMain(String stockName, int startDay, int basePrice, FinalProjectSelect selectFrame,
         StockRowPanel rowPanel) {
      this.stockName = stockName; // 종목 이름
      this.dayCount = startDay; // FinalProjectMain에서 사용할 시작 날짜 저장
      this.basePrice = basePrice; // 초기값 저장
      this.selectFrame = selectFrame; // 종목 선택 화면 참조 저장
      this.rowPanel = rowPanel; // 종목 행 참조 저장

      GameData.get().setDay(startDay); // GameData의 메소드를 이용해서 날짜를 상세화면 날짜로 맞춤(선택화면과 상세화면 날짜를 동기화하기 위해)
      this.stockState = GameData.get().getOrCreateStock(stockName, basePrice); // GameData의 메소드를 이용해서 종목의 상태 객체를 가져옴

      setTitle("주식 화면 - " + stockName); // 창 이름에 종목 이름 표시
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창 닫아도 프로그램 실행
      setSize(700, 500);
      setResizable(false); // 창 크기 변경 X

      Container c = getContentPane();
      c.setLayout(new BorderLayout());

      c.add(new NorthPanel(), BorderLayout.NORTH);
      chartPanel = new ChartPanel(stockState);
      c.add(chartPanel, BorderLayout.CENTER);
      c.add(new SouthPanel(), BorderLayout.SOUTH);

      updatePriceLabel(); // 처음 화면을 열때 가격을 세팅해야 하므로 최초로 1회 갱신(56줄)
      setVisible(true);
   }

   // 🔹 가격 라벨 갱신
   private void updatePriceLabel() {
      double last = stockState.getLastClose(); // GameData의 메소드를 이용해서 현재 종가 가져옴
      double prev = stockState.getPrevClose(); // GameData의 메소드를 이용해서 이전 종가 가져옴
      double changePct = (last - prev) / prev * 100.0; // 변동률 계산

      String text = String.format("%,d원 (%.2f%%)", (int) last, changePct); // 가격 + 변동률을 포멧을 이용해서 문자열로 나타냄
      LaPrice.setText(text);
      rowPanel.updatePrice((int) last, changePct); // 상세화면에서 가격 계산해서 선택화면의 해당 줄 갱신 / 없으면 같은 종목인데 가격이 다른 현상이 생김
   }

   // 상단 영역
   class NorthPanel extends JPanel {
      public NorthPanel() {
         setLayout(null);
         setPreferredSize(new Dimension(700, 105));

         JLabel LaTop = new JLabel("종목 클릭 시 차트 표시"); // 안내 문구 라벨
         LaTop.setBounds(10, 5, 200, 20);
         add(LaTop);

         JLabel LaName = new JLabel("종목명 :"); // 종목명 라벨
         LaName.setBounds(10, 30, 60, 25);
         add(LaName);

         TfStockName = new JTextField(stockName); // 선택된 종목 이름을 보여줘는 텍스트필드
         TfStockName.setBounds(65, 30, 120, 25);
         TfStockName.setEditable(false); // 사용자가 수정 X
         add(TfStockName);

         TfSkip = new JTextField(); // 스킵할 날짜 수 입력필드
         TfSkip.setBounds(200, 30, 50, 25);
         add(TfSkip);

         JLabel LaSkip = new JLabel("일 스킵"); // 일 스킵 라벨
         LaSkip.setBounds(255, 30, 50, 25);
         add(LaSkip);

         LaPrice = new JLabel("가격 로딩 중..."); // 초기엔 계산 안 됐으므로 안내 문구 라벨
         LaPrice.setBounds(10, 60, 250, 25);
         add(LaPrice);

         LaCandleType = new JLabel("차트 단위: 일봉"); // 차트는 일봉 고정
         LaCandleType.setBounds(300, 60, 150, 25);
         add(LaCandleType);

         JButton btnSkip = new JButton("스킵 ▶"); // 날짜 스킵 버튼
         btnSkip.setBounds(460, 30, 120, 28);
         add(btnSkip);

         LaDayCount = new JLabel("현재 " + dayCount + "일째"); // 현재 게임 날짜 표시
         LaDayCount.setBounds(460, 65, 150, 25);
         add(LaDayCount);

         btnSkip.addActionListener(e -> { // 스킵 버튼을 눌렀을 때
            int amount = calcSkipAmount(); // 메소드(138줄)를 이용해서 정수로 계산
            if (amount <= 0)
               return; // 음수면 종료

            GameData.get().skipDay(amount); // GameData의 메소드를 이용해서 amount 만큼 게임 날짜 증가
            dayCount = GameData.get().getDay(); // 저장
            stockState.ensureUpToDay(dayCount); // GameData의 메소드를 이용해서 차트가 끊기지 않게 한다

            if (GameData.get().getDay() >= 365) { // 최대 날짜 : 365일
               JOptionPane.showMessageDialog(FinalProjectMain.this, // 안내 메세지를 메인창에서 나오게 한다
                     "365일이 종료되었습니다.\n결과 화면으로 이동합니다.", // 메세지 내용
                     "게임 종료", // 제목
                     JOptionPane.INFORMATION_MESSAGE);
               new ResultScreen(); // 결과 화면 창 생성
               dispose(); // 현재 화면 닫음
               return;
            }
            LaDayCount.setText("현재 " + dayCount + "일째"); // 날짜 라벨 갱신

            if (selectFrame != null) { // 종목 선택 화면이 존재할때만
               selectFrame.refreshView(); // 선택 화면 갱신
            }

            chartPanel.repaint(); // 차트 갱신
            updatePriceLabel(); // 가격/변동률 갱신(56줄)
         });
      }

      private int calcSkipAmount() {
         String text = TfSkip.getText().trim(); // 스킵일수를 공백을 제거한 문자열로 가져옴
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
         } catch (NumberFormatException e) { // 예외처리: 문자열로 입력 될 때
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "숫자만 입력하세요.", "입력 오류",
                  JOptionPane.WARNING_MESSAGE);
            return -1;
         }
      }
   }

   // 하단 패널
   class SouthPanel extends JPanel {
      public SouthPanel() {
         setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));

         JButton btnBuy = new JButton("매수하기");
         JButton btnSell = new JButton("매도하기");

         add(btnBuy);
         add(btnSell);

         btnBuy.addActionListener(e -> new BuyWindow(stockName).setVisible(true)); // 매수 창 띄우기
         btnSell.addActionListener(e -> new SellWindow(stockName).setVisible(true)); // 매도 창 띄우기
      }
   }
   // 그래프 그리는 패널
   class ChartPanel extends JPanel {

      private static final int VISIBLE_CANDLE_COUNT = 15; // 화면에 최근 15개의 캔들을 보여줌
      private static final int GRID_LINE_COUNT = 5; // 가격을 나누는 가로 기준선 5개

      private static final int GAP = 30; // 캔들끼리 x축 간격
      private static final int CANDLE_WIDTH = 15; // 캔들 몸통을 가로 15로 함
      private static final int CANDLE_MIDDLE_X = 7; // 캔들 꼬리를 가운데쯤에 그리기 위한 15/2=7.5 대략 7으로 놓음 

      private static final int TOP_MARGIN = 30; // 차트 위에 30의 여백
      private static final int BOTTOM_MARGIN = 30; // 차트 아래에 30의 여백

      private static final int LEFT_PADDING = 40; // 좌우 40의 여백
      private static final int  PRICE_TEXT_X = 100; // 차트 오른쪽에 가격를 안전하게 표시하기 위한 거리

      private static final int MIN_BODY_HEIGHT = 2; // 캔들 몸통 최소 높이를 2로 설정

      private static final int GRID_FONT_SIZE = 12; // 텍스트 폰트 크기
      private static final double PADDING_RATIO = 0.1; // 가격범위의 10%만큼 여유 -> 차트가 답답하지 않게 함
      private static final double MIN_PADDING = 1.0; // 가격 변동이 없을때도 1만큼 여유

      private static final Color GRID_COLOR = Color.LIGHT_GRAY;

      // 필드
      private GameData.StockState stockState; // 차트가 참조할 캔들 목록을 가진 객체
      private int startX; // 캔들을 그릴 때의 첫 x좌표
      private double tickMin, tickMax; // 가격의 최대/최소

      public ChartPanel(GameData.StockState stockState) {
         this.stockState = stockState;
         setBackground(Color.WHITE);
      }

      protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         
         // GamaData의 메소드를 이용해서 최근 캔들 15개를 가져와 candles 리스트에 저장
         java.util.List<GameData.StockState.Candle> candles = stockState.getRecentCandles(VISIBLE_CANDLE_COUNT);
         if (candles.isEmpty())
            return;

         calculateStartX(candles); // 캔들이 화면 가운데에 오도록 startX를 계산하는 메소드
         calculatePriceRange(candles); // 캔들의 고가/저가를 보고 가격 범위 계산하는 메소드
         drawGrid(g); // 격자선 그리는 메소드
         drawCandles(g, candles); // 캔들의 몸통+꼬리를 그리는 메소드
      }

      // x 시작 위치 계산
      private void calculateStartX(java.util.List<GameData.StockState.Candle> candles) {
         int w = getWidth(); // 현재 폭을 얻음(내장되어 있는 메소드)
         int totalWidth = (candles.size() - 1) * GAP; // 캔들을 화면 가운데에 정확히 배치하기 위한 totalWidth (간격 * Gap)
         startX = (w - totalWidth) / 2; // 가운데 시작점 :(전체 폭 - 필요한 폭 ) /2
      }

      // 가격 범위 계산
      private void calculatePriceRange(java.util.List<GameData.StockState.Candle> candles) {
         // 처음에 극단 값으로 세팅해서 첫 번쨰에 무조건 갱신 되게함
         double dataMin = Double.MAX_VALUE;
         double dataMax = Double.MIN_VALUE;

         for (int i = 0; i < candles.size(); i++) { //  low, high, get()는 GameData의 필드
             GameData.StockState.Candle c = candles.get(i); // i번째 캔들을 하나 꺼내서
             if (c.low < dataMin) { // 기존 최저가보다 이 캔들의 저가가 더 낮으면 최저가 갱신
                 dataMin = c.low;
             }
             if (c.high > dataMax) { // 기존 최고가보다 이 캔들의 고가가 더 높으면 최고가 갱신
                 dataMax = c.high;
             }
         }

         // 여유 = (최고가 - 최저가) x 비율 여유가 1보다 작을 경우를 고려해 1과 여유값중 더 큰 값을 여유값으로 함
         double padding = Math.max(MIN_PADDING, (dataMax - dataMin) * PADDING_RATIO); 
         tickMin = dataMin - padding; // 최소 가격을 더 낮춰서 여유롭게
         tickMax = dataMax + padding; // 최대 가격을 더 높여서 여유롭게
      }

      // 격자선 그리기
      private void drawGrid(Graphics g) {
         int w = getWidth(); // 현재 폭
         int h = getHeight(); // 현재 높이

         int usableHeight = h - TOP_MARGIN - BOTTOM_MARGIN; // 실제 차트를 그릴 수 있는 높이는 위 아래 여백을 제외한 값

         double range = tickMax - tickMin; // 가격의 범위
         double step = range / (GRID_LINE_COUNT - 1); // 각 줄 사이의 가격 차이 계산

         g.setFont(new Font("Dialog", Font.PLAIN, GRID_FONT_SIZE));
         g.setColor(GRID_COLOR);

         for (int i = 0; i < GRID_LINE_COUNT; i++) { // 격자선 5줄을 그림
            double value = tickMax - step * i; // i가 0일 때 가장 윗줄 가격이고 i가 클수록 가격값이 줄어든다
            int y = TOP_MARGIN + (int) ((tickMax - value) / range * usableHeight); // 가격 value를 화면 y좌표로 변환

            g.drawLine(LEFT_PADDING, y, w - LEFT_PADDING, y); // 좌우 40 띄어서 y위치에 가로선 그림
            g.setColor(Color.BLACK);
            g.drawString(String.format("%,d", (int) value), w -  PRICE_TEXT_X, y + 4); // 오른쪽에 가격 숫자 출력
            g.setColor(GRID_COLOR);
         }
      }

      // 캔들 그리기
      private void drawCandles(Graphics g, java.util.List<GameData.StockState.Candle> candles) {
         int h = getHeight(); 
         int usableHeight = h - TOP_MARGIN - BOTTOM_MARGIN; 
         double range = tickMax - tickMin; // 가격의 범위 

         for (int i = 0; i < candles.size(); i++) {
            GameData.StockState.Candle c = candles.get(i); // i번째 캔들을 꺼냄
            int x = startX + i * GAP; // 캔들의 x좌표, i가 커질수록 오른쪽으로 Gap 간격으로 이동

            if (c.close < c.open) { // 처음 거래된 가격이 마지막에 거래된 가격보다 크다 -> 하락(파란색)
                g.setColor(Color.BLUE);
            } else { // 캔들이 상승이면 빨간색
                g.setColor(Color.RED);
            }

            int yHigh = toY(c.high, range, usableHeight); // 메소드(305줄)를 이용해서 고가를 y좌료로 변환
            int yLow = toY(c.low, range, usableHeight); // 저가를 y좌표로 변환
            int yOpen = toY(c.open, range, usableHeight); // 시가를 y좌표로 변환
            int yClose = toY(c.close, range, usableHeight); // 종가를 y좌표로 변횐

            g.drawLine(x + CANDLE_MIDDLE_X, yHigh, x + CANDLE_MIDDLE_X, yLow); // 고가부터 저가까지 세로 선 그림
            g.fillRect(x, // 몸통 왼쪽 시작 위치
                  Math.min(yOpen, yClose), // 작은 y가 몸통의 시작점
                  CANDLE_WIDTH, // 몸통 너비
                  Math.max(MIN_BODY_HEIGHT, Math.abs(yOpen - yClose))); // 최소 높이와 시가~종가 차이중 높은게 몸통 높이
         }
      }
        // 가격을 화면의 y좌표로 바꿔주는 메소드(공식)
      private int toY(double price, double range, int usableHeight) {
         return TOP_MARGIN + (int) ((tickMax - price) / range * usableHeight);
      }
   }
}
