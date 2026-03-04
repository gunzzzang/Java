import java.util.Scanner;
public class practice2_8 {

	public static void main(String[] args) {
		System.out.print("연산 입력>>");
		Scanner sc=new Scanner(System.in);
		
		double a=sc.nextDouble();
		String op=sc.next();
		double b=sc.nextDouble();
		
		double total=0;
		if(op.equals("곱하기")) {
			total=a*b;
		}
		else if(op.equals("더하기")) {
			total=a+b;
		}
		else if(op.equals("빼기")) {
			total=a-b;
		}
		else if(op.equals("나누기")) {
			total=a/b;
		}
		else {
			System.out.print("사칙연산이 아닙니다.");
		}
		System.out.print(a + op+ b +"의 계산 결과는 "+ total);
		sc.close();
	}
}
