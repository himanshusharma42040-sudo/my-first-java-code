package LMS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Frame3Library extends Frame {
	
	Frame3Library(){
		
		super("Library Management System");
		setLayout(null);
		setVisible(true);
		setSize(600, 600);
		
		// Top Label
		Label topLabel= new Label("Librarian Section");
		topLabel.setBounds(50, 40, 500, 50);
		topLabel.setAlignment(Label.CENTER);
		topLabel.setFont(new Font("Arial", Font.BOLD, 24));
		add(topLabel);	
		
		//Add Books Button
		Button addBooks= new Button("Add Books");
		addBooks.setBounds(70, 100, 150, 30);
		add(addBooks);	
		
		//view Books Button
		Button viewBooks= new Button("View Books");
		viewBooks.setBounds(70, 150, 150, 30);
		add(viewBooks);	
		
		//issue Books Button
		Button issueBooks= new Button("Issue Books");
		issueBooks.setBounds(70, 200, 150, 30);
		add(issueBooks);
		
		//view issued Books Button
		Button viewIssuedBooks= new Button("View Issued Books");
		viewIssuedBooks.setBounds(70, 250, 150, 30);
		add(viewIssuedBooks);
		
		//return Books Button
		Button returnBooks= new Button("Return Book");
		returnBooks.setBounds(70, 300, 150, 30);
		add(returnBooks);
		
		//logout Button
		Button logoutButton = new Button("Logout");
		logoutButton.setBounds(70, 350, 150, 30);
		add(logoutButton);		
		
		addWindowListener(new Frame3WindowListener(this) );
		
		addBooks.addActionListener(new AddBooksActionListener(this));
		
		viewBooks.addActionListener(new ViewBooksActionListener(this));
		
		issueBooks.addActionListener(new IssueBooksActionListener(this));
		
		viewIssuedBooks.addActionListener(new ViewIssueBooksActionListener(this));
		
		returnBooks.addActionListener(new ReturnBooksActionListener(this));
	}

}

class Frame3WindowListener extends WindowAdapter{
	
	Frame f;
	
	Frame3WindowListener(Frame f){
		
		this.f= f;
	}
	
	public void windowClosing(WindowEvent e) {
		f.dispose();
		
	}
	
}

class AddBooksActionListener implements ActionListener{
	
	Frame f;
	
	AddBooksActionListener(Frame f){
		
		this.f= f;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		f.dispose();
		new AddBooksFrame();
	}
	
}

class ViewBooksActionListener implements ActionListener{
	
	Frame f;
	
	ViewBooksActionListener(Frame f){
		
		this.f= f;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		f.dispose();
		new ViewBooksFrame();
	}
	
}

class IssueBooksActionListener implements ActionListener{

	Frame f;
	
	IssueBooksActionListener(Frame f){
		
		this.f= f;
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		f.dispose();
		
		new IssueBooksFrame();
		
	}
	
}

class ViewIssueBooksActionListener implements ActionListener{

	Frame f;
	
	ViewIssueBooksActionListener(Frame f){
		
		this.f= f;
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		f.dispose();
		
		new ViewIssuedBooksFrame();
		
	}
	
}

class ReturnBooksActionListener implements ActionListener{
	
	Frame f;
	
	ReturnBooksActionListener(Frame f){
		
		this.f = f;
	
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		f.dispose();
		
		new ReturnBookFrame();
		
	}
	
}

