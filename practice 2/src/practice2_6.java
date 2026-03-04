
import java.util.Scanner;
public class practice2_6 {

	public static void main(String[] args) {
		
		System.out.print("나이를 입력하세요>>");
		Scanner sc=new Scanner(System.in);
		int age=sc.nextInt();
		if(age<0)
			System.out.print("나이는 양수로만 입력하세요.");
		else {
			int redCandle= age/10;
			int blueCandle= age%10/5;
			int yellowCandle= age%10%5;
			int total=redCandle+blueCandle+yellowCandle;
			
			System.out.print("빨간초 "+redCandle+"개,"+"파란초 "+blueCandle+"개,"+"노란초 "+
			yellowCandle+"개."+" 총 "+ total+"개가 필요합니다.");
		}
		sc.close();
	}
}

