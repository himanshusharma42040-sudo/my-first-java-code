import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class StudentRegistrationDelete {
	public static void main(String[] args) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Frame f= new Frame("Student Registration Delete");
		f.setSize(750, 750);
		f.setLayout(null);
		f.setVisible(true);
		
		// Name
		Label name= new Label("Name");
		name.setBounds(50, 50, 60, 30);
		f.add(name);
		
		TextField nameTextField = new TextField();
		nameTextField.setBounds(120, 50, 150, 30);
		f.add(nameTextField);
		
		//Delete Button
		Button deleteButton= new Button("Delete");
		deleteButton.setBounds(180, 100, 60, 30);
		f.add(deleteButton);
		
		class DeleteWindowListener extends WindowAdapter{
			
			public void windowClosing(WindowEvent e) {
				
				f.dispose();
			}
		}
		
		class DeleteActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
					
					String name= nameTextField.getText();
					
					PreparedStatement psdelete= conn.prepareStatement("DELETE FROM studentgui \r\n"
							+ "WHERE name=?;");
					
					psdelete.setString(1, name);
					
					int i=psdelete.executeUpdate();
					System.out.println(i);
				
				}catch(Exception o) {
					o.printStackTrace();
				}
				
			}
			
		}
		
		f.addWindowListener(new DeleteWindowListener());
		deleteButton.addActionListener(new DeleteActionListener() );
	}

}
