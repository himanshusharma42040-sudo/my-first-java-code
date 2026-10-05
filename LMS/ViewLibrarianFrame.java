package LMS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewLibrarianFrame extends Frame {
	
	ViewLibrarianFrame(){
		
		super("Library Management System");
		setLayout(null);
		setVisible(true);
		setSize(600, 600);
		
		// Top Label
		Label topLabel= new Label("View Librarian");
		topLabel.setBounds(50, 40, 500, 50);
		topLabel.setAlignment(Label.CENTER);
		topLabel.setFont(new Font("Arial", Font.BOLD, 24));
		add(topLabel);
		
		//ID Label
		Label idLabel= new Label("Enter ID");
		idLabel.setBounds(70, 120, 60, 30);
		add(idLabel);
		
		TextField idTextField = new TextField();
		idTextField.setBounds(150, 120, 150, 30);
		add(idTextField);
		
		//fetch details
		Button fetchDetailsButton= new Button("Fetch Details");
		fetchDetailsButton.setBounds(150, 220, 130, 30);
		add(fetchDetailsButton);
		
		//result label
		Label resultLabel= new Label();
		resultLabel.setBounds(70, 280, 400, 30);
		resultLabel.setFont(new Font("Avant Garde", Font.BOLD, 12));
		add(resultLabel);
		
		class FetchButtonActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
						
					String id= idTextField.getText();
					
					PreparedStatement psdb= conn.prepareStatement("select * from Librarian where id= ?");
					
					psdb.setString(1, id);
					
					ResultSet rs= psdb.executeQuery();
					
					if(rs.next()) {
						
						resultLabel.setText("Id:"+rs.getString("id")+" Name:"+rs.getString("name")+" Email:"+rs.getString("email")+" Age:"+rs.getNString("age"));
					}else {
						resultLabel.setText("No records found!");
					}
					
				}catch(Exception o) {
					
					o.printStackTrace();
				}
			}
			
		}
				
		fetchDetailsButton.addActionListener(new FetchButtonActionListener());
		
		addWindowListener(new ViewLibrarianWindowListener(this) );
		
	}

}

class ViewLibrarianWindowListener extends WindowAdapter{
	
Frame f;
	
		ViewLibrarianWindowListener(Frame f){
		
		this.f= f;
	}
	
	public void windowClosing(WindowEvent e) {
		f.dispose();
		
	}
}
