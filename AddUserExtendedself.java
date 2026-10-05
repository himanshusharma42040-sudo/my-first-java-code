import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddUserExtendedself {
	public static void main(String[] args) {
		
		Frame f= new Frame("Add User Self");
		f.setLayout(null);
		f.setSize(500, 500);
		f.setVisible(true);
		
		
		
		//Name
		Label nameLabel= new Label("Name");
		nameLabel.setBounds(80, 100, 100, 30);
		f.add(nameLabel);
		
		TextField nameField1= new TextField();
		nameField1.setBounds(180, 100, 220, 30);
		f.add(nameField1);
		
		//Email
		Label emailLabel= new Label("Email");
		emailLabel.setBounds(80, 150,100, 30);
		f.add(emailLabel);
		
		TextField emailField1= new TextField();
		emailField1.setBounds(180, 150, 220, 30);
		f.add(emailField1);
		
		Button addButton= new Button("Add User");
		addButton.setBounds(290, 220, 60, 30);
		f.add(addButton);
		
		Button resetButton= new Button("Reset");
		resetButton.setBounds(150, 220, 60, 30);
		f.add(resetButton);
		
		class MyWindowListener3 extends WindowAdapter{
			
			public void windowClosing(WindowEvent e) {
				System.out.println("closed");
				f.dispose();
			}
		}
		
		class ButtonActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				System.out.println("Add user button pressed");
				System.out.println(nameField1.getText());
				System.out.println(emailField1.getText());
			}
			
		}
		
		class ResetButtonActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("clicked on the reset button");
				nameField1.setText("");
				emailField1.setText("");
			}
			
		}
		
		f.addWindowListener(new MyWindowListener3());
		addButton.addActionListener(new ButtonActionListener());
		resetButton.addActionListener(new ResetButtonActionListener());
	}

}
