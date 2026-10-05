import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyGreetingApp {
	

	public static void main(String[] args) {
		
		Frame f= new Frame("My Greeting App");
		f.setLayout(null);
		f.setSize(500, 500);
		f.setVisible(true);
		
		// code for name label
		Label nameLabel= new Label("Name");
		nameLabel.setBounds(50, 40,100, 30);
		f.add(nameLabel);
		
		// code for text field
		TextField nameTextField= new TextField();
		nameTextField.setBounds(160, 40, 220, 30);
		f.add(nameTextField);
		
		// code for greet button
		Button greetButton= new Button("Greet");
		greetButton.setBounds(65, 100, 160, 30);
		f.add(greetButton);
		
		
		Label resultLabel= new Label();
		resultLabel.setBounds(65, 180, 200, 100);
		f.add(resultLabel);
		
		
		class GreetButtonActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String name=nameTextField.getText();
				resultLabel.setText("Hello "+ name+ "!");
			}
			
		}
		
		class GreetWindowListener extends WindowAdapter{
			
			public void windowClosing(WindowEvent e) {
				
				f.dispose();
			}
		}
		f.addWindowListener(new GreetWindowListener());
		greetButton.addActionListener(new GreetButtonActionListener());
		
	}

}
