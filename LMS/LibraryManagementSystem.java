package LMS;



public class LibraryManagementSystem {
	public static void main(String[] args) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		new Frame1Library();
		
	}

}
