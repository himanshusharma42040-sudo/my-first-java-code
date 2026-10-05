package LMS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Frame1Library extends Frame{
	
	Frame1Library(){
		
		super("Library Management System");
		setLayout(null);
		setVisible(true);
		setSize(600, 600);
		
		// Top Label
		Label topLabel= new Label("Library Management System");
		topLabel.setBounds(50, 40, 500, 50);
		topLabel.setAlignment(Label.CENTER);
		topLabel.setFont(new Font("Arial", Font.BOLD, 24));
		add(topLabel);
		
		//Admin login Button
		Button adminButton= new Button("Admin Login");
		adminButton.setBounds(70, 100, 150, 30);
		add(adminButton);
		
		//Librarian login Button
		Button librarianButton= new Button("Librarian Login");
		librarianButton.setBounds(70, 150, 150, 30);
		add(librarianButton);		
		
		class AdminLoginActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				new Frame2Library();
			}
			
		}
		
		class LibrarianLoginActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				new Frame3Library();
			}
			
		}
		
		adminButton.addActionListener(new AdminLoginActionListener() );
		
		librarianButton.addActionListener(new LibrarianLoginActionListener() );
		
		addWindowListener(new MyWindowListener(this));
	}
	
	
}
class MyWindowListener extends WindowAdapter{
	Frame f;
	
	MyWindowListener(Frame f){
		this.f= f;
	}
	
	public void windowClosing(WindowEvent e) {
		f.dispose();
		
	}
}
