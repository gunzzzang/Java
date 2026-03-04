package practice3;
import java.util.Scanner;
public class practice3_3 {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int plus;
		while(true) {
			System.out.print("양의 정수 입력>>");
			plus=sc.nextInt();
			if(plus>0) 
				break;
			}
		for(int i=0; i<plus; i++) {
			for(int j=0; j<plus-i; j++) {
				System.out.print("*");
			}
			System.out.println();
		}
	
	}
}
