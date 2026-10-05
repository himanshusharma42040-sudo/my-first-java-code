package LMS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Frame2Library extends Frame {
	
	Frame2Library(){
		
		super("Library Management System");
		setLayout(null);
		setVisible(true);
		setSize(600, 600);
		
		// Top Label
		Label topLabel= new Label("Admin Section");
		topLabel.setBounds(50, 40, 500, 50);
		topLabel.setAlignment(Label.CENTER);
		topLabel.setFont(new Font("Arial", Font.BOLD, 24));
		add(topLabel);
		
		//Add Librarian Button
		Button addLibrarian= new Button("Add Librarian");
		addLibrarian.setBounds(70, 100, 150, 30);
		add(addLibrarian);
		
		//view Librarian Button
		Button viewLibrarian= new Button("View Librarian");
		viewLibrarian.setBounds(70, 150, 150, 30);
		add(viewLibrarian);	
		
		//delete Librarian Button
		Button deleteLibrarian= new Button("Delete Librarian");
		deleteLibrarian.setBounds(70, 200, 150, 30);
		add(deleteLibrarian);			
		
		//logout Librarian Button
		Button logoutButton= new Button("Logout");
		logoutButton.setBounds(70, 250, 150, 30);
		add(logoutButton);	
		
		addWindowListener(new Frame2WindowListener(this) );
		
		addLibrarian.addActionListener(new AddLibrarianActionListener(this));
		
		viewLibrarian.addActionListener(new ViewLibrarianActionListener(this) );
		
		deleteLibrarian.addActionListener(new DeleteLibrarianActionListener(this));
	}
	

}

class Frame2WindowListener extends WindowAdapter{
	
	Frame f;
	
	Frame2WindowListener(Frame f){
		
		this.f= f;
	}
	
	public void windowClosing(WindowEvent e) {
		f.dispose();
		
	}
	
}

class AddLibrarianActionListener implements ActionListener{
	
	Frame f;

	AddLibrarianActionListener(Frame f){
		
		this.f= f;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		f.dispose();
		new AddLibrarianFrame();
	}
	
}

class ViewLibrarianActionListener implements ActionListener{
	
	Frame f;
	
	ViewLibrarianActionListener(Frame f){
		
		this.f= f;
		
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		f.dispose();
		
		new ViewLibrarianFrame();
	}
	
}

class DeleteLibrarianActionListener implements ActionListener{
	
	Frame f;
	
	DeleteLibrarianActionListener(Frame f){
		
		this.f= f;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		f.dispose();
		new DeleteLibrarianFrame();
	}
	
}
