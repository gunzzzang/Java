import java.util.Scanner;
public class practice2_9 {

	public static void main(String[] args) {
		System.out.print("점 (x,y)의 좌표 입력>>");
		Scanner sc=new Scanner(System.in);
		int x= sc.nextInt();
		int y= sc.nextInt();
		
		if((x>10 && x<200) && (y>10 && y<300)) {
			System.out.print("(" + x + ","+ y + ")는 사각형 안에 있습니다.");
		}
		else if((x<10 || x>200) || (y<10 || y>300)) {
			System.out.print("(" + x + ","+ y + ")는 사각형 밖에 있습니다.");
		}
		else if((x==10 || x==200) && (y>=10 && y<=300) || (x>=10 && x<=200) && (y==10 || y==300)){
			System.out.print("(" + x + ","+ y + ")는 사각형 선 상에 있습니다.");
		}
		sc.close();
	}
}
