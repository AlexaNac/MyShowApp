package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;

import gui.LoginPanel;
import gui.MainPanel;
import gui.MainFrame;
import connection.User;
import database.DatabaseOperations;

public class SignupPanel extends JPanel {

	private final MainFrame rootFrame;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;
	private JLabel wrongUsernameLabel;
	private JLabel wrongPasswordLabel;
	private JLabel emptyPasswordLabel;
	
	public SignupPanel(MainFrame rootFrame) {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBackground(Color.WHITE);
		this.rootFrame = rootFrame;
		initComponents();
	}
	private void initComponents(){
		rootFrame.setSize(440,370);
		setBounds(100, 100, 435, 350);
		setLayout(null);
		
		JButton backButton = new JButton("<-Back");
		backButton.setBounds(10, 11, 88, 23);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rootFrame.changePanel(new LoginPanel(rootFrame));
			}
		});
		setLayout(null);
		add(backButton);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(31, 90, 100, 23);
		add(usernameLabel);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(31, 150, 100, 23);
		add(passwordLabel);
		/*
		wrongLabel = new JLabel("");
		wrongLabel.setForeground(Color.RED);
		wrongLabel.setBounds(60, 105, 246, 20);
		this.add(wrongLabel);
		*/
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		usernameField.setBounds(164, 90, 246, 20);
		add(usernameField);
		passwordField = new JPasswordField();
		passwordField.setBounds(164, 150, 246, 20);
		add(passwordField);
		
		JLabel passwordConfirmLabel = new JLabel("Confirm password:");
		passwordConfirmLabel.setBounds(31, 214, 123, 17);
		this.add(passwordConfirmLabel);
		
		confirmPasswordField = new JPasswordField();
		confirmPasswordField.setBounds(164, 211, 246, 20);
		this.add(confirmPasswordField);
		
		wrongPasswordLabel = new JLabel("");
		wrongPasswordLabel.setForeground(Color.RED);
		wrongPasswordLabel.setBounds(31, 124, 345, 16);
		add(wrongPasswordLabel);
		
		wrongUsernameLabel = new JLabel("");
		wrongUsernameLabel.setForeground(Color.RED);
		wrongUsernameLabel.setBounds(31, 62, 345, 17);
		add(wrongUsernameLabel);
		
		emptyPasswordLabel = new JLabel("");
		emptyPasswordLabel.setForeground(Color.RED);
		emptyPasswordLabel.setBounds(31, 186, 345, 17);
		add(emptyPasswordLabel);
		
		JButton signupButton = new JButton("Sign Up");
		signupButton.setBounds(131, 281, 147, 27);
		signupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registerButtonActionPerformed(e);
			}
		});
		this.add(signupButton);
		}
	private void registerButtonActionPerformed(ActionEvent e){
		wrongUsernameLabel.setText("");
		wrongPasswordLabel.setText("");
		emptyPasswordLabel.setText("");
		passwordField.setBorder(BorderFactory.createLineBorder(Color.black));
		confirmPasswordField.setBorder(BorderFactory.createLineBorder(Color.black));
		if (checkFields()){
			SignupWorker worker = new SignupWorker();
	        try {
	            worker.execute();
	            if(worker.get()){
	                //if (!connectToMasterServer())
	                   // return;
	                rootFrame.changePanel(new MainPanel(rootFrame));
	            }
	        } catch (InterruptedException ex) {
	            //ConsoleFrame.showError(ex.getMessage());
	        } catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	}
	private boolean checkFields(){
		boolean res = true;
		String conf = new String(confirmPasswordField.getPassword());
		String password = new String(passwordField.getPassword());
		String user = new String(usernameField.getText());
		if (user.equals("")){
			usernameField.setBorder(BorderFactory.createLineBorder(Color.red));
			wrongUsernameLabel.setText("This field can not be empty.");
			res = false;
		}
		if (password.equals("")){
			passwordField.setBorder(BorderFactory.createLineBorder(Color.red));
			emptyPasswordLabel.setText("This field can not be empty.");
			res = false;
		}
		if (conf.equals("")){
				confirmPasswordField.setBorder(BorderFactory.createLineBorder(Color.red));
				wrongPasswordLabel.setText("This field can not be empty.");
				res = false;
		}
		if (!password.equals("") && !conf.equals("") && !conf.equals(password)){
					res = false;
					wrongPasswordLabel.setText("Password does not match.");
					emptyPasswordLabel.setText("");
					wrongUsernameLabel.setText("");
					confirmPasswordField.setText("");
		}
				
		return res;
	}
	private class SignupWorker extends SwingWorker<Boolean, Object>{

        protected Boolean doInBackground() throws Exception {
            //This should start false
            boolean success = false;
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if(checkUsername(username)){
            	passwordField.setText("");
                confirmPasswordField.setText("");
                User.getInstance().setUsername(username);
                User.getInstance().setPassword(password);
                success = true;
                String res = DatabaseOperations.insertUser(User.getInstance());
                if (res != "OK"){
                	wrongUsernameLabel.setText(res);
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
                	wrongUsernameLabel.setText("Username can't contain these characters: ' = + ; \"");
                	usernameField.setText("");
					passwordField.setText("");
					confirmPasswordField.setText("");
                    return false;
                }
            }
            return true;
        }
        
    }
}