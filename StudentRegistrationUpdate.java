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

public class StudentRegistrationUpdate {
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
		
		// Age 
		Label age= new Label("Age");
		age.setBounds(50, 120, 60, 30);
		f.add(age);
		
		TextField ageTextField = new TextField();
		ageTextField.setBounds(120, 120, 150, 30);
		f.add(ageTextField);		
		
		//Update Button
		Button updateButton= new Button("Update Student");
		updateButton.setBounds(180, 200, 120, 30);
		f.add(updateButton);
		
		class UpdateActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
					
					String name= nameTextField.getText();
					String age= ageTextField.getText();
					
					PreparedStatement psUpdate= conn.prepareStatement("update studentgui set age = ? where name = ?");
					
					psUpdate.setString(1, age);
					psUpdate.setString(2, name);
					
					int i=psUpdate.executeUpdate();
					System.out.println(i);
					
				}catch(Exception h) {
					h.printStackTrace();
				}
				
			}
			
		}
		
		class UpdateWindowListener extends WindowAdapter{
			
			public void windowClosing(WindowEvent e) {
				
				f.dispose();
			}
		}
		
		f.addWindowListener(new UpdateWindowListener());
		updateButton.addActionListener(new UpdateActionListener() );
	}

}
