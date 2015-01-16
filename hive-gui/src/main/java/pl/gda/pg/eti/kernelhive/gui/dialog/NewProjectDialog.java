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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;
import pl.gda.pg.eti.kernelhive.common.validation.ResourcePathValidator;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

import java.util.ResourceBundle;
import java.util.logging.Logger;

public class NewProjectDialog extends JDialog {
	
	public static final int APPROVE_OPTION = 0;
	public static final int CANCEL_OPTION = 1;

	private static final long serialVersionUID = 3060001596955474123L;
	private static ResourceBundle BUNDLE = AppConfiguration.getInstance().getLanguageResourceBundle();
	private static Logger LOG = Logger.getLogger(NewProjectDialog.class.getName());
	
	private final JPanel contentPanel = new JPanel();
	private JTextField tfProjectName;
	private JTextField tfProjectDir;
	private JLabel lblChoosenFolderPath;
	private JButton okButton;
	private JButton cancelButton;
	private int retval;
	
	/**
	 * Create the dialog.
	 */
	public NewProjectDialog() {
		LOG.info("KH: constructor fired!");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblProjectName = new JLabel(BUNDLE.getString("NewProjectDialog.lblProjectName.text")); //$NON-NLS-1$
		lblProjectName.setBounds(12, 12, 100, 15);
		contentPanel.add(lblProjectName);
		
		JLabel lblProjectFolder = new JLabel(BUNDLE.getString("NewProjectDialog.lblProjectFolder.text")); //$NON-NLS-1$
		lblProjectFolder.setBounds(12, 39, 100, 15);
		contentPanel.add(lblProjectFolder);
		
		tfProjectName = new JTextField();
		tfProjectName.setBounds(130, 10, 190, 19);
		contentPanel.add(tfProjectName);
		tfProjectName.setColumns(10);
		
		tfProjectDir = new JTextField();
		tfProjectDir.setBounds(130, 37, 190, 19);
		tfProjectDir.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent arg0) {
				ValidationResult vr = ResourcePathValidator.validateDirectory(tfProjectDir.getText());
				if(vr.getType()==ValidationResultType.INVALID){
					okButton.setEnabled(false);
					lblChoosenFolderPath.setVisible(true);
				} else{
					okButton.setEnabled(true);
					lblChoosenFolderPath.setVisible(false);
				}
				
			}
		});
		contentPanel.add(tfProjectDir);
		tfProjectDir.setColumns(10);
		
		JButton button = new JButton("...");
		button.setBounds(332, 34, 35, 25);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setAcceptAllFileFilterUsed(false);
				fc.setMultiSelectionEnabled(false);
				if(fc.showDialog(contentPanel, "Select")==JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					tfProjectDir.setText(file.getAbsolutePath());
				}
				
			}
		});
		contentPanel.add(button);
		
		lblChoosenFolderPath = new JLabel(BUNDLE.getString("NewProjectDialog.lblChoosenFolderPath.text")); //$NON-NLS-1$
		lblChoosenFolderPath.setVisible(false);
		lblChoosenFolderPath.setIcon(new ImageIcon(NewProjectDialog.class.getResource("/com/sun/java/swing/plaf/windows/icons/Error.gif")));
		lblChoosenFolderPath.setBounds(12, 66, 279, 49);
		contentPanel.add(lblChoosenFolderPath);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						retval = APPROVE_OPTION;
						setVisible(false);
						dispose();
					}
				});
			}
			{
				cancelButton = new JButton(BUNDLE.getString("NewProjectDialog.cancelButton.text")); //$NON-NLS-1$
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						retval = CANCEL_OPTION;
						setVisible(false);
						dispose();
					}
				});
			}
		}
	}
	/**
	 * get the status of this dialog operation
	 * @return {@link JDialog} status int
	 */
	public int getStatus(){
		return retval;
	}
	
	/**
	 * get the choosen project name
	 * @return {@link String}
	 */
	public String getProjectName(){
		return this.tfProjectName.getText();
	}
	
	/**
	 * get the choosen project directory
	 * @return {@link String}
	 */
	public String getProjectDirectory(){
		return this.tfProjectDir.getText();
	}
	
}
