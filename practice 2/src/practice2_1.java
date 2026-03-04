
import java.util.Scanner;
public class practice2_1 {

	public static void main(String[] args) {
		System.out.print("$1은=1200원입니다. 달러를 입력하세요>>");
		
		Scanner sc=new Scanner(System.in);
		
		int doller=sc.nextInt();
		int won= doller*1200;
		
		System.out.print("$"+doller+"은 "+won+" 입니다");
		sc.close();

	}

}
