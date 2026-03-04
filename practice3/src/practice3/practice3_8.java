package practice3;
import java.util.Random;
import java.util.Scanner;
public class practice3_8 {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		Random rand=new Random();
		
		System.out.print("정수 몇개 저장하시겠습니까>>");
		int count=sc.nextInt();
		int sum=0;
		float avg=0;
		System.out.print("랜덤한 정수들...");
		for(int i=0; i<count; i++) {
			int r=rand.nextInt(100)+1; // 1~100까지 생성
			System.out.print(r+" ");
			sum+=r;
		}
		avg=(float)sum/count;
		System.out.println();
		System.out.print("평균은 "+ avg);
		sc.close();
	}
}