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

public class IssueBooksFrame extends Frame {
	
	IssueBooksFrame(){
		
		super("Library Management System");
		setLayout(null);
		setVisible(true);
		setSize(600, 600);
		
		// Top Label
		Label topLabel= new Label("Issue Books");
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
		
		// Issue book button
		Button issueBookButton= new Button("issue Book");
		issueBookButton.setBounds(130, 350, 130, 30);
		add(issueBookButton);
		
		//result label
		Label resultLabel= new Label();
		resultLabel.setBounds(70, 420, 500, 30);
		resultLabel.setFont(new Font("Avant Garde", Font.BOLD, 12));
		add(resultLabel);
		
		class IssueBookActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "root");
					
					String issueBookName= bookNameTextField.getText();
					
					String authorName= authorNameTextField.getText();
					
					String bookCode= bookCodeTextField.getText();
					
					PreparedStatement psdb= conn.prepareStatement("select * from Librarybooks where Book_Name = ?");
					
					psdb.setString(1, issueBookName);
					
					ResultSet rs= psdb.executeQuery();
					
					if(rs.next()) {
						
						resultLabel.setText(rs.getString("Book_Name")+" has been issued");
						
						PreparedStatement psdbdelete= conn.prepareStatement("delete from Librarybooks where Book_Name = ?");
						
						psdbdelete.setString(1, issueBookName);
						
						int i= psdbdelete.executeUpdate();
						
						PreparedStatement psdbissue= conn.prepareStatement("insert into Issuedbooks values(?,?,?) ");
						
						psdbissue.setString(1, bookCode);
						psdbissue.setString(2, issueBookName);
						psdbissue.setString(3, authorName);
						
						psdbissue.executeUpdate();
						
						
						
					}else {
						
						resultLabel.setText("No records found!");
					
					}
				
				}catch(Exception h) {
					
					h.printStackTrace();
				}
				
			}
			
		}
		
		addWindowListener(new IssueBooksWindowListener(this));
		
		issueBookButton.addActionListener(new IssueBookActionListener());
	}

}

class IssueBooksWindowListener extends WindowAdapter{
	
	Frame f;
	
	IssueBooksWindowListener(Frame f){
		
			this.f= f;
	}
	
public void windowClosing(WindowEvent e) {
		
		f.dispose();
		
	}
}
