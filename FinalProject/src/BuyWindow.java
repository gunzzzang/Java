import javax.swing.*;
import java.awt.*;
import javax.swing.event.DocumentListener; // 텍스트 입력이 바뀔 때 발생하는 이벤트 처리를 위함

public class BuyWindow extends JFrame {

    private String stockName;        // 매수할 종목
    private int tradePrice;          // 현재가(시장가)
    private JLabel holdingValue;     // 보유 수량/평단가

    public BuyWindow(String stockName) {
        super(stockName + " 매수 창");
        this.stockName = stockName;

        // 현재가 가져오기
        GameData gd = GameData.get();  // 싱글턴 GameData 객체를 가져옴
        if (stockName != null) {
            GameData.StockState state = gd.getOrCreateStock(stockName, 10000); // 해당 종목의 StockState를 가져옴
            tradePrice = (int) state.getLastClose();  // 마지막 종가를 가져와서 저장
        } 
        else {
            tradePrice = 10000; // 기본 값 10000
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창 닫아도 프로그램 실행
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color bgColor = new Color(246, 249, 252);
        getContentPane().setBackground(bgColor);

        BuyCenterPanel centerPanel = new BuyCenterPanel(stockName, tradePrice, gd); // 중앙에 보유정보/입력/버튼 패널
        add(new BuyNorthPanel(), BorderLayout.NORTH);// 위쪽은 제목 패널
        add(centerPanel, BorderLayout.CENTER); // 중앙 패널
        add(new BuySouthPanel(stockName, tradePrice, centerPanel), BorderLayout.SOUTH); // 아래쪽은 매수하기 버튼 패널
    }

    // 상단 패널
    class BuyNorthPanel extends JPanel {
        BuyNorthPanel() {
            setBackground(new Color(246, 249, 252));
            setLayout(new BorderLayout());

            JLabel titleLabel = new JLabel("매수 창 (시장가)", SwingConstants.CENTER); // 텍스트를 가운데에 정렬
            titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24)); 
            titleLabel.setForeground(new Color(44, 62, 80)); // 짙은 남색
            titleLabel.setBorder( BorderFactory.createEmptyBorder(15, 0, 10, 0));
            add(titleLabel, BorderLayout.CENTER);
        }
    }

    // 중앙 패널
    class BuyCenterPanel extends JPanel {
        JTextField quantityField; // 수량 입력칸
        JLabel totalValueLabel; // 계산한 체결금액
        JLabel capitalLabel; // 보유 현금

        private int tradePrice; // 가격
        private GameData gd;

        BuyCenterPanel(String stockName, int tradePrice, GameData gd) { // 생성자 : 종목명, 가격, GameData 정보를 받음
        	    // 필드에 저장
            this.tradePrice = tradePrice;
            this.gd = gd;

            Color bgColor = new Color(246, 249, 252); 
            Color cardColor = Color.WHITE;
            Color borderColor = new Color(223, 228, 234);

            setLayout(new GridBagLayout());
            setBackground(bgColor);

            JPanel cardPanel = new JPanel(); 
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
            cardPanel.setBackground(cardColor);
            cardPanel.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(borderColor),
                            BorderFactory.createEmptyBorder(20, 30, 20, 30)
                    )
            );
            add(cardPanel);

            // 보유 정보
            GameData.Holding h = gd.getHolding(stockName);
    
            int holdingQty = 0; // 보유 수량 기본값 0으로
            int avgPrice = 0; // 평균 단가 기본값 0으로
            if (h != null) {
                holdingQty = h.getQuantity(); // Holding에서 보유수량 가져옴
                avgPrice = h.getAvgPrice(); // Holding에서 평균 단가 가져옴
            }

            JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 5));
            infoPanel.setOpaque(false);
            infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // 아래 영역과 간격 두기
            
            infoPanel.add(new JLabel("현재가(시장가)"));
            infoPanel.add(new JLabel(String.format("%,d 원", tradePrice),SwingConstants.RIGHT)); // 오른쪽 정렬
            
            infoPanel.add(new JLabel("보유 수량 / 평단가"));
            holdingValue = new JLabel( String.format("%d주 / %,d원", holdingQty, avgPrice),SwingConstants.RIGHT); // 오른쪽 정렬
            
            infoPanel.add(holdingValue);
            cardPanel.add(infoPanel);

            // 수량 + 예상 체결 금액
            JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            formPanel.setOpaque(false); // 투명
            formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // 아래 영역과 간격 두기

            quantityField = new JTextField("1");
            quantityField.setHorizontalAlignment(JTextField.RIGHT); // 숫자를 오른쪽으로 정렬

            totalValueLabel = new JLabel("0 원", SwingConstants.RIGHT); // 오른쪽으로 정렬
            totalValueLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));

            formPanel.add(new JLabel("수량(주)"));
            formPanel.add(quantityField); // 수랑 입력칸 추가
            formPanel.add(new JLabel("예상 체결 금액"));
            formPanel.add(totalValueLabel); // 예상 금액 라벨 추거
            cardPanel.add(formPanel);
           
            // 퍼센트 버튼
            JPanel percentPanel = new JPanel(new GridLayout(1, 4, 10, 0));
            percentPanel.setOpaque(false); // 투명
            percentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0)); // 아래 영역과 간격 두기
            // 10%,25%,50% 최대 버튼 추가
            JButton btn10 = new JButton("10%");
            JButton btn25 = new JButton("25%");
            JButton btn50 = new JButton("50%");
            JButton btnMax = new JButton("최대");

            percentPanel.add(btn10);
            percentPanel.add(btn25);
            percentPanel.add(btn50);
            percentPanel.add(btnMax);

            cardPanel.add(percentPanel);
          
            // 현금
            capitalLabel = new JLabel( "보유 현금: " + String.format("%,d 원", gd.getCapital())); // GameData에서 현재 현금을 가져와 라벨 생성
            capitalLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
            capitalLabel.setForeground(new Color(120, 130, 140));
            capitalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT); // 오른쪽으로 

            cardPanel.add(capitalLabel);
            
            // 금액 계산
            Runnable updateTotal = () -> { // (161줄) 코드를 넘겨야하므로 Runnable을 씀
                try {
                    int q = Integer.parseInt(quantityField.getText()); // 수량 문자열을 정수로 변환
                    totalValueLabel.setText( String.format("%,d 원", q * tradePrice)); // 수량 X 현재가 표시
                } catch (Exception e) {
                    totalValueLabel.setText("0 원"); // 변환 실패하면 0원 표시
                }
            };
            
            // quantityField 내용이 변할 때마다 updateTotal을 호출하도록 리스너 연결(248 줄)
            quantityField.getDocument().addDocumentListener(new SimpleDocListener(updateTotal));
            updateTotal.run();

            // 메소드를 이용해서 살 수 있는 수량을 계산해서 입력칸에 넣고 총액 갱신
            btn10.addActionListener(e -> setQtyByPercent(0.10, updateTotal)); 
            btn25.addActionListener(e -> setQtyByPercent(0.25, updateTotal));
            btn50.addActionListener(e -> setQtyByPercent(0.50, updateTotal));
            btnMax.addActionListener(e -> setQtyByPercent(1.00, updateTotal));
        }

        int getQty() { // 매수버튼에서 수량을 가져갈 수 있게 반환
            return Integer.parseInt(quantityField.getText());
        }
        void refreshCapital() {  // 현금이 매수 후 줄어들 때 라벨 최신 값으로 갱신
            capitalLabel.setText( "보유 현금: " +String.format("%,d 원", gd.getCapital()));
        }
        // 버튼 눌렀을 때 수량 계산해서 입력칸에 넣는 메소드
        private void setQtyByPercent(double percent, Runnable updateTotal) { 
            int capital = gd.getCapital(); // 현재 보유 현금 가져옴
            int qty = (int) ((capital * percent) / tradePrice); // 살 수 있는 주 수 계산
            if (qty < 1) qty = 1; // 최소 1주는 삼
            quantityField.setText(Integer.toString(qty)); // 계산한 qty를 문자열로 바꿔서 quantityField에 넣음
            updateTotal.run(); // 총 금액 다시 계산
        }
    }

    // 하단 영역
    class BuySouthPanel extends JPanel {

        BuySouthPanel(String stockName,  int tradePrice,  BuyCenterPanel centerPanel) { // 생성자 : 종목명, 현재가, 중앙패널 정보를 받음
            setBackground(new Color(246, 249, 252)); 
            
            JButton buyButton = new JButton("매수하기");
            buyButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            buyButton.setPreferredSize(new Dimension(250, 55));
            buyButton.setBackground(new Color(52, 152, 219));
            buyButton.setForeground(Color.WHITE);

            add(buyButton);

            buyButton.addActionListener(e -> {
                try { // try-catch 구문 사용
                    int qty = centerPanel.getQty(); // 센터패널의 메소드(171줄) 호출해서 사용자가 입력한 수량을 가져옴

                    if (!GameData.get().buy(stockName, qty, tradePrice)) { // 매수가 실패할 때
                        JOptionPane.showMessageDialog(
                                this,
                                "잔고가 부족합니다.", // 내용
                                "매수 실패", // 제목
                                JOptionPane.ERROR_MESSAGE // 에러 메세지
                        );
                        return;
                    }
                    centerPanel.refreshCapital(); // 매수 성공했으므로 보유 현금 최신 값으로 갱신

                    GameData.Holding h = GameData.get().getHolding(stockName); // GameData에서 해당 종목의 보유 종목을 가져옴
                    
                    int newQty; // 새 보유 수량
                    int newAvg; // 새 평단가
                    if (h == null) {
                        newQty = 0;
                        newAvg = 0;
                    } else { // 보유 정보가 있을 때 최신 값으로 반영
                        newQty = h.getQuantity(); 
                        newAvg = h.getAvgPrice();
                    }

                    holdingValue.setText(String.format("%d주 / %,d원", newQty, newAvg)); // 화면에 최신 값 반영
                    JOptionPane.showMessageDialog(
                            this,
                            "시장가로 매수되었습니다.", // 내용
                            "매수 완료", // 제목
                            JOptionPane.INFORMATION_MESSAGE // 정보 메세지
                    );

                } catch (Exception ex) { // 예외처리
                    JOptionPane.showMessageDialog(
                            this,
                            "수량을 올바르게 입력하세요.", // 내용
                            "입력 오류", // 제목
                            JOptionPane.WARNING_MESSAGE // 경고 메세지
                    );
                }
            });
        }
    }
    // 공통 리스너
    //SimpleDocListener는 수량 입력이 바뀌는 모든 순간마다 updateTotal을 자동으로 실행시키기 위해서 씀
    class SimpleDocListener implements DocumentListener { // 텍스트 입력칸의 내용이 바뀌었는지 감시하는 역할
        private Runnable r; // 텍스트가 바뀔때 실행한 코드를 저장하는 변수
        SimpleDocListener(Runnable r) { this.r = r; } // 입력이 바뀌면 r을 실행한다(161 줄)
        // 모두 r.run() 호출
        public void insertUpdate(javax.swing.event.DocumentEvent e) { r.run(); } // 텍스트가 추가 될 떄 
        public void removeUpdate(javax.swing.event.DocumentEvent e) { r.run(); } // 텍스트가 지워질 때
        public void changedUpdate(javax.swing.event.DocumentEvent e) { r.run(); } // 다른 방식으로 바뀔 떄
    }
}
