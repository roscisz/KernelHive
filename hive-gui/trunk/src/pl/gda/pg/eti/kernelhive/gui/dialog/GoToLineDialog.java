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

import javax.swing.JDialog;

import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GoToLineDialog extends JDialog {

	private static final long serialVersionUID = -3350538298987066984L;
	
	private MainFrame frame;
	private JTextField textField;
	
	public GoToLineDialog(MainFrame frame){
		super(frame);
		this.frame = frame;
		setSize(new Dimension(400, 90));
		setBounds(getParent().getX(), getParent().getY(), getWidth(), getHeight());
		getContentPane().setLayout(null);
		
		JLabel lblGoToLine = new JLabel("Go To Line:");
		lblGoToLine.setBounds(12, 12, 87, 15);
		getContentPane().add(lblGoToLine);
		
		textField = new JTextField();
		textField.setBounds(117, 10, 114, 19);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					goToLine();
					setVisible(false);
					dispose();
				}
			}
		});
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnGo = new JButton("Go");
		btnGo.setBounds(243, 7, 117, 25);
		btnGo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				goToLine();
				setVisible(false);
				dispose();
			}
		});
		getContentPane().add(btnGo);
	}
	
	public void goToLine(){
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		tabContent.goToLine(Integer.parseInt(textField.getText()));
	}
	
	
}
