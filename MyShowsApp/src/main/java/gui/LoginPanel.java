package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import gui.MainPanel;
import gui.SignupPanel;
import gui.MainFrame;
import connection.User;
import database.DatabaseOperations;

import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class LoginPanel extends JPanel {
	
	private final MainFrame rootFrame;
	private JPasswordField passwordField;
	private JTextField usernameField;
	private JLabel wrongLabel;
	/**
	 * Create the panel.
	 */
	public LoginPanel(MainFrame rootFrame) {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBackground(Color.WHITE);
		this.rootFrame = rootFrame;
		initComponents();
	}
	private void initComponents(){
		rootFrame.setSize(420,350);
		setBounds(100, 100, 400, 330);
		setLayout(null);
				
		JButton signupButton = new JButton("Sign Up");
		signupButton.setBounds(302, 11, 89, 23);
		signupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rootFrame.changePanel(new SignupPanel(rootFrame));
			}
		});
		add(signupButton);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(48, 106, 100, 23);
		add(usernameLabel);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(48, 155, 100, 23);
		add(passwordLabel);
		
		wrongLabel = new JLabel("");
		wrongLabel.setForeground(Color.RED);
		wrongLabel.setBounds(47, 75, 246, 20);
		this.add(wrongLabel);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(118, 156, 246, 20);
		add(passwordField);
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		usernameField.setBounds(118, 107, 246, 20);
		add(usernameField);
		
		JButton loginButton = new JButton("Log In");
		loginButton.setBounds(127, 235, 134, 23);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				wrongLabel.setText("");
				LoginWorker worker = new LoginWorker();
		        try {
		            worker.execute();
		            if(worker.get()){
		                //if (!connectToMasterServer())
		                   // return;
		                rootFrame.changePanel(new MainPanel(rootFrame));
		            }
		        } catch (InterruptedException ex) {
		            //ConsoleFrame.showError(ex.getMessage());
		        } catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		add(loginButton);
	}
	private class LoginWorker extends SwingWorker<Boolean, Object>{

        protected Boolean doInBackground() throws Exception {
            //This should start false
            boolean success = false;
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if(checkUsername(username)){
            	passwordField.setText("");
                User.getInstance().setUsername(username);
                User.getInstance().setPassword(password);
                success = true;
                String res = DatabaseOperations.searchUser(User.getInstance());
                if (res.equals("WRONG PASSWORD") || res.equals("WRONG USERNAME")){
					wrongLabel.setText("WRONG USERNAME OR PASSWORD, TRY AGAIN!");
					usernameField.setText("");
					passwordField.setText("");
					success = false;
                }
            }
            
            return success;
        }
      
        private boolean checkUsername(String username){
            char[] unacceptebleChars = {'\'','=','+',';','\"'};
            for(char c:unacceptebleChars){
                if(username.indexOf(c) >= 0){
                 //ConsoleFrame.showError("Username can't contain these characters: ' = + ; \" ");
                	wrongLabel.setText("Username can't contain these characters: ' = + ; \"");
                	usernameField.setText("");
					passwordField.setText("");
                    return false;
                }
            }
            return true;
        }
        
    }
}
