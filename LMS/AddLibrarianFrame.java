package LMS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddLibrarianFrame extends Frame {
	
	AddLibrarianFrame(){
		
		super("Library Management System");
		setLayout(null);
		setVisible(true);
		setSize(600, 600);
		
		// Top Label
		Label topLabel= new Label("Add Librarian");
		topLabel.setBounds(50, 40, 500, 50);
		topLabel.setAlignment(Label.CENTER);
		topLabel.setFont(new Font("Arial", Font.BOLD, 24));
		add(topLabel);		
		
		// Name
		Label name= new Label("Name");
		name.setBounds(70, 120, 60, 30);
		add(name);
		
		TextField nameTextField = new TextField();
		nameTextField.setBounds(150, 120, 150, 30);
		add(nameTextField);
		
		//Email
		Label email= new Label("E-mail");
		email.setBounds(70, 190, 60, 30);
		add(email);
		
		TextField emailTextField = new TextField();
		emailTextField.setBounds(150, 190, 150, 30);
		add(emailTextField);	
		
		//Age
		Label age= new Label("Age");
		age.setBounds(70, 260, 60, 30);
		add(age);
				
		TextField ageTextField = new TextField();
		ageTextField.setBounds(150, 260, 150, 30);
		add(ageTextField);
		
		//ID
		Label id= new Label("ID");
		id.setBounds(70, 330, 60, 30);
		add(id);
				
		TextField idTextField = new TextField();
		idTextField.setBounds(150, 330, 150, 30);
		add(idTextField);
		
		//Register Button
		Button registerButton= new Button("Register Librarian");
		registerButton.setBounds(100, 400, 130, 30);
		add(registerButton);	
		
		//register button's action listener
		class RegisterButtonActionListener implements ActionListener{
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
					
					String id= idTextField.getText();
					String name= nameTextField.getText();
					String email= emailTextField.getText();
					String age= ageTextField.getText();
					
					
					PreparedStatement psdb= conn.prepareStatement("insert into Librarian values(?,?,?,?)");
					psdb.setString(1, id);
					psdb.setString(2, name);
					psdb.setString(3, email);
					psdb.setString(4, age);
					
					int i=psdb.executeUpdate();
					System.out.println(i+" record(s) has been inserted into database");
				}catch(Exception o) {
					o.printStackTrace();
				}
				
			}
			
		}

		
		
		addWindowListener(new Frame3WindowListener(this) );
		registerButton.addActionListener(new RegisterButtonActionListener() );
		
	}

}

class AddLibrarianWindowListener extends WindowAdapter{
	
	Frame f;
	
	AddLibrarianWindowListener(Frame f){
		
		this.f= f;
	}
	
	public void windowClosing(WindowEvent e) {
		f.dispose();
		
	}
	
}

