package practice3;
import java.util.Scanner;
import java.util.InputMismatchException;
public class practice3_17 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		String coffee[] = {"핫아메리카노", "아이스아메리카노", "카푸치노", "라떼"};
		int price[] = {3000, 3500, 4000, 5000};
		int sellPrice=0;
		System.out.println("핫아메리카노, 아이스아메리카노, 카푸치노, 라떼 있습니다.");
		
		while(true) {
			System.out.print("주문>>");
			
			boolean found =false;
			try {
				String order=sc.next();
				if(order.equals("그만")) {
					break;
				}
				int count=sc.nextInt(); 
				
				for(int i=0; i<coffee.length; i++) {
					
					if(coffee[i].equals(order)) {
						sellPrice=price[i]*count;
						System.out.println("가격은 "+sellPrice+"원입니다.");
						found=true;
						break;
					}
				}
				if(!found) {
					System.out.println(order+"는 없는 메뉴입니다.");
				}
				
			}
			catch(InputMismatchException e){
				System.out.println("잔 수는 양의 정수로 입력해주세요!");
				sc.nextLine();
			}
		}
		sc.close();
	}
}
