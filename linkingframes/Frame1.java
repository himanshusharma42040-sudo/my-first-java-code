package linkingframes;



import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Frame1 extends Frame implements WindowListener {
	public Frame1() {
		
		super("My Frist Frame");
		
		Button b=new Button("Click Me");
		b.setBounds(100, 40, 100, 30);
		add(b);
		
		class ButtonListner implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				new Frame2();
			}
			
		}
		b.addActionListener(new ButtonListner());
		setLayout(null);
		setVisible(true);
		setSize(500, 500);
		
		addWindowListener(new MyWindowListner(this));
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Self class window Listener");
		dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
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
class MyWindowListner extends WindowAdapter{
	Frame f;
	public MyWindowListner(Frame f) {
		this.f=f;
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Closing method of outer class window listner");
		f.dispose();
	}
}
