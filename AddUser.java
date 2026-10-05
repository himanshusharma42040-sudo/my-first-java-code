import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AddUser {
	public static void main(String[] args) {
		
		Frame f= new Frame("Add user screen");
		f.addWindowListener(new MyWindowListener2(f));
		f.setLayout(null);
		f.setSize(500, 500);
		f.setVisible(true);
	}

}
class MyWindowListener2 extends WindowAdapter{
	private Frame f;
	public MyWindowListener2(Frame f) {
		this.f= f;
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("window opened");
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Listener created from adapter class");
		f.dispose();
		
	}
}
class MyWindowListener implements WindowListener{
	private Frame f;
	public MyWindowListener(Frame f) {
		this.f=f;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("window opened");
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Listener created using directly interface");
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("window closed");
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
