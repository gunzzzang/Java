package practice3;
import java.util.Random;
import java.util.Scanner;
public class practice3_10{

	public static void main(String[] args) {
		
		Scanner sc=new Scanner(System.in);
		Random rand=new Random();
		
		System.out.println("4x4 배열에 랜덤한 값을 저장한 후 출력합니다.");
		int intArray[][]=new int[4][4];
		
		for(int i=0; i<intArray.length; i++) {
			for(int j=0; j<intArray[i].length; j++) {
				int r=rand.nextInt(256);
				intArray[i][j]=r;
				System.out.print(r+"\t");
				
			}
			System.out.println();
		}
		
		System.out.print("임계값 입력>>");
		int threshold= sc.nextInt(); // 임계값
		
		for(int i=0; i<intArray.length; i++) {
			for(int j=0; j<intArray[i].length; j++) {
				if(intArray[i][j]>threshold) {
					intArray[i][j]=255;
				}
				else
					intArray[i][j]=0;
				System.out.print(intArray[i][j]+"\t");
			}
			System.out.println();
		}
		sc.close();
	}
}