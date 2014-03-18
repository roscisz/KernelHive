/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.gui.workflow.wizard;

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
		setSize(new Dimension(900, 300));
		setPreferredSize(new Dimension(900, 300));
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
