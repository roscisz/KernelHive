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
	
	public static void showMessageDialog(Component parent, String title, String message){
		JOptionPane
		.showMessageDialog(
				parent,
				message,
				title,
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(
						MessageDialog.class
								.getResource("/com/sun/java/swing/plaf/windows/icons/Inform.gif")));
	}
	
	public static void showSuccessDialog(Component parent, String title, String message){
		JOptionPane
		.showMessageDialog(
				parent,
				message,
				title,
				JOptionPane.INFORMATION_MESSAGE);
	}
}
