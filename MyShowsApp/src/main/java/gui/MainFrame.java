package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.LoginPanel;
import gui.MainFrame;

public class MainFrame extends JFrame{
private static MainFrame instance;
	
	private boolean loggedIn = false;
	
	private MainFrame(){
		super("MyShowsApp");
	}
	
	private void setup(){
		this.setSize(400,330);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(new LoginPanel(this));
	}
	public static MainFrame getInstance(){
        if(instance == null){
            instance = new MainFrame();
            instance.setup();
            //need to tell the engine the window is closing);
            instance.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    /*if(VisualEngine.initialized()){
                        //telling the engine to close with the window
                        VisualEngine.getInstance().dispatchEvent(new WindowEvent(instance, WindowEvent.WINDOW_CLOSING));    //firing a closing event
                    }*/
                    super.windowClosing(e);
                }

            });
        }
        return instance;
    }
	public void changePanel(JPanel panel){
        this.getContentPane().removeAll();
        this.getContentPane().add(panel);
        //this.pack();
        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }
}
