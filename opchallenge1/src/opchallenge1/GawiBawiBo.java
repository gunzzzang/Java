package opchallenge1;

import java.util.Scanner;
import java.util.Random;

public class GawiBawiBo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("가위/바위/보 중에서 입력하세요");
        System.out.print("사용자>>");
        String player = sc.next();
    
        Random rand = new Random();
        String computer;
        int number = rand.nextInt(3);
        
        if (number == 0) {
            computer = "가위";
        } 
        else if (number == 1) {
            computer = "바위";
        } 
        else {
            computer = "보";
        }
        System.out.println("컴퓨터>>" + computer);

        if(player.equals(computer)) {
        	System.out.println("비겼습니다.");
        }
        	else if(player.equals("가위")) {
        		if(computer.equals("바위")) {
        			System.out.println("사용자가 졌습니다.");
        		}
        		else {
        			System.out.println("사용자가 이겼습니다.");
        		}
        }
        	else if(player.equals("바위")) {
        		if(computer.equals("보")) {
        			System.out.println("사용자가 졌습니다.");
        		}
        		else {
        			System.out.println("사용자가 이겼습니다.");
        		}
        }
        	else if(player.equals("보")) {
        		if(computer.equals("바위")) {
        			System.out.println("사용자가 졌습니다.");
        		}
        		else {
        			System.out.println("사용자가 이겼습니다.");
        		}
        }
        System.out.println("가위바위보 게임을 종료합니다.");
        sc.close();
    }
}
