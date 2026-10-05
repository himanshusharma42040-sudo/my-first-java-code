package linkingframes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame1Learning extends Frame  {
	
	Frame1Learning(){
		super("My first linking window");
		setLayout(null);
		setVisible(true);
		setSize(500,500);
		
		Button clickButton = new Button("Click Me");
		clickButton.setBounds(100, 40, 100, 30);
		add(clickButton);
		
		class ClickActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				new Frame2Learning();
			}
			
		}
		
		clickButton.addActionListener(new ClickActionListener());
	}

}
