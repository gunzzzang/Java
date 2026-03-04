import java.util.*;
import java.text.SimpleDateFormat; // 시간을 2025-12-14 15:30같은 문자열로 반환하기 위해서 import 

// 싱글턴 방식(프로그램 전체에서 단 하나의 객체만 존재하도록 하는 방식)
public class GameData {

	private static final GameData instance = new GameData(); // GameData 클래스 객체인 instance

	private boolean finished = false; // 게임 종료 여부

	private String nickname = "유저";
	private int day = 1;
	private int capital = 10_000_000; // 초기 자본

	// Manager : 여러 객체를 보관하고,상태를 동기화 관리하는 클래스
	private final StockManager stockManager; // 주가/캔들 데이터 관리 역할
	private final NewsManager newsManager; // 뉴스 관리 역할
	private final TradeManager tradeManager; // 거래/보유종목 관리 역할

	private GameData() {
		stockManager = new StockManager();
		newsManager = new NewsManager();
		tradeManager = new TradeManager();
	}

	public static GameData get() { // 외부애서 인스턴스에 접근하기 위한 get() 메소드
		return instance;
	}

	// 유저 정보(외부에서 접근할 수 있게 하는 메소드)
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) { // 로그인화면에서 입력한 닉네임을 GameData에 저장
		this.nickname = nickname;
	} 
	public int getDay() { // 현재 날짜 조회
		return day;
	} 
	public int getCapital() { // 현재 자산 조회
		return capital;
	} 
	public boolean isFinished() { // 게임이 끝났는지 체크
		return finished;
	}
	public void finishGame() { // 게임 종료(365일)시 호출
		finished = true;
	}

	// 날짜 관리
	public void setDay(int day) { // 여러 화면의 시간 기준을 맞추기 위한 동기화용 메소드
		if (finished)
			return; // 끝난 게임이면 날짜 변경 X
		if (day >= 365) { // 365일 넘어가면 날짜를 365일로 고정하고 게임 종료
			this.day = 365;
			finishGame();
		} else {
			this.day = day; // 365일 미만이면 작동
		}
		stockManager.ensureAllUpToDay(this.day); // ensureAllUpToDay() : 차트/현재가가 빈 값이 안나오게 하는 메소드(280 줄)
	}

	public void skipDay(int amount) { // 날짜를 하루씩 옮길 때
		if (amount <= 0 || finished)
			return; // 음수 or 게임 끝났으면 진행 X

		for (int i = 0; i < amount; i++) {
			if (day >= 365) { // 365일 넘어가면 365일 고정하고 게임 종료
				day = 365;
				finishGame();
				break;
			}
			day++; // 안 넘어가면 날짜 증가
			newsManager.dailyNewsCheck(day, stockManager.stocks.keySet()); // 오늘 날짜 기준으로 뉴스 발생 최신화 함
			stockManager.ensureAllUpToDay(day); // 각 종목 캔들을 오늘까지 생성해서 가격/차트 최신화함
		}
	}

	// 종목 상태 (외부 호환 유지용 메소드 -> GameData 내부 구조를 바꿔도 다른 파일을 안 고치려는 목적)
	public StockState getOrCreateStock(String name, int basePrice) {
		return stockManager.getOrCreateStock(name, basePrice, day); // 종목 상태를 얻음(종목 관리 클래스 :265줄)
	}

	// 뉴스 (외부 호환 유지용 메소드 -> GameData 내부 구조를 바꿔도 다른 파일을 안 고치려는 목적)
	public List<NewsEvent> getRecentNews() {
		return newsManager.getRecentNews(); // 최근 뉴스 목록을 얻음 (뉴스 관리 클래스 :140줄)
	}

	// 거래 (외부 호환 유지용 메소드 -> GameData 내부 구조를 바꿔도 다른 파일을 안 고치려는 목적)
	public Holding getHolding(String name) {
		return tradeManager.getHolding(name); // 특정 보유정보 조회 (매수/매도 관련 클래스 :405줄)
	}

	public Map<String, Holding> getHoldings() {
		return tradeManager.getHoldings(); // 전체 보유종목 조회
	}

	public List<TradeRecord> getTrades() {
		return tradeManager.getTrades(); // 거래 내역 리스트 조회
	}

	public boolean buy(String stockName, int qty, int price) {
		return tradeManager.buy(stockName, qty, price); // 성공/실패 반환
	}

	public boolean sell(String stockName, int qty, int price) {
		return tradeManager.sell(stockName, qty, price); // 성공/실패 반환
	}

	public void reset() { // 결과창에서 다시 시작 누를 때 게임 리셋
		finished = false; // 종료 상태 해제
		// 게임 정보 초기화
		nickname = "유저";
		day = 1;
		capital = 10_000_000;
		// 리셋용 메소드를 이용해서 초기화 
		stockManager.clear();
		newsManager.clear();
		tradeManager.clear();
	}

	// 뉴스 시스템
	public static class NewsEvent {
		public String stockName; // 뉴스가 적용되는 종목명
		public String title; // 뉴스 제목
		public double impact; // 영향도
		public int daysLeft; // 뉴스 효과 남은 일수
		public int startDay; // 뉴스가 시작된 게임 날짜

		// 뉴스 하나를 표현하기 위해 정보를 받아 NewsEvent 객체를 초기화하는 생성자
		public NewsEvent(String stockName, String title, double impact, int daysLeft, int startDay) {
			this.stockName = stockName;
			this.title = title;
			this.impact = impact;
			this.daysLeft = daysLeft;
			this.startDay = startDay;
		}
	}

	// 뉴스 시스템 관리하는 클래스
	private class NewsManager {
		// key로 value를 바로 찾는 Map 자료 구조 사용
		private final Map<String, List<NewsEvent>> newsPool = new HashMap<>(); // 뉴스 후보 리스트
		private final Map<String, NewsEvent> activeNews = new HashMap<>(); // 현재 뉴스
		private final Deque<NewsEvent> recentNews = new ArrayDeque<>(); // 최근 뉴스를 저장하는 큐
		private final Random rand = new Random();

		NewsManager() {
			initNewsPool(); // NewsManager 만들어질 때 바로 실행, initNewsPool()에 종목 별 뉴스를 세팅 해놓음
		}

		void dailyNewsCheck(int day, Set<String> stockNames) {
            // Map에 들어있는 한 쌍들을 Iterator를 이욯해 하나씩 순서대로 꺼내고 만들고 필요하면 삭제함(it이라는 변수로 Iterator 저장)
			for (Iterator<Map.Entry<String, NewsEvent>> it = activeNews.entrySet().iterator(); // 초기화
					it.hasNext();) // 조건(내장 메소드를 이용해 꺼낼게 있는지 확인함)
			        // 증감 부분 없음
			{
				NewsEvent ev = it.next().getValue(); // ev에 다음 Map 항목의 뉴스객체 저장
				ev.daysLeft--; // 뉴스의 남은 날짜 하루 줄임
				if (ev.daysLeft <= 0)
					it.remove(); // 기간이 끝났으면 뉴스를 목록에서 제거
			}

			Iterator<String> it = stockNames.iterator();   // 종목 이름들을 하나씩 꺼낼 Iterator 생성
			while (it.hasNext()) {  // 아직 꺼낼 종목이 남아 있으면 반복
			    String stockName = it.next();    // 다음 종목 이름 하나 꺼냄

			    if (activeNews.containsKey(stockName)) {  // containsKey() 메소드를 이용해서 종목이 있는지 확인
			        continue;
			    }
			    if (rand.nextDouble() < 0.02) { // 2퍼센트의 확률
			        List<NewsEvent> pool = newsPool.get(stockName);// 해당 종목의 뉴스 후보 목록 가져오기
			        
			        if (pool == null || pool.isEmpty()) { // poo1이 아예 없는경우 or 비어있을 때 넘어감 
			            continue;
			        }
			        // 뉴스 후보 중 하나를 랜덤으로 선택해서 t에 저장
			        int randomIndex = rand.nextInt(pool.size());
			        NewsEvent t = pool.get(randomIndex);

			        // 선택된 뉴스로 새 뉴스 객체 생성
			        NewsEvent ev = new NewsEvent(
			                t.stockName,
			                t.title,
			                t.impact,
			                t.daysLeft,
			                day
			        );
			        activeNews.put(stockName, ev); // 현재 활성 뉴스에 등록
			        addRecentNews(ev); // (204 줄)메소드를 이용해서 최근 뉴스 목록에 추가
			    }
			}

		}

		NewsEvent getActiveNews(String stockName) {// 종목에 적용 중인 뉴스가 있으면 뉴스 객체를 반환함 
			return activeNews.get(stockName); 
		}
		List<NewsEvent> getRecentNews() { // 최근 뉴스 목록을 가져옴
			return new ArrayList<>(recentNews);
		}

		private void addRecentNews(NewsEvent ev) {
			recentNews.addFirst(ev); // Deque 내장메소드 addFirst()를 이용해서 맨 앞에 ev 추가
			if (recentNews.size() > 5) // 5개 이상이면 
				recentNews.removeLast(); // Deque 내장메소드 removeLast()를 이용해서 가장 오래된 뉴스 삭제
		}

		private void clear() { // 뉴스 상태 초기화
			activeNews.clear();
			recentNews.clear();
		}

		// 종목별로 긍정·부정 뉴스 데이터를 미리 구성하여 뉴스 newsPool에 저장함
		private void initNewsPool() {

			List<NewsEvent> samsung = new ArrayList<>();
			samsung.add(new NewsEvent("삼성전자", "삼성전자, 차세대 AI 칩 양산 본격화… 글로벌 수요 급증 기대", +0.04, 1, 0));
			samsung.add(new NewsEvent("삼성전자", "삼성전자, 미국 대형 투자사 1조 원 규모 매수 포착", +0.06, 1, 0));
			samsung.add(new NewsEvent("삼성전자", "삼성, 초고성능 HBM 메모리 계약 체결… 엔비디아 공급 확대 전망", +0.12, 1, 0));
			samsung.add(new NewsEvent("삼성전자", "삼성전자, 반도체 재고 증가 우려로 실적 전망치 하향", -0.13, 1, 0));
			samsung.add(new NewsEvent("삼성전자", "삼성, 중국 스마트폰 시장 점유율 하락… 경쟁 심화", -0.06, 1, 0));
			samsung.add(new NewsEvent("삼성전자", "메모리 가격 단기 조정 전망… 단기 실적 부담", -0.04, 1, 0));
			newsPool.put("삼성전자", samsung);

			List<NewsEvent> kakao = new ArrayList<>();
			kakao.add(new NewsEvent("카카오", "카카오, AI 기반 신규 서비스 흥행… 트래픽 30% 증가", +0.03, 1, 0));
			kakao.add(new NewsEvent("카카오", "카카오페이, 금융상품 수익 급증으로 흑자전환 성공", +0.14, 1, 0));
			kakao.add(new NewsEvent("카카오", "카카오게임즈, 신작 출시 첫날 매출 1위 기록", +0.06, 1, 0));
			kakao.add(new NewsEvent("카카오", "카카오, 데이터센터 안정성 논란 재점화", -0.05, 1, 0));
			kakao.add(new NewsEvent("카카오", "공정위, 카카오 계열사 내부거래 조사 착수", -0.09, 1, 0));
			kakao.add(new NewsEvent("카카오", "카카오톡 광고 노출 증가, 이용자 불만 확산", -0.04, 1, 0));
			newsPool.put("카카오", kakao);

			List<NewsEvent> naver = new ArrayList<>();
			naver.add(new NewsEvent("네이버", "네이버웹툰, 글로벌 구독자 1억 2천만 명 돌파… 북미 매출 급성장", +0.21, 1, 0));
			naver.add(new NewsEvent("네이버", "네이버, AI 검색 고도화 성공… 사용자 만족도 개선", +0.05, 1, 0));
			naver.add(new NewsEvent("네이버", "네이버 클라우드, 정부 대규모 프로젝트 단독 수주", +0.23, 1, 0));
			naver.add(new NewsEvent("네이버", "네이버, 광고 시장 침체로 실적 전망치 하향", -0.27, 1, 0));
			naver.add(new NewsEvent("네이버", "네이버페이 수수료 논란… 사용자 불만 급증", -0.05, 1, 0));
			naver.add(new NewsEvent("네이버", "일본 시장 경쟁 심화… 라인 매출 성장 둔화", -0.18, 1, 0));
			newsPool.put("네이버", naver);

			List<NewsEvent> hynix = new ArrayList<>();
			hynix.add(new NewsEvent("SK하이닉스", "SK하이닉스, HBM4 개발 성공… 전 세계 생산 경쟁력 강화", +0.14, 1, 0));
			hynix.add(new NewsEvent("SK하이닉스", "메모리 반도체 가격 강세… SK하이닉스 최대 수혜 전망", +0.16, 1, 0));
			hynix.add(new NewsEvent("SK하이닉스", "AI 서버 투자 증가로 주문량 2배 확대", +0.13, 1, 0));
			hynix.add(new NewsEvent("SK하이닉스", "SK하이닉스, 공장 정전 사고로 일시적 생산 차질", -0.03, 1, 0));
			hynix.add(new NewsEvent("SK하이닉스", "미·중 규제 충돌로 중국향 수출 감소 가능성", -0.06, 1, 0));
			hynix.add(new NewsEvent("SK하이닉스", "DRAM 단기 가격 조정 예고… 실적 변동성 확대", -0.10, 1, 0));
			newsPool.put("SK하이닉스", hynix);

			List<NewsEvent> hyundai = new ArrayList<>();
			hyundai.add(new NewsEvent("현대차", "현대차 전기차 신모델, 사전예약 6만 대 돌파", +0.04, 1, 0));
			hyundai.add(new NewsEvent("현대차", "현대차그룹, 자율주행 3단계 상용화 승인 획득", +0.05, 1, 0));
			hyundai.add(new NewsEvent("현대차", "현대차, 유럽 판매량 역대 최고치 경신", +0.16, 1, 0));
			hyundai.add(new NewsEvent("현대차", "현대차, 엔진 결함 리콜 10만 대… 품질 논란 재점화", -0.13, 1, 0));
			hyundai.add(new NewsEvent("현대차", "현대차 노조 파업 예고… 생산 차질 우려 확대", -0.07, 1, 0));
			hyundai.add(new NewsEvent("현대차", "전기차 보조금 축소 이슈… EV 판매 영향 가능성", -0.15, 1, 0));
			newsPool.put("현대차", hyundai);
		}
	}

	// 종목 관리 클래스
	private class StockManager {

		private final Map<String, StockState> stocks = new HashMap<>(); 

		// 메소드 정의
		StockState getOrCreateStock(String name, int basePrice, int day) {
			StockState s = stocks.get(name); //get() 메소드 이용해서 Map에 종목이 있는지 확인
			if (s == null) { // 처음 등장한 종목이면
				s = new StockState(name, basePrice); // 새로운 StockState 객체 생성
				stocks.put(name, s); // Mapdp 등록해서 재사용 가능하게 함
			}
			s.ensureUpToDay(day); // 메소드()를 이용해서 캔들을 day까지 채움
			return s;
		}

		void ensureAllUpToDay(int day) {
		    Iterator<StockState> it = stocks.values().iterator(); // 종목 이름들을 하나씩 꺼낼 Iterator 생성
		    while (it.hasNext()) { //  아직 남아있으면 반복
		        StockState s = it.next();  //  다음 종목 하나 꺼냄
		        s.ensureUpToDay(day);   // 그 종목을 day까지 생성
		    }
		}
		// 종목 데이터를 초기화하는 메소드
		void clear() {
			stocks.clear();
		}
	}

	public static class StockState {

		public static class Candle {
			public double open, close, high, low; 

			public Candle(double open, double close, double high, double low) { // 생성자 : 하루치 주가 정보를 받아서 하나의 캔들 객체 초기화
				this.open = open; // 시가
				this.close = close; // 종가
				this.high = high; // 고가
				this.low = low; // 저가 
			}
		}

		private String name; // 종목명
		private int basePrice; // 초기값
		private ArrayList<Candle> candles = new ArrayList<>(); // candles: 모든 날짜 캔들 저장리스트
		private Random r = new Random();// 주가는 랜덤으로 변동
		private int lastGeneratedDay = 0; // 마지막으로 생성된 날짜

		public StockState(String name, int basePrice) { // 생성자 : 종목 하나의 기본 정보를 설정
			this.name = name; 
			this.basePrice = basePrice; 
		}

		public void ensureUpToDay(int endDay) {
			if (endDay <= lastGeneratedDay) // 이미 만들어져있으면 종료
				return;

			double price; // 캔들 생성시 기준이 될 변수

			if (candles.isEmpty()) {
			    price = basePrice; // 아직 캔들이 없으면 시가에서 시작
			} else {
			    price = candles.get(candles.size() - 1).close; // 있으면 마지막 날 종가부터 시작(리스트는 0부터 시작하므로 -1) 
			}
			while (lastGeneratedDay < endDay) { // 목표 날짜까지 도달할 때까지 하루씩 캔들을 만듬

				double volatility = 0.05; // 하루 변동 폭 기본값 :5%
				double percentChange = (r.nextDouble() * volatility * 2 - volatility); // 오늘 주가의 변동률을 랜덤으로 함

				// 가격 하락은 상대적으로 빠르게 발생하지만, 하락 이후 동일 수준까지 회복되기 어려움-> deviation 보정 로직을 도입
				double deviation = (price - basePrice) / basePrice; // 현재 가격이 기준가에서 얼만큼 벗어났는지 비율로 표현
				percentChange += (-deviation * 0.1); // 가격이 너무 오르면 누르고 너무 내려가면 끌어올림

				NewsEvent ev = GameData.get().newsManager.getActiveNews(name); // 종목에 뉴스가 있는지 확인
				if (ev != null) // 뉴스가 있으면 뉴스에 설정한 영향도를 가격변동률에 더함
					percentChange += ev.impact;

				double open = price; // 시가는 전날 종가로 시작(실제 주식 시장 로직)
				double close = open * (1 + percentChange);  // 종가는 시가에 변동률을 적용한 것 
				double high = Math.max(open, close) * (1 + r.nextDouble() * 0.03); // 시가와 종가중 높은 값 기준으로 랜덤으로 위로 튀게 함
				double low = Math.min(open, close) * (1 - r.nextDouble() * 0.03); // 시가와 종가중 낮은 값 기준으로 랜덤으로 아래로 튀게 함

				candles.add(new Candle(open, close, high, low)); // 계산한 모든 주식 정보를 캔들 리스트에 저장 
				price = close; // 오늘 종가는 내일 시가
				lastGeneratedDay++; // 날짜 하루 증가
			}
		}
		// 화면에 최근 데이터만 보여주기위한 메소드
		public List<Candle> getRecentCandles(int count) { // count만큼 최근 캔들을 가져옴
			if (candles.isEmpty()) // 없을 때 빈 리스트를 반환해야함
				return Collections.emptyList(); // 자바에서 제공하는 표준 메소드를 이용해서 비어 있는 리스트를 반환함
			int from = Math.max(0, candles.size() - count); // 캔들이 count보다 적으면 전부, 많으면 count만큼 가져옴
			return candles.subList(from, candles.size()); // 내장메소드를 이용해서 원하는 구간만 자름
		}

		// 전날 종가를 반환하는 메소드
		public double getLastClose() { 
		    if (candles.isEmpty()) {
		        // 아직 캔들이 하나도 없으면 기준가 반환
		        return basePrice;
		    } else {
		        // 있으면 오늘종가 반환
		        return candles.get(candles.size() - 1).close;
		    }
		}

		public double getPrevClose() {
		    if (candles.size() < 2) {
		        // 데이터가 없으면 기준가 반환
		        return basePrice;
		    } else {
		        // 있으면 어제 종가 반환(-2이므로 어제)
		        return candles.get(candles.size() - 2).close;
		    }
		}

	}

	// 거래 시스템
	public static class Holding {
		private String name; // 종목명
		private int quantity; // 보유 수량
		private int avgPrice; // 평균 단가 

		public Holding(String name) { // 생성자 : 종목 이름을 받아서 저장
			this.name = name;
		}
		
        // 외부에서 값을 읽을 수 있게 하는 메소드
		public String getName() {
			return name;
		}
		public int getQuantity() {
			return quantity;
		}
		public int getAvgPrice() {
			return avgPrice;
		}
	}

	// 매수/매도 한 번 할 때마다 저장됨
	public static class TradeRecord { 
		private final String dateTime; // 거래한 시간 문자열 (ex: 2025-12-14 15:30)
		private final String stockName; // 거래한 종목명
		private final int quantity;// 거래한 수량
		private final int price; // 체결된 단가
		private final int fee; // 수수료
		private final int amount; // 체결 금액
		private final Integer tradeProfit; // 실현 손익, 손익이 없을 수 있다 -> NULL값이 존재 -> int 말고 정수 객체 타입인 integer씀

		public TradeRecord(String dateTime, String stockName, int quantity, int price, int fee, int amount,
				Integer tradeProfit) { // 생성자 : 필요한 값들을 다 받아서 객체의 필드에 저장
			this.dateTime = dateTime;
			this.stockName = stockName;
			this.quantity = quantity;
			this.price = price;
			this.fee = fee;
			this.amount = amount;
			this.tradeProfit = tradeProfit;
		}
		
        // 외부에서 값을 읽을 수 있게 하는 메소드
		public String getDateTime() {
			return dateTime;
		}
		public String getStockName() {
			return stockName;
		}
		public int getQuantity() {
			return quantity;
		}
		public int getPrice() {
			return price;
		}
		public int getFee() {
			return fee;
		}
		public int getAmount() {
			return amount;
		}
		public Integer getTradeProfit() {
			return tradeProfit;
		}
	}

	// 거래 기능 클래스
	private class TradeManager { 
		private final Map<String, Holding> holdings = new HashMap<>(); // Map으로 보유 종목들 저장
		private final List<TradeRecord> trades = new ArrayList<>(); // 매수/매도 할 때 마다 하나씩 추가

		Holding getHolding(String name) {// 특정 종목의 보유종목 1개 가져옴
			return holdings.get(name); 
		}

		Map<String, Holding> getHoldings() { // 보유 종목 전체 조회
			return holdings; 
		}

		List<TradeRecord> getTrades() { // 거래 내역 전체 조회
			return trades;
		}

		// 매수 기능 메소드
		boolean buy(String stockName, int buyQuantity, int price) { // 각각 종목명, 매수 수량, 1주 가격
			if (finished || buyQuantity <= 0 || price <= 0) // 오류나거나 게임이 종료일 때
				return false;
			int cost = (int) buyQuantity * price; // 매수금액 : 수량 X 가격 
			
			if (cost > capital) // 매수 금액이 자산보다 크면 실패
				return false;
			capital -= cost; // 매수 했으므로 자산에서 차감

			Holding h = holdings.get(stockName); // 해당 종목 보유정보 가져오기
			
			if (h == null) { // 처음 사는 정목이면 Holding 만들고 holdings Map에 등록
				h = new Holding(stockName);
				holdings.put(stockName, h);
			}

			int totalCost = (int) h.avgPrice * h.quantity + cost; // 총 투자금액 다시 계산
			h.quantity += buyQuantity;  // 보유 수량 증가
			h.avgPrice = (int) (totalCost / h.quantity); // 새 평단가 계산

			// 거래 내역 리스트에 이번에 매수한 기록 저장
			trades.add(new TradeRecord(nowString(), stockName, buyQuantity, price, 0, (int) cost, null));
			return true;
		}

		// 매도 기능 메소드
		boolean sell(String stockName, int sellQuantity, int price) { // 각각 종목명, 매도 수량, 1주 가격
			if (finished) // 게임 끝나면 매도 X
				return false;

			Holding h = holdings.get(stockName); // 해당 종목 보유정보 가져오기
			if (h == null || h.quantity < sellQuantity) // 보유 x or 매도 수량이 보유 수량보다 많으면 실패
				return false;

			int revenue = (int)sellQuantity * price; // 매도 금액: 수량 X 현재가
			int cost = (int) sellQuantity * h.avgPrice; // 원가 : 수량 X 평단가
			int tradeProfit = (int) (revenue - cost); // 실현손익 : 매도금액 - 원가

			capital += revenue; // 매도 했으므로 현금이 늘어남
			h.quantity -= sellQuantity; // 팔았으므로 보유 수량 감소
			if (h.quantity == 0) // 다 팔면 보유목록에서 삭제
				holdings.remove(stockName);

			// 거래 내역 리스트에 이번에 매도한 기록 저장
			trades.add(new TradeRecord(nowString(), stockName,sellQuantity, price, 0, (int) revenue,tradeProfit));
			return true;
		}

		// 보유종목 목록, 거래내역 초기화
		void clear() {
			holdings.clear();
			trades.clear();
		}
	}

	// 현재 시간을 2025-12-14 15:30 같은 문자열로 만들어서 반환
	private String nowString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
	}
}
