package LMS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.*;


public class ViewIssuedBooksFrame extends Frame {
	
	ViewIssuedBooksFrame(){
		
		super("Library Management System");
		setLayout(null);
		setVisible(true);
		setSize(600, 600);
		
		// Top Label
		Label topLabel= new Label("View Issued Books");
		topLabel.setBounds(50, 40, 500, 50);
		topLabel.setAlignment(Label.CENTER);
		topLabel.setFont(new Font("Arial", Font.BOLD, 24));
		add(topLabel);
		
		// Book Name
		Label bookName= new Label("Book Name");
		bookName.setBounds(70, 120, 100, 30);
		add(bookName);
		
		TextField bookNameTextField = new TextField();
		bookNameTextField.setBounds(200, 120, 150, 30);
		add(bookNameTextField);
		
		//Book code
		Label bookCode= new Label("Book Code");
		bookCode.setBounds(70, 190, 100, 30);
		add(bookCode);
				
		TextField bookCodeTextField = new TextField();
		bookCodeTextField.setBounds(200, 190, 150, 30);
		add(bookCodeTextField);
		
		// View Issue book button
		Button viewIssuedBookButton= new Button("View Issued Book");
		viewIssuedBookButton.setBounds(130, 260, 130, 30);
		add(viewIssuedBookButton);
		
		//result label
		Label resultLabel= new Label();
		resultLabel.setBounds(70, 320, 500, 30);
		resultLabel.setFont(new Font("Avant Garde", Font.BOLD, 12));
		add(resultLabel);			
		
		class ViewIssuedBooks implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					
					Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
					
					String bookName = bookNameTextField.getText();
					
					String bookCode = bookCodeTextField.getText();
					
					PreparedStatement psdb= conn.prepareStatement("select * from issuedbooks where book_code = ? and Book_Name=?");
					
					psdb.setString(1, bookCode);
					
					psdb.setString(2, bookName);
					
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
		
		
		viewIssuedBookButton.addActionListener(new ViewIssuedBooks() );		
		
		addWindowListener(new ViewIssuedBooksWindowListener(this));
	}

}

class ViewIssuedBooksWindowListener extends WindowAdapter{
	
	Frame f;
	
	ViewIssuedBooksWindowListener(Frame f){
		
		this.f= f;
		
	}
	
public void windowClosing(WindowEvent e) {
		
		f.dispose();
		
	}
}
