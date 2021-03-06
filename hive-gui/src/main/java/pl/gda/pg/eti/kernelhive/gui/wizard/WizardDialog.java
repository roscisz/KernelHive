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
package pl.gda.pg.eti.kernelhive.gui.wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

public class WizardDialog extends JDialog {

	private static final long serialVersionUID = 640539009350073827L;

	private JPanel cardPanel;
	private CardLayout cardLayout;
	private JButton backButton;
	private JButton nextButton;
	private JButton cancelButton;

	private WizardController controller;

	public WizardDialog(Dialog owner, WizardController controller) {
		super(owner);
		this.controller = controller;
		initComponents();
	}

	public WizardDialog(Frame frame, WizardController controller) {
		super(frame);
		this.controller = controller;
		initComponents();
	}

	JButton getBackButton() {
		return backButton;
	}

	JButton getCancelButton() {
		return cancelButton;
	}

	/**
	 * 
	 * @return
	 */
	public CardLayout getCardLayout() {
		return cardLayout;
	}

	/**
	 * 
	 * @return
	 */
	public JPanel getCardPanel() {
		return cardPanel;
	}

	JButton getNextButton() {
		return nextButton;
	}

	private void initComponents() {
		JPanel buttonPanel = new JPanel();
		JSeparator separator = new JSeparator();
		Box buttonBox = new Box(BoxLayout.X_AXIS);

		cardPanel = new JPanel();
		cardPanel.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));

		cardLayout = new CardLayout();
		cardPanel.setLayout(cardLayout);

		backButton = new JButton("Back");
		nextButton = new JButton("Next");
		cancelButton = new JButton("Cancel");

		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controller.executeBack();
				} catch (WizardPanelNotFoundException e1) {
					e1.printStackTrace();
					controller.getWizard().close(Wizard.ERROR_RETURN_CODE);
				}
			}
		});
		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controller.executeNext();
				} catch (WizardPanelNotFoundException e1) {
					e1.printStackTrace();
					controller.getWizard().close(Wizard.ERROR_RETURN_CODE);
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.executeCancel();
			}
		});

		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(separator, BorderLayout.NORTH);

		buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
		buttonBox.add(backButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(nextButton);
		buttonBox.add(Box.createHorizontalStrut(30));
		buttonBox.add(cancelButton);

		buttonPanel.add(buttonBox, BorderLayout.EAST);

		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().add(cardPanel, BorderLayout.CENTER);
	}
}
