package playlist;

import java.util.Scanner;
class Song {
	private String title;
	private String singer;
	private String lang;
	private String genre; // 추가 기능
	private int year;
	private String lyric; // 1절 가사 출력
	
	
	public Song(String title, String singer, String lang, String genre, int year,String lyric ) {
		
		this.title=title;
		this.singer=singer;
		this.lang=lang;
		this.genre=genre;
		this.year=year;
		this.lyric=lyric;
		
	}
	
	public void show() {
		System.out.println(year + "년 "+ lang +"의 "+singer +"가 부른 "+ title + "(장르:" +genre + ")");
	}
	
	public void showlyric() {
		System.out.println(lyric);
	}
	
	public static void main(String[] args) {
		Song song= new Song("가로수 그늘 아래 서면", "이문세", "한국", "발라드", 1988,
		    "라일락 꽃향기 맡으면\n"
	      + "잊을 수 없는 기억에\n"
	      + "햇살 가득 눈부신 슬픔 안고\n"
	      + "버스 창가에 기대 우네\n"
	      + "가로수 그늘 아래 서면\n"
	      + "떠 가는듯 그대 모습\n"
	      + "어느 찬비 흩날린 가을 오면\n"
	      + "아침 찬바람에 지우지"
	);
	
	song.show();
	Scanner sc=new Scanner(System.in);
	System.out.print("1 절 가사를 보시겠습니까?(yes or no)");
	String option=sc.next();
	
	if(option.equals("yes")){
	   song.showlyric();
	}
	else {
		System.out.println("(가사를 표시하지 않습니다.)");
	}
	sc.close();
	}
}


