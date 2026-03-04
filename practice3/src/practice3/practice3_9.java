package practice3;
import java.util.Random;
public class practice3_9 {

	public static void main(String[] args) {
		System.out.println("4x4 배열에 랜덤한 값을 저장한 후 출력합니다.");
		Random rand=new Random();
		int intArray[][]=new int[4][4];
		
		for(int i=0; i<intArray.length; i++) {
			for(int j=0; j<intArray[i].length; j++) {
				int r=rand.nextInt(256);
				System.out.print(r+"\t");
			}
			System.out.println();
		}

	}
}
