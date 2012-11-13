package pl.gda.pg.eti.kernelhive.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class LoginDialog extends JDialog {

	private static final long serialVersionUID = -2872256566061326736L;
	public static final int APPROVE_OPTION = 0;
	public static final int CANCEL_OPTION = 1;

	private final JTextField textField;
	private final JPasswordField passwordField;
	private final JLabel lblValidationError;
	private final JButton btnLogin;
	private final JProgressBar progressBar;
	private final int retval = CANCEL_OPTION;

	public LoginDialog() {
		getContentPane().setLayout(null);

		final JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(12, 12, 86, 15);
		getContentPane().add(lblUsername);

		final JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(12, 39, 86, 15);
		getContentPane().add(lblPassword);

		textField = new JTextField();
		textField.setBounds(116, 10, 114, 19);
		getContentPane().add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(116, 37, 114, 19);
		getContentPane().add(passwordField);

		lblValidationError = new JLabel("Invalid username or password!");
		lblValidationError
				.setIcon(new ImageIcon(
						NewProjectDialog.class
								.getResource("/com/sun/java/swing/plaf/windows/icons/Error.gif")));
		lblValidationError.setBounds(12, 66, 424, 32);
		getContentPane().add(lblValidationError);

		btnLogin = new JButton("Login");
		btnLogin.setBounds(319, 230, 117, 25);
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// TODO

			}
		});
		getContentPane().add(btnLogin);

		progressBar = new JProgressBar();
		progressBar.setBounds(12, 110, 424, 14);
		getContentPane().add(progressBar);
	}

	public int getStatus() {
		return retval;
	}

	public String getUsername() {
		return textField.getText();
	}

	public byte[] getPassword() {
		final byte[] pass = new byte[passwordField.getPassword().length];
		for (int i = 0; i < passwordField.getPassword().length; i++) {
			pass[i] = (byte) passwordField.getPassword()[i];
		}
		return pass;
	}
}
