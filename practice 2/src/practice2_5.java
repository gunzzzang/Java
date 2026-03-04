
import java.util.Scanner;
public class practice2_5 {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		
		System.out.print("학생1>>");
		String student1=sc.next();
		
		int late1=sc.nextInt();
		int absence1=sc.nextInt();
		
		int penaltyScore1=late1*3+absence1*8;
		int Score1=100-penaltyScore1;
		
		System.out.print("학생2>>");
		String student2=sc.next();
		
		int late2=sc.nextInt();
		int absence2=sc.nextInt();
		
		int penaltyScore2=late2*3+absence2*8;
		int Score2=100-penaltyScore2;
		
		System.out.println(student1+"의 감점은 "+penaltyScore1+", "+ student2+"의 감점은 "+penaltyScore2);
		if(Score1==Score2) {
			System.out.print("점수 동일");
		}
		else if (Score1>Score2) {
			System.out.print(student1+"의 출석 점수가 더 높음, "+ student1 +"출석 점수는 "+Score1 );
		}
		else if (Score1<Score2) {
			System.out.print(student2+"의 출석 점수가 더 높음, "+ student2 +" 출석 점수는 "+Score2 );
		}
		sc.close();

	}

}
