package pl.gda.pg.eti.kernelhive.gui.workflow.wizard;

import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;

import java.awt.Dimension;

public class UserCredentialsPanel extends JPanel {

	private static final long serialVersionUID = 595029158626657109L;
	
	static final String USERNAME_CHANGED_ACTION = "username_changed";
	static final String PASSWORD_CHANGED_ACTION = "password_changed";
	
	private JTextField usernameField;
	private JPasswordField passwordField;
	
	public UserCredentialsPanel(){
		setSize(new Dimension(450, 300));
		setPreferredSize(new Dimension(450, 300));
		setMinimumSize(new Dimension(450, 300));
		setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(12, 12, 86, 15);
		add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(12, 49, 70, 15);
		add(lblPassword);
		
		usernameField = new JTextField();
		usernameField.setBounds(116, 10, 114, 19);
		add(usernameField);
		usernameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(116, 47, 114, 19);
		add(passwordField);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUsername(){
		return usernameField.getText();
	}
	
	/**
	 * 
	 * @return
	 */
	public char[] getPassword(){
		return passwordField.getPassword();
	}
	
	void addUsernameTextFieldCaretListener(CaretListener l){
		usernameField.addCaretListener(l);
	}
	
	void addPasswordFieldCaretListener(CaretListener l){
		passwordField.addCaretListener(l);
	}
}
