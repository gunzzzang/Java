
import java.util.Scanner;
public class practice2_4 {

	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);
		
		System.out.print("여행지>>");
		String travel= sc.nextLine();
		
		System.out.print("인원수>>");
		int people=sc.nextInt();
		
		System.out.print("숙박일>>");
		int days=sc.nextInt();
		
		System.out.print("1인당 항공료>>");
		int perTicket = sc.nextInt();
		
		System.out.print("1방 숙박비>>");
		int perHotel=sc.nextInt();
		
		int roomCount=0;
		
		if(people<=2)
			roomCount=1;
		else if(people>2 && people<=4)
			roomCount=2;
		else if(people >4 && people<=6)
			roomCount=3;
		else
			roomCount=(people/2)+1;
		
		int total=people*perTicket+days*perHotel*roomCount;
		System.out.println(people+"명의"+travel + days+"박"+ (days+1)+"일 여행에는 방이 "+roomCount+"개 필요하며 경비는 " +total+"입니다.");
		sc.close();
	}

}
