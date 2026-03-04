package practice3;

public class practice3_2 {

	public static void main(String[] args) {
		int n[]= {1,-2,6,20,5,72,-16,256};
		for(int i=0; i<n.length; i++) {
			if(n[i]>0 && n[i]%4==0) {
				System.out.print(n[i]+" ");
			}
		}

	}

}

/* while문
package practice3;

public class practice3_2 {

	public static void main(String[] args) {
		int n[]= {1,-2,6,20,5,72,-16,256};
		int i=0;
		while(i<n.length) {
			if(n[i]>0 && n[i]%4==0) {
				System.out.print(n[i]+ " ");
			}
			i++;
		}

	}

}
*/

