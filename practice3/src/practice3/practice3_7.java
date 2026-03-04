package practice3;
import java.util.Random;
import java.util.Scanner;
public class practice3_7 {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		Random rand=new Random();
		System.out.print("랜덤한 정수들...");
		int sum=0;
		
		for(int i=0; i<10; i++) {
			int num= rand.nextInt(9)+11; // 0~8 +11  11부터 19
			System.out.print(num+ " ");
			sum+=num;
		}
		System.out.println();
		System.out.print("평균은 " +(double)sum/10);

	}

}
