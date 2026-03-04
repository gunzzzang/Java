
import java.util.Scanner;

public class GawiBawiBo {



	public static void main(String[] args) {

		Scanner sc= new Scanner(System.in);

		System.out.println("가위/바위/보 중에서 입력하세요");

		System.out.print("철수>>");

		String c =sc.next();

		System.out.print("영희>>");

		String y = sc.next();

		

		if(c.equals("가위")) {

			if(y.equals("가위")) {

				System.out.println("비겼습니다");

			}

			else if(y.equals("바위")){

				System.out.println("철수가 이김");

			}

			else

				System.out.println("철수가 졌음");

		}

		else if(c.equals("바위")) {

			if(y.equals("가위")) {

				System.out.println("비겼습니다");

			}

			else if(y.equals("바위")){

				System.out.println("철수가 이김");

			}

			else

				System.out.println("철수가 졌음");

		

	}

	}

}

철수는 내가 짜고 영희는 컴퓨터가 랜덤으로 설정하게끔