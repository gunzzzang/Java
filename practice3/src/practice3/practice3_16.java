package practice3;
import java.util.Scanner;

public class practice3_16 {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int sum=0, count=0;
		System.out.print("양의 정수를 입력하세요. -1은 입력 끝>>");
		while(true) {
			
			String input=sc.next();
			try {
				int n= Integer.parseInt(input);
				if(n==-1) {
					break;
				}
				if(n>0) {
					sum+=n;
					count++;
				}
				else {
					System.out.println(n+" 제외");
				}
			}
			catch(NumberFormatException e) {
				System.out.println(input+" 제외");
				
			}
		}
		if(count>0) {
			System.out.println("평균은 "+sum/count);
		}
		else {
			System.out.println("양의정수가 0개입니다.");
		}
		sc.close();	
	}
}
