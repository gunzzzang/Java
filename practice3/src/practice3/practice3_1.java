package practice3;
import java.util.Scanner;
public class practice3_1 {
	
	public static void main(String[] args) {
		int sum=0;
		int i=1;
		do {
			sum+=i;
			i+=3;
		}while(i<50);
		System.out.println(sum);
	}
}
