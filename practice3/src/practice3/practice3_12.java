package practice3;
import java.util.Random;
import java.util.Scanner;
public class practice3_12 {

	public static void main(String[] args) {
		
		Scanner sc=new Scanner(System.in);
		Random rand=new Random();
		
		String boyMiddleList[] = {"기", "민", "용", "종", "현", "진", "재", "승", "소", "상", "지"};
		String boyLastList[] = {"태", "진", "광", "혁", "우", "철", "빈", "준", "구", "호", "석"};
		String girlMiddleList[] = {"은", "원", "경", "수", "현", "예", "여", "송", "서", "채", "하"};
		String girlLastList[] = {"진", "연", "경", "서", "리", "숙", "미", "원", "린", "희", "수"};
		System.out.println("***** 작명 프로그램이 실행됩니다. *****");
		
		while(true) {
			System.out.print("남/여 선택>>");
			String choose=sc.next();
			
			if(choose.equals("남")) {
				int mid=rand.nextInt(11);
				int last=rand.nextInt(11);
				
				System.out.print("성 입력>>");
				String first=sc.next();
				
				System.out.println("추천 이름: "+first+boyMiddleList[mid]+boyLastList[last]);
				
			}
			else if(choose.equals("여")) {
				int mid=rand.nextInt(11);
				int last=rand.nextInt(11);
				
				System.out.print("성 입력>>");
				String first=sc.next();
				
				System.out.println("추천 이름: "+first+girlMiddleList[mid]+girlLastList[last]);
			}
			else if(choose.equals("그만")) {
				break;
			}
			else {
				System.out.println("남/여/그만 중에서 입력하세요.");
			}

		}
		sc.close();
	}
}
