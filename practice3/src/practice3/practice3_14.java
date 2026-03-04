package practice3;
import java.util.Scanner;
import java.util.Random;
public class practice3_14 {

	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);
		Random rand= new Random();
		
		System.out.println("***** 갬블링 게임을 시작합니다. *****");
		while(true) {
			System.out.print("엔터키 입력>>");
			sc.nextLine();
			
			int a=rand.nextInt(3);
			int b=rand.nextInt(3);
			int c=rand.nextInt(3);
			
			System.out.println(a+" "+ b+" "+c);
			
			if(a==b && b==c) {
				System.out.println("성공! 대박났어요!");
				System.out.print("계속하시겠습니까?(yes/no)");
				String choose=sc.nextLine();
				if(choose.equals("yes")) {
					continue;
				}
				else if(choose.equals("no")) {
					System.out.println("게임을 종료합니다.");
					sc.close();
					return;
				}
				else
					System.out.println("잘못입력했습니다.");
			}
		}
		
	}
}
