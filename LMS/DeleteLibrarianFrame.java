package LMS;

import java.awt.Button;
import java.awt.Font;
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

public class DeleteLibrarianFrame extends Frame {
	
	DeleteLibrarianFrame(){
		
		super("Library Management System");
		setLayout(null);
		setVisible(true);
		setSize(600, 600);
		
		// Top Label
		Label topLabel= new Label("Delete Librarian");
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
		
		//delete librarian
		Button deleteButton= new Button("Delete Librarian");
		deleteButton.setBounds(150, 220, 130, 30);
		add(deleteButton);
		
		//result label
		Label resultLabel= new Label();
		resultLabel.setBounds(70, 280, 400, 30);
		resultLabel.setFont(new Font("Avant Garde", Font.BOLD, 12));
		add(resultLabel);		
		
		class DeleteActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
					
					String id= idTextField.getText();
					
					PreparedStatement psdb= conn.prepareStatement("delete from Librarian where id= ?");
					
					psdb.setString(1, id);
					
					int i= psdb.executeUpdate();
					
					if(i != 0) {
						
						resultLabel.setText(i+" Librarian Deleted from database");
					}else {
						resultLabel.setText("No records Found");
					}
					
				}catch(Exception h) {
					h.printStackTrace();
				}
				
			}
			
		}
		
		addWindowListener(new DeleteLibrarianWindowListener(this));
		
		deleteButton.addActionListener(new DeleteActionListener() );
	}

}
class DeleteLibrarianWindowListener extends WindowAdapter{
	
	Frame f;
	
	DeleteLibrarianWindowListener(Frame f){
		
		this.f= f;
		
	}
	
	public void windowClosing(WindowEvent e) {
		
		f.dispose();
		
	}
	
}
