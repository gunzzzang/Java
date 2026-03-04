
import java.util.Scanner;
public class practice2_3 {

	public static void main(String[] args) {
		System.out.println("**** 자바 분식입니다. 주문하면 금액을 알려드립니다. ****");
		Scanner sc= new Scanner(System.in);
		
		System.out.print("떡볶이 몇인분>>");
		int duk= sc.nextInt();
		System.out.print("김말이 몇인분>>");
		int kim=sc.nextInt();
		System.out.print("쫄면 몇인분>>");
		int nuddle=sc.nextInt();
		
		int sum=duk*2000 + kim*1000 + nuddle*3000;
		System.out.println("전체 금액은 "+sum+"원입니다.");
		
		sc.close();
	}

}
