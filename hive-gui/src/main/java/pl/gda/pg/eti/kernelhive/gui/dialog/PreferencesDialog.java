/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 * Copyright (c) 2014 Szymon Bultrowicz
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

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;
import java.awt.Dimension;
import java.net.URL;
import javax.swing.JTextField;

public class PreferencesDialog extends JDialog {

	private static final long serialVersionUID = -5061336150519876896L;
	private JComboBox comboBox;
	JTextField textEngineAddress;

	public PreferencesDialog(Frame frame) {
		super(frame);
		setPreferredSize(new Dimension(450, 300));
		setSize(new Dimension(470, 320));
		setBounds(getParent().getX(), getParent().getY(), getWidth(), getHeight());
		getContentPane().setLayout(null);

		JLabel lblLanguage = new JLabel("Language");
		lblLanguage.setBounds(12, 12, 80, 15);
		getContentPane().add(lblLanguage);

		JLabel lblEngineAddress = new JLabel("Engine base address");
		lblEngineAddress.setBounds(12, 40, 150, 15);
		getContentPane().add(lblEngineAddress);

		textEngineAddress = new JTextField();
		textEngineAddress.setBounds(170, 40, 150, 20);
		getContentPane().add(textEngineAddress);

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
				savePreferences();
				setVisible(false);
				dispose();
			}
		});
		getContentPane().add(btnSave);

		initUI();
	}

	private void initUI() {
		List<String> languages = AppConfiguration.getInstance()
				.getAvailableLanguageResourceBundles();

		comboBox = new JComboBox(languages.toArray());
		comboBox.setSelectedItem(AppConfiguration.getInstance()
				.getSelectedLanguage());
		comboBox.setBounds(110, 7, 102, 24);
		getContentPane().add(comboBox);

		URL engineAddress = AppConfiguration.getInstance().getEngineAddress();
		if (engineAddress != null) {
			textEngineAddress.setText(engineAddress.toString());
		}
	}

	private void savePreferences() {
		String language = (String) comboBox.getSelectedItem();
		AppConfiguration.getInstance().setLanguage(language);
		AppConfiguration.getInstance().setEngineAddress(textEngineAddress.getText());
	}
}
