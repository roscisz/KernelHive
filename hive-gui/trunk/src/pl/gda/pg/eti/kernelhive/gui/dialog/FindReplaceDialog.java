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
import javax.swing.JCheckBox;

public class FindReplaceDialog extends JDialog {

	private static final long serialVersionUID = -3682228024712339859L;

	private MainFrame frame;
	private JTextField textFind;
	private JTextField textReplace;
	private JCheckBox chckbxMatchCase;
	private JCheckBox chckbxRegularExpression;
	private JCheckBox chckbxWholeWord;
	private JCheckBox chckbxSearchBackwards;

	public FindReplaceDialog(MainFrame frame) {
		super(frame);
		this.frame = frame;
		setPreferredSize(new Dimension(400, 300));
		setSize(new Dimension(410, 310));
		setBounds(getParent().getX(), getParent().getY(), getWidth(),
				getHeight());
		getContentPane().setLayout(null);

		JLabel lblFind = new JLabel("Find");
		lblFind.setBounds(12, 12, 88, 15);
		getContentPane().add(lblFind);

		JLabel lblReplace = new JLabel("Replace");
		lblReplace.setBounds(12, 39, 88, 15);
		getContentPane().add(lblReplace);

		textFind = new JTextField();
		textFind.setBounds(118, 10, 114, 19);
		getContentPane().add(textFind);
		textFind.setColumns(10);

		textReplace = new JTextField();
		textReplace.setBounds(118, 37, 114, 19);
		getContentPane().add(textReplace);
		textReplace.setColumns(10);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(12, 230, 114, 25);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		getContentPane().add(btnCancel);

		JButton btnReplaceAll = new JButton("Replace All");
		btnReplaceAll.setBounds(264, 81, 114, 25);
		btnReplaceAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				replaceAll();
			}
		});
		getContentPane().add(btnReplaceAll);

		JButton btnReplace = new JButton("Replace");
		btnReplace.setBounds(264, 44, 114, 25);
		btnReplace.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				replace();
			}
		});
		getContentPane().add(btnReplace);

		JButton btnFind = new JButton("Find");
		btnFind.setBounds(264, 7, 114, 25);
		btnFind.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				find();
			}
		});
		getContentPane().add(btnFind);
		
		chckbxMatchCase = new JCheckBox("Match case");
		chckbxMatchCase.setBounds(12, 62, 162, 23);
		getContentPane().add(chckbxMatchCase);
		
		chckbxRegularExpression = new JCheckBox("Regular expression");
		chckbxRegularExpression.setBounds(12, 89, 162, 23);
		getContentPane().add(chckbxRegularExpression);
		
		chckbxWholeWord = new JCheckBox("Whole word");
		chckbxWholeWord.setBounds(12, 116, 162, 23);
		getContentPane().add(chckbxWholeWord);
		
		chckbxSearchBackwards = new JCheckBox("Search backwards");
		chckbxSearchBackwards.setBounds(12, 143, 162, 23);
		getContentPane().add(chckbxSearchBackwards);

	}

	protected void replaceAll() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		boolean matchCase = chckbxMatchCase.getSelectedObjects()==null?false:true;
		boolean wholeWord = chckbxWholeWord.getSelectedObjects()==null?false:true;
		boolean regex = chckbxRegularExpression.getSelectedObjects()==null?false:true;
		boolean searchBack = chckbxSearchBackwards.getSelectedObjects()==null?false:true;
		int replacesCount = tabContent.replaceAll(textFind.getText(),
				textReplace.getText(), matchCase, wholeWord, regex, searchBack);
		if (replacesCount >= 0) {
			MessageDialog.showSuccessDialog(this, "", "'" + replacesCount
					+ "' occurences of '" + textFind.getText() + "' replaced");
		} else{
			MessageDialog.showErrorDialog(this, "Error", "No text: '"
					+ textFind.getText() + "' could be found!");
		}
	}

	protected void replace() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		boolean matchCase = chckbxMatchCase.getSelectedObjects()==null?false:true;
		boolean wholeWord = chckbxWholeWord.getSelectedObjects()==null?false:true;
		boolean regex = chckbxRegularExpression.getSelectedObjects()==null?false:true;
		boolean searchBack = chckbxSearchBackwards.getSelectedObjects()==null?false:true;
		boolean replaced = tabContent.replace(textFind.getText(),
				textReplace.getText(), matchCase, wholeWord, regex, searchBack);
		if (!replaced) {
			MessageDialog.showErrorDialog(this, "Error", "No text: '"
					+ textFind.getText() + "' could be found!");
		}
	}

	protected void find() {
		JTabContent tabContent = (JTabContent) frame.getWorkspacePane()
				.getSelectedComponent();
		boolean matchCase = chckbxMatchCase.getSelectedObjects()==null?false:true;
		boolean wholeWord = chckbxWholeWord.getSelectedObjects()==null?false:true;
		boolean regex = chckbxRegularExpression.getSelectedObjects()==null?false:true;
		boolean searchBack = chckbxSearchBackwards.getSelectedObjects()==null?false:true;
		boolean found = tabContent.find(textFind.getText(), matchCase, wholeWord,
				regex, searchBack);
		if (!found) {
			MessageDialog.showErrorDialog(this, "Error", "No text: '"
					+ textFind.getText() + "' could be found!");
		}
	}
}
