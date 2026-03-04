
import java.util.Scanner;
public class practice2_2 {
	public static void main(String[] args) {
		System.out.print("생일 입력 하세요>>");
		
		Scanner sc=new Scanner(System.in);
		int birthday=sc.nextInt(); 
		
		int year=birthday/10000;
		int month=birthday%10000/100;
		int day=birthday%100;
		
		System.out.print(year+"년 "+month+"월 "+day+"일");
		sc.close();
	}
}
