package opchallenge3;

import java.util.Scanner;
import java.util.Random;


public class UpDown {

	public static void main(String[] args) {
		
		Scanner sc= new Scanner(System.in);
		Random rand=new Random();
		
	while(true) {	
		
		System.out.println("수를 결정하였습니다. 맞추어 보세요.");
		System.out.println("0-99");
		
		int count=0; // 0부터 시작
		int maxcount=6; // 최대로 가능한 횟수:6
		int answer= rand.nextInt(99) + 1; // 1부터 99까지 생성
		
		int min=1;  // 범위를 정하기 위해 최소랑 최대를 변수로 지정
		int max=99;
		
		while(count<maxcount) {
			count++;
			System.out.print(count+">>");
			int input =sc.nextInt(); // 사용자가 입력한 값의 변수
			
			if(input == answer) {
				System.out.println(count+ "번 만에 맞았습니다.");
				break;
			}else if(input < answer) {
				System.out.println("더 높게");
				min=input;
			}else {
				System.out.println("더 낮게");
				max=input;
			}
			System.out.println(min+ "~" + max);
			System.out.println("남은 기회 :"+ (maxcount-count)+"번");
			
			if(count == maxcount) {
				System.out.println("기회를 다 소모하였습니다.");
				System.out.println("정답은 "+ answer+ "이었습니다.");
			}
		}
		System.out.print("다시하시겠습니까(y/n)>>");
		String again=sc.next();
		if(again.equals("n")) {
			System.out.println("게임 종료");
			break;
		}
	}
	
	sc.close();
	}
}
