package practice3;
import java.util.Scanner;
public class practice3_5 {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int intArray[]=new int[10];
		System.out.print("양의 정수 10개 입력>>");
		for(int i=0; i<intArray.length; i++) {
			intArray[i]=sc.nextInt();
		}
		System.out.print("3의 배수는...");
		for(int i=0; i<intArray.length; i++) {
			if(intArray[i]%3==0) {
				System.out.print(intArray[i]+" ");
			}
		}
		sc.close();
	}
}
