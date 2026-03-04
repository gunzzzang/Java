package practice3;
import java.util.Scanner;
public class practice3_6 {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int intArray[]=new int[10];
		
		System.out.print("양의 정수 10개 입력>>");
		for(int i=0; i<intArray.length; i++) {
			intArray[i]=sc.nextInt();
		}
		System.out.print("자리수의 합이 9인 것은...");
		
		for(int i=0; i<intArray.length; i++) {
			int n=intArray[i];
			int sum=0;
			while(n>0) {
				sum=sum+n%10;
				n=n/10;
			}
			if(sum==9) {
				System.out.print(intArray[i]+" ");
			}
		}
		
	}
}
