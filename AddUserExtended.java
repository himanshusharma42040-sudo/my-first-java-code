import java.awt.Button;

import java.awt.Frame;

import java.awt.Label;

import java.awt.TextField;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import java.awt.event.WindowListener;



public class AddUserExtended {

	 static int a=10;

	public static void main(String[] args) {

		Frame f = new Frame("Add User Screen");



		



		// Name

		Label nameLabel = new Label("Name:");

		nameLabel.setBounds(80, 100, 100, 30);

		f.add(nameLabel);



		TextField nameField = new TextField();

		nameField.setBounds(180, 100, 220, 30);

		f.add(nameField);



		// Email

		Label emailLabel = new Label("Email:");

		emailLabel.setBounds(80, 150, 100, 30);

		f.add(emailLabel);



		TextField emailField = new TextField();

		emailField.setBounds(180, 150, 220, 30);

		f.add(emailField);



		// Button

		Button addButton = new Button("Add User");

		addButton.setBounds(180, 210, 120, 40);

		f.add(addButton);

		

		Button resetButton = new Button("Reset data");

		resetButton.setBounds(180, 280, 120, 40);

		f.add(resetButton);

		
		
		f.setLayout(null);

		f.setSize(500, 500);

		f.setVisible(true);

		

		class MyWindowListner2 extends WindowAdapter{

			

			@Override

			public void windowClosing(WindowEvent e) {

				// TODO Auto-generated method stub

				f.dispose();

				

			}

		} 

		class BtnActionListner implements ActionListener {



			@Override

			public void actionPerformed(ActionEvent e) {

				// TODO Auto-generated method stub

				System.out.println("Clicked on the button");

				System.out.println(nameField.getText());

				System.out.println(emailField.getText());

				

			}

			

		}

		class ResetActionListner implements ActionListener{



			@Override

			public void actionPerformed(ActionEvent e) {

				// TODO Auto-generated method stub

				System.out.println("Clicked on the reset button");

				

			}

			

		}

		addButton.addActionListener(new BtnActionListner());

		resetButton.addActionListener(new ResetActionListner());

		f.addWindowListener(new MyWindowListner2());

		

	}

}



class MyWindowListner implements WindowListener{

	private Frame f;

	public MyWindowListner(Frame f,TextField tf1, TextField tf2) {

		this.f=f;

		

	}

	@Override

	public void windowOpened(WindowEvent e) {

		// TODO Auto-generated method stub

		System.out.println("windowOpened");

	}



	@Override

	public void windowClosing(WindowEvent e) {

		// TODO Auto-generated method stub

		System.out.println("Listner created using dircly interface");

		f.dispose();

		

	}



	@Override

	public void windowClosed(WindowEvent e) {

		// TODO Auto-generated method stub

		

		System.out.println("windowClosed");

	}



	@Override

	public void windowIconified(WindowEvent e) {

		// TODO Auto-generated method stub

		System.out.println("windowIconified");

	}



	@Override

	public void windowDeiconified(WindowEvent e) {

		// TODO Auto-generated method stub

		

	}



	@Override

	public void windowActivated(WindowEvent e) {

		// TODO Auto-generated method stub

		

	}



	@Override

	public void windowDeactivated(WindowEvent e) {

		// TODO Auto-generated method stub

		

	}

	

}

