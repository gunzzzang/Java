package practice3;
import java.util.Scanner;
import java.util.InputMismatchException;

public class practice3_15 {

	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);
		while(true) {
			System.out.print("곱하고자 하는 정수 2개 입력>>");
			try {
				int n=sc.nextInt();
				int m=sc.nextInt();
				System.out.print(n+ "x" + m + "=" + n*m);
				break;
			} 
			catch(InputMismatchException e){
				System.out.println("정수를 입력하세요!");
				sc.nextLine(); // 버퍼 비우기
				continue;
			}
		}
		sc.close();
		
	}

}
