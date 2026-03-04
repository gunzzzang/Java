
import java.util.Scanner;
public class practice2_7 {

	public static void main(String[] args) {
		System.out.print("월을 입력하세요(1~12)>>");
		Scanner sc=new Scanner(System.in);
		int month=sc.nextInt();
		
		if(month>=3 && month<=5) {
			System.out.print("따뜻한 봄");
		}
		else if(month>=6 && month<=8) {
			System.out.print("바다가 즐거운 여름");
		}
		else if(month>=9 && month<=11) {
			System.out.print("낙엽이 지는 아름다운 가을");
		}
		else if(month==12 || month<=2){
			System.out.print("눈 내리는 하얀 겨울");
		}
		else {
			System.out.print("1~12만 입력하세요.");
		}
		sc.close();
	}
}