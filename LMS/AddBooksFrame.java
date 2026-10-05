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

public class AddBooksFrame extends Frame {
	
	AddBooksFrame() {
		
		super("Library Management System");
		setLayout(null);
		setVisible(true);
		setSize(600, 600);
		
		// Top Label
		Label topLabel= new Label("Add Books");
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
		
		//Author name
		Label authorName= new Label("Author Name");
		authorName.setBounds(70, 190, 100, 30);
		add(authorName);
		
		TextField authorNameTextField = new TextField();
		authorNameTextField.setBounds(200, 190, 150, 30);
		add(authorNameTextField);
		
		//Book code
		Label bookCode= new Label("Book Code");
		bookCode.setBounds(70, 260, 100, 30);
		add(bookCode);
				
		TextField bookCodeTextField = new TextField();
		bookCodeTextField.setBounds(200, 260, 150, 30);
		add(bookCodeTextField);
		
		//Add book Button
		Button addBookButton= new Button("Add Book");
		addBookButton.setBounds(130, 350, 130, 30);
		add(addBookButton);	
		
		//result label
		Label resultLabel= new Label();
		resultLabel.setBounds(70, 420, 400, 30);
		resultLabel.setFont(new Font("Avant Garde", Font.BOLD, 12));
		add(resultLabel);			
		
		// Add Book button's listener 
		class AddBookActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					
					Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
					
					String bookCode= bookCodeTextField.getText();
					String bookName= bookNameTextField.getText();
					String authorName= authorNameTextField.getText();
					
					PreparedStatement psdb= conn.prepareStatement("insert into Librarybooks values(?,?,?)");
					
					psdb.setString(1, bookCode);
					psdb.setString(2, bookName);
					psdb.setString(3, authorName);
					
					int i=psdb.executeUpdate();
					
					if(i != 0) {
						resultLabel.setText(i+" book(s) updated in database");
					}else {
						resultLabel.setText("Book add failed");
					}
					
				}catch(Exception h) {
					
					h.printStackTrace();
					
				}
			}
			
		}
		
		addWindowListener(new AddBooksWindowListener(this) );
		
		addBookButton.addActionListener(new AddBookActionListener());
		
	}
	
	

}

class AddBooksWindowListener extends WindowAdapter{
	
	Frame f;
	
	AddBooksWindowListener(Frame f){
		
		this.f= f;
		
	}
	
public void windowClosing(WindowEvent e) {
		
		f.dispose();
		
	}
	
}
