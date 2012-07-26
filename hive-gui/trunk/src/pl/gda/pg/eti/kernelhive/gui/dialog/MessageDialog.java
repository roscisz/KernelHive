package pl.gda.pg.eti.kernelhive.gui.dialog;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class MessageDialog {

	public static void showErrorDialog(Component parent, String title,
			String message) {
		JOptionPane
				.showMessageDialog(
						parent,
						message,
						title,
						JOptionPane.ERROR_MESSAGE,
						new ImageIcon(
								MessageDialog.class
										.getResource("/com/sun/java/swing/plaf/windows/icons/Error.gif")));
	}
}
