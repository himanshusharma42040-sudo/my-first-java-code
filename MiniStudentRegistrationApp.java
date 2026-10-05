import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MiniStudentRegistrationApp {
	public static void main(String[] args) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Frame f= new Frame("Mini Student Registration App");
		f.setSize(500, 500);
		f.setLayout(null);
		f.setVisible(true);
		
		// Name
		Label name= new Label("Name");
		name.setBounds(50, 50, 60, 30);
		f.add(name);
		
		TextField nameTextField = new TextField();
		nameTextField.setBounds(120, 50, 150, 30);
		f.add(nameTextField);
		
		//Email
		Label email= new Label("E-mail");
		email.setBounds(50, 120, 60, 30);
		f.add(email);
		
		TextField emailTextField = new TextField();
		emailTextField.setBounds(120, 120, 150, 30);
		f.add(emailTextField);
		
		//Age
		Label age= new Label("Age");
		age.setBounds(50, 190, 60, 30);
		f.add(age);
				
		TextField ageTextField = new TextField();
		ageTextField.setBounds(120, 190, 150, 30);
		f.add(ageTextField);
		
		//Register Button
		Button registerButton= new Button("Register");
		registerButton.setBounds(180, 400, 60, 30);
		f.add(registerButton);
		
		//clear Button
		Button clearButton= new Button("Clear");
		clearButton.setBounds(50, 400, 60, 30);
		f.add(clearButton);
		
		//Gender Label
		Label gender= new Label("Gender");
		gender.setBounds(50, 260, 60, 30);
		f.add(gender);
		
		//Check boxes
		CheckboxGroup genderGroup= new CheckboxGroup();
		
		Checkbox male= new Checkbox("Male", genderGroup, false);
		male.setBounds(120,260, 80, 30);
		f.add(male);
		
		Checkbox female= new Checkbox("Female", genderGroup, false);
		female.setBounds(200, 260, 80, 30);
		f.add(female);
		
		//course label
		Label course= new Label("Course");
		course.setBounds(50,330, 60, 30);
		f.add(course);
		
		//drop down
		Choice courseChoice= new Choice();
		courseChoice.add("Select course");
		courseChoice.add("Java");
		courseChoice.add("Python");
		courseChoice.add("Sql");
		courseChoice.add("C++");
		
		courseChoice.setBounds(120, 330, 150, 50);
		f.add(courseChoice);
		
		//Status label
		Label statusLabel= new Label();
		statusLabel.setBounds(50, 500, 200, 50);
		f.add(statusLabel);
		
		
		class RegisterActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("register button clicked");
				
				try {
					Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
					//System.out.println("database connected");
					
					String name= nameTextField.getText();
					String Email= emailTextField.getText();
					String age= ageTextField.getText();
					Checkbox selected= genderGroup.getSelectedCheckbox();
					String gender= selected.getLabel();
					String course= courseChoice.getSelectedItem();
					
					if(name.isEmpty()) {
						statusLabel.setText("please enter name");
					}else if(Email.isEmpty()) {
						statusLabel.setText("please enter email");
					}else if(age.isEmpty()) {
						statusLabel.setText("please enter age");
					}else if(genderGroup.getSelectedCheckbox() == null) {
						statusLabel.setText("please select gender");
					}else if(courseChoice.getSelectedItem().equals("Select course")) {
						statusLabel.setText("please select a course");
					}
					
					PreparedStatement psdb= conn.prepareStatement("insert into studentgui values(?,?,?,?,?)");
					psdb.setString(1, name);
					psdb.setString(2, Email);
					psdb.setString(3, age);
					psdb.setString(4, gender);
					psdb.setString(5, course);
					
					int i=psdb.executeUpdate();
					System.out.println(i+" record(s) has been inserted into database");
				
				}catch(Exception d) {
					d.printStackTrace();
				}
				
			}
			
		}
		
		class ClearActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				nameTextField.setText("");
				emailTextField.setText("");
				ageTextField.setText("");
				male.setState(false);
				female.setState(false);
				courseChoice.select(0);
			}
			
		}
		
		
		class StudentRegistrationApp extends WindowAdapter{
			
			public void windowClosing(WindowEvent e) {
				
				f.dispose();
			}
		}
		
		f.addWindowListener(new StudentRegistrationApp());
		registerButton.addActionListener(new RegisterActionListener());
		clearButton.addActionListener(new ClearActionListener());
	}

}
