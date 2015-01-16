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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import pl.gda.pg.eti.kernelhive.gui.project.IProject;
import java.awt.Dimension;

public class ProjectPropertiesDialog extends JDialog {
	
	private static final long serialVersionUID = -333988629126703788L;
	
	private JTextField textField;
	private IProject project;
	
	public ProjectPropertiesDialog(JFrame frame, IProject project) {
		super(frame);
		
		setSize(new Dimension(470, 320));
		setPreferredSize(new Dimension(470, 320));
		setBounds(getParent().getX(), getParent().getY(), getWidth(), getHeight());
		this.project = project;
		
		getContentPane().setLayout(null);
		
		JLabel lblProjectName = new JLabel("Project name");
		lblProjectName.setBounds(12, 12, 101, 15);
		getContentPane().add(lblProjectName);
		
		textField = new JTextField();
		textField.setBounds(131, 10, 184, 19);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(12, 230, 117, 25);
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		getContentPane().add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(319, 230, 117, 25);
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveProjectProperties();
				setVisible(false);
				dispose();
			}
		});
		getContentPane().add(btnSave);
		
		initUI();
	}
	
	private void initUI(){
		textField.setText(project.getProjectName());
	}
	
	private void saveProjectProperties(){
		String projectName = textField.getText();
		project.setProjectName(projectName);
	}

}
