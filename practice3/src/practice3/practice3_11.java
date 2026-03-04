package practice3;
import java.util.Random;
import java.util.Scanner;
public class practice3_11 {

	public static void main(String[] args) {
		
		System.out.println("***** 구구단을 맞추는 퀴즈입니다. *****");
		Random rand=new Random();
		Scanner sc=new Scanner(System.in);
		int wrongCount=0;
		while(true) {
			int x=rand.nextInt(9)+1;
			int y=rand.nextInt(9)+1;
			int answer=x*y;
			
			System.out.print(x+"x"+y+"=");
			int res=sc.nextInt();
			
			if(res==answer) {
				System.out.println("정답입니다. 잘했습니다.");
			}
			else  {
				wrongCount++;
				if(wrongCount<3) {
					System.out.println(wrongCount+"번 틀렸습니다. 분발하세요.");
				}
				else {
					System.out.println(wrongCount+"번 틀렸습니다. 퀴즈를 종료합니다.");
					break;
				}
			}
	
		}
		sc.close();

	}
}