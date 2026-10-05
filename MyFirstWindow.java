

import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;

public class MyFirstWindow {
	public static void main(String[] args) {
		
		Frame f= new Frame("My first window");
		
		/*
		 * code for name label
		 */
	Label nameLabel= new Label("Name : ");
	nameLabel.setBounds(50, 40, 100, 30);
	f.add(nameLabel);
	
	/*
	 * code for textbox
	 */
	TextField nameTextField= new TextField("Enter your name");
	nameTextField.setBounds(170, 40, 150, 30);
	f.add(nameTextField);
	f.setLayout(null);
	f.setVisible(true);
	f.setSize(500, 500);
		
	}

}
