package pl.gda.pg.eti.kernelhive.gui.frame;

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
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;

import java.util.Locale;
import java.util.ResourceBundle;

public class NewProjectDialog extends JDialog {
	private static ResourceBundle BUNDLE = AppConfiguration.getInstance().getLanguageResourceBundle();

	/**
	 * 
	 */
	private static final long serialVersionUID = 3060001596955474123L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField tfProjectName;
	private JTextField tfProjectDir;
	private JLabel lblChoosenFolderPath;

	/**
	 * Create the dialog.
	 */
	public NewProjectDialog() {
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
		tfProjectDir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				//TODO path validation
				
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
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(BUNDLE.getString("NewProjectDialog.cancelButton.text")); //$NON-NLS-1$
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
