
import java.util.Scanner;
public class practice2_10 {

	public static void main(String[] args) {
		System.out.print("(x1,y1), (x2,y2)의 좌표 입력>>");
		Scanner sc=new Scanner(System.in);
		int x1= sc.nextInt();
		int y1= sc.nextInt();
		int x2= sc.nextInt();
		int y2= sc.nextInt();
		
		System.out.print("("+x1+","+y1+")"+"("+x2+","+y2+")");
		if((x1>=10 && x1<=200) && (x2>=10 && x2<=200) && (y1>=10 && y1<=300) && (y2>=10 &&y2<=300)){
			System.out.print("사각형은 (10,10) (200,300) 사각형에 포함된다.");
		}
		else {
			System.out.print("사각형은 (10,10) (200,300) 사각형에 포함되지 않는다.");
		}
		sc.close();
	}
}