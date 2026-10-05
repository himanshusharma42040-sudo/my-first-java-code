package LMS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class ReturnBookFrame extends Frame {
	
	ReturnBookFrame(){
		
		super("Library Management System");
		setLayout(null);
		setVisible(true);
		setSize(600, 600);
		
		// Top Label
		Label topLabel= new Label("Return Books");
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
		
		//return book Button
		Button returnBookButton= new Button("Return Book");
		returnBookButton.setBounds(130, 350, 130, 30);
		add(returnBookButton);
		
		//result label
		Label resultLabel= new Label();
		resultLabel.setBounds(70, 420, 500, 30);
		resultLabel.setFont(new Font("Avant Garde", Font.BOLD, 12));
		add(resultLabel);
		
		class ReturnBookActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					
					Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
					
					String bookName = bookNameTextField.getText();
					
					String authorName = authorNameTextField.getText();
					
					String bookCode = bookCodeTextField.getText();
					
					PreparedStatement psdb= conn.prepareStatement("insert into Librarybooks values(?,?,?)");
					
					psdb.setString(1, bookCode);
					psdb.setString(2, bookName);
					psdb.setString(3, authorName);
					
					int i=psdb.executeUpdate();
					
					if(i != 0) {
						resultLabel.setText(i+" book(s) updated in library");
						
						PreparedStatement psdbdelete= conn.prepareStatement("delete from issuedbooks where book_code = ?");
						
						psdbdelete.setString(1,bookCode);
						
						psdbdelete.executeUpdate();
						
					}else {
						resultLabel.setText("Book return failed");
					}
				
				}catch(Exception h) {
					
					h.printStackTrace();
				
				}
				
			}
			
		}
				
		
		addWindowListener(new ReturnBookFrameWindowListener(this) );
		
		returnBookButton.addActionListener(new ReturnBookActionListener());
		
	}

}

class ReturnBookFrameWindowListener extends WindowAdapter{
	
	Frame f;
	
	ReturnBookFrameWindowListener(Frame f){
		
		this.f = f;
	}
	
	
public void windowClosing(WindowEvent e) {
		
		f.dispose();
		
	}
}
