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
import java.sql.ResultSet;

public class ViewBooksFrame extends Frame {
	
	ViewBooksFrame(){
		
		super("Library Management System");
		setLayout(null);
		setVisible(true);
		setSize(600, 600);
		
		// Top Label
		Label topLabel= new Label("View Books");
		topLabel.setBounds(50, 40, 500, 50);
		topLabel.setAlignment(Label.CENTER);
		topLabel.setFont(new Font("Arial", Font.BOLD, 24));
		add(topLabel);	
		
		// Name
		Label bookName= new Label("Book Name");
		bookName.setBounds(70, 120, 100, 30);
		add(bookName);
		
		TextField bookNameTextField = new TextField();
		bookNameTextField.setBounds(200, 120, 150, 30);
		add(bookNameTextField);
		
		//view book button
		Button viewBookButton= new Button("View Book");
		viewBookButton.setBounds(130, 200, 130, 30);
		add(viewBookButton);
		
		//result label
		Label resultLabel= new Label();
		resultLabel.setBounds(70, 280, 500, 30);
		resultLabel.setFont(new Font("Avant Garde", Font.BOLD, 12));
		add(resultLabel);		
		
		// view book button's action listener
		class ViewBookActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					
					Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
					
					String viewBook= bookNameTextField.getText();
					
					PreparedStatement psdb= conn.prepareStatement("select * from Librarybooks where Book_Name = ?");
					
					psdb.setString(1, viewBook);
					
					ResultSet rs = psdb.executeQuery();
					
					if(rs.next()) {
						
						resultLabel.setText("Book Code:"+rs.getString("book_code")+" Book Name:"+rs.getString("Book_Name")+" Author Name:"+rs.getString("Author_Name"));	
					
					}else {
						
						resultLabel.setText("No recorde found!!");
					}
					
				}catch(Exception h) {
					
					h.printStackTrace();
				
				}
				
			}
			
		}
		
		addWindowListener(new ViewBooksWindowListener(this) );
		
		viewBookButton.addActionListener(new ViewBookActionListener());
	}
	

}

class ViewBooksWindowListener extends WindowAdapter{
	
	Frame f;
	
	ViewBooksWindowListener(Frame f){
		
		this.f= f;
	}
	
public void windowClosing(WindowEvent e) {
		
		f.dispose();
		
	}
}
