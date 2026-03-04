package practice3;
import java.util.Scanner;
import java.util.InputMismatchException;
public class practice3_18 {

	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);
		int studentNum[]= new int[10];
		int score[]= new int[10];
		
		System.out.println("10명 학생의 학번과 점수 입력");
		for(int i=0; i<10; i++) {
			System.out.print((i+1)+">>");
			studentNum[i]=sc.nextInt();
			score[i]=sc.nextInt();
		}
		while(true) {
			try {
				boolean found=false;
				System.out.print("학번으로 검색: 1, 점수로 검색 : 2, 끝내려면 3>>");
				int choose=sc.nextInt();
				if(choose==1) {
					System.out.print("학번>>");
					int check=sc.nextInt();
					for(int i=0; i<studentNum.length; i++) {
						if(check ==studentNum[i]) {
							System.out.println(score[i]+" 점");
							found=true;
						}
					}
					if(!found) {
						System.out.println(check+"의 학생은 없습니다.");
					}
				}
				
				else if(choose==2) {
					System.out.print("점수>>");
					int check=sc.nextInt();
					System.out.print("점수가 "+check+"인 학생은 ");
					for(int i=0; i<score.length; i++) {
						if(check ==score[i]) {
							System.out.print(studentNum[i]+" ");
							found=true;
						}
					}
			
					if(!found) {
						System.out.println(" 없습니다.");
					}
					else {
						System.out.println(" 입니다.");
					}
				}
				
				else if(choose==3) {
					System.out.println("프로그램을 종료합니다.");
					sc.close();
					return;
				}
				
			}
			catch(InputMismatchException e){
				System.out.println("경고!! 정수를 입력하세요.");
				sc.nextLine();
			}
		}	
	}
}
