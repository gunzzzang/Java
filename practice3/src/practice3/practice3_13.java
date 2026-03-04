package practice3;
import java.util.Scanner;
public class practice3_13 {

	public static void main(String[] args) {
		
		String course [] = {"C", "C++", "Python", "Java", "HTML5" };
		String grade [] = {"A", "B+", "B", "A+", "D"};
		
		Scanner sc=new Scanner(System.in);
		
		while(true) {
			
			System.out.print("과목>>");
			String courseName=sc.next(); // 과목명
			
			if(courseName.equals("그만")) {
				break;
			}
			
			boolean found=false;
			for(int i=0; i<course.length; i++) {
				if(course[i].equals(courseName)) {
					System.out.println(courseName+" 학점은 "+ grade[i]);
					found=true;
					break;
				}	
			}
			if(!found) {
				System.out.println(courseName+"는 없는 과목 입니다.");
			}
		}
		sc.close();
	}
}
