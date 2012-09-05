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

public class PreferencesDialog extends JDialog {

	private static final long serialVersionUID = -5061336150519876896L;
	
	private JComboBox comboBox;

	public PreferencesDialog(Frame frame) {
		super(frame);
		setPreferredSize(new Dimension(450, 300));
		setSize(new Dimension(470, 320));
		setBounds(getParent().getX(), getParent().getY(), getWidth(), getHeight());
		getContentPane().setLayout(null);

		JLabel lblLanguage = new JLabel("Language");
		lblLanguage.setBounds(12, 12, 80, 15);
		getContentPane().add(lblLanguage);

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
	}

	private void savePreferences() {
		String language = (String) comboBox.getSelectedItem();
		AppConfiguration.getInstance().setLanguage(language);
	}
}
