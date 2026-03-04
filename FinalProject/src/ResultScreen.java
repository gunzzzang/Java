import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ResultScreen extends JFrame {

    private static final String FILE_NAME = "rank.txt";  // 랭킹을 저장/읽을 파일 이름

    private GameData gd; // GameData를 참조할 변수
    private java.util.List<RankEntry> ranks = new ArrayList<>();  // 랭킹 목록을 담을 리스트

    public ResultScreen() {
        gd = GameData.get(); // 객체를 가져옴

        loadRanks();     // 1. rank.text파일에서 랭킹 불러오는 메소드
        saveMyResult();  // 2. 지금 플레이어의 결과 저장 메소도
        sortRanks();     // 3. 자산 기준으로 내림차순 정렬 메소드

        setTitle("게임 결과");
        setSize(600, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Container c = getContentPane();
        c.setLayout(new BorderLayout(10, 10));
        c.setBackground(new Color(245, 247, 250)); 
        c.add(createTitlePanel(), BorderLayout.NORTH);
        c.add(createCenterPanel(), BorderLayout.CENTER);
        c.add(createBottomPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // 파일 로드 메소드
    private void loadRanks() {
        File file = new File(FILE_NAME); // rank.text 파일 객체 생성
        if (!file.exists()) return;

        // try-catch 구문 사용
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) { // 내장되어있는 readLine() 사용해서 파일을 한줄 씩 읽음
                String[] t = line.split(","); // 쉼표로 분리 (닉네임,자산,수익률 형태) 
                ranks.add(new RankEntry(
                        t[0], // 닉네임
                        Integer.parseInt(t[1]), // 자산 (문자열을 int로)
                        Double.parseDouble(t[2]) // 수익률 (문자열을 double로)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace(); // 오류 콘솔에 출력
        }
    }

    // 플레이어의 결과 저장
    private void saveMyResult() {
        int initial = 10_000_000;
        int asset = gd.getCapital(); // GameData에서 최종 자산 가져오기
        double rate = (asset - initial) / (double) initial * 100.0; // 수익률 계산

        // 모든 결과 저장
        ranks.add(new RankEntry(gd.getNickname(), asset, rate)); // 결과를 RankEntry 형태로 만들어서, ranks 리스트에 추가

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME, true))) { // pw 객체 생성
            pw.println(gd.getNickname() + "," + asset + "," + String.format("%.2f", rate)); // 파일에 한 줄 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 정렬 메소드
    private void sortRanks() {
        ranks.sort((a, b) -> Integer.compare(b.asset, a.asset)); // sort() 메소드를 사용해 asset이 큰 사람부터 오도록 정렬
    }

    // 상단 제목 영역
    private JPanel createTitlePanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        JLabel title = new JLabel("게임 결과", JLabel.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        p.add(title);
        return p;
    }

   // 센터 요약+랭킹 영역 
    private JPanel createCenterPanel() { 
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 여백을 위10, 왼20, 아래10, 오른20
        center.setBackground(new Color(245, 247, 250)); 

        center.add(createSummaryPanel(), BorderLayout.NORTH); // 센터 패널 위쪽에 플레이어 결과 요약 
        center.add(createRankPanel(), BorderLayout.CENTER); // 센터 중앙에 랭킹 테이블 
        return center;
    }
    // 플레이어의 닉네임, 최종자산, 수익, 수익률을 정리한 패널 
    private JPanel createSummaryPanel() {
        JPanel outer = new JPanel(new GridLayout(4, 1, 8, 8));
        outer.setBackground(new Color(245, 247, 250)); 

        int initial = 10_000_000;
        int asset = gd.getCapital(); // GameData에서 최종 자산을 가져옴
        int profit = asset - initial; // 이윤 계산
        double rate = profit / (double) initial * 100.0; // 수익률 계산

        // (138 줄) makeRow 메소드 사용
        outer.add(makeRow("닉네임", gd.getNickname()));
        outer.add(makeRow("최종 자산", String.format("%,d 원", asset)));
        outer.add(makeRow("수익", String.format("%+,d 원", profit)));
        outer.add(makeRow("수익률", String.format("%.2f %%", rate)));

        return outer;
    }

    private JScrollPane createRankPanel() {
        String[] cols = {"순위", "닉네임", "자산", "수익률"}; // 테이블의 컬럼 지정
        int limit = Math.min(10, ranks.size()); // 랭킹은 최대 10개 -> 10이랑 ranks의 크기중 작은 값

        Object[][] data = new Object[limit][4]; // 행은 limit 열은 4개
        for (int i = 0; i < limit; i++) {
            RankEntry r = ranks.get(i);
            data[i][0] = i + 1; // 순위는 1부터이므로 i+1
            data[i][1] = r.name;
            data[i][2] = String.format("%,d 원", r.asset);
            data[i][3] = String.format("%.2f %%", r.rate);
        }

        JTable table = new JTable(data, cols); 
        table.setEnabled(false); // 수정 X
        table.setRowHeight(24); // 테이블의 각 행의 높이 :24

        return new JScrollPane(table); // 랭킹이 길어져도 스크롤이 가능
    }

    // 닉네임, 자산, 수익, 수익률을 각각 한줄로 추가
    private JPanel makeRow(String t, String v) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel left = new JLabel(t); // 왼쪽은 항목 이름
        JLabel right = new JLabel(v, JLabel.RIGHT); // 오른쪽은 값 

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    // 맨 아래에 다시시작, 종료 버튼
    private JPanel createBottomPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        p.setBackground(new Color(245, 247, 250)); 

        JButton btnRestart = new JButton("다시 시작");
        JButton btnExit = new JButton("종료");

        btnRestart.setPreferredSize(new Dimension(140, 38));
        btnExit.setPreferredSize(new Dimension(140, 38));

        btnRestart.addActionListener(e -> {
            GameData.get().reset(); // GameData의 메소드로 게임 데이터 초기화
            new FinalProjectLogin().setVisible(true); // 로그인 화면을 새로 띄움
            dispose(); // 결과창 닫기
        });

        btnExit.addActionListener(e -> System.exit(0)); // 종료 버튼 누르면 프로그램 종료

        p.add(btnRestart);
        p.add(btnExit);
        return p;
    }

    static class RankEntry { // 랭킹 한 줄에 필요한 데이터들을 표현하기 위한 자료형
        String name;
        int asset; // 자산
        double rate; // 수익률
        RankEntry(String n, int a, double r) {
            name = n;
            asset = a;
            rate = r;
        }
    }
}

