
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

class Phone{
   private String name;
   private  String tel;
   
   Phone(String name, String tel){
      this.name=name;
      this.tel=tel;
   }
   String getName() {
      return name;
   } 
   String getTel() {
      return tel;
   }
}

public class PhoneBook extends JFrame {
	private Scanner sc;
	private Phone[] pArray;
	private int lineY = 60;
	
	public PhoneBook() {
		sc= new Scanner(System.in);
		
		setTitle("전화번호부 결과");
		setSize(400,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	 //입력
	void input() {
	    System.out.print("입력 인원수:");
	    int n=sc.nextInt();
	    //객체 배열 생성
	    pArray=new Phone[n];
	      
	    for(int i=0; i<pArray.length; i++) {
	       System.out.print("이름과 전화번호 입력>> ");
	       String name=sc.next();
	       String tel=sc.next();
	       
	       pArray[i]=new Phone(name,tel);
	    }
	 }
	      
	   //검색
	 String search(String name) {
		 if(pArray==null) {
			 return null;
		 }
		 for(int i=0; i<pArray.length; i++) {
			 if(pArray[i].getName().equals(name)) {
				 return pArray[i].getTel();
			 }
		 }
		 return null;
	 }

	   
	   
	   // 출력
	 void run() {
	    System.out.println("전화번호부 관리 프로그램");
	    input();
	    
	    while(true) {
	       System.out.print("검색할 이름을 입력>>");
	       String name=sc.next();
	       
	       Graphics g = getGraphics();
	       g.setFont(new Font("맑은 고딕", Font.PLAIN,16));
	       
	       if (name.equals("그만")) {
	        	  g.drawString("프로그램을 종료합니다.", 30 , lineY);
	        	  break;
	       }
	         
	       String tel=search(name);
	       
	       if(tel != null) {
	    	      g.drawString(name+ "의 번호는 "+ tel + "입니다.", 30 , lineY);
	       }
	       else {
	    	      g.drawString(name+ "이가 없습니다.", 30 , lineY);
	       }
	       lineY += 20;
	     
	    }
	 }
	   
	 public static void main(String[] args) {
		 PhoneBook app= new PhoneBook();
		 app.run();
	      
	 }

}