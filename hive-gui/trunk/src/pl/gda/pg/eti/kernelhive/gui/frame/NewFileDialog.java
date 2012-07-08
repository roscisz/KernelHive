package pl.gda.pg.eti.kernelhive.gui.frame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;
import pl.gda.pg.eti.kernelhive.common.validation.ResourcePathValidator;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class NewFileDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6014149952911009233L;
	public static final int APPROVE_OPTION = 0;
	public static final int CANCEL_OPTION = 1;

	private static ResourceBundle BUNDLE = AppConfiguration.getInstance().getLanguageResourceBundle();
	private static Logger LOG = Logger.getLogger(NewProjectDialog.class.getName());
	
	private final JPanel contentPanel = new JPanel();
	private JTextField tfFileName;
	private JTextField tfFileDir;
	private JLabel lblChoosenFolderPath;
	private JButton okButton;
	private JButton cancelButton;
	private int retval;
	
	public NewFileDialog() {
		LOG.info("KH: constructor fired!");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblFileName = new JLabel("Name"); 
		lblFileName.setBounds(12, 12, 100, 15);
		contentPanel.add(lblFileName);
		
		JLabel lblFileFolder = new JLabel("Directory");
		lblFileFolder.setBounds(12, 39, 100, 15);
		contentPanel.add(lblFileFolder);
		
		tfFileName = new JTextField();
		tfFileName.setBounds(130, 10, 190, 19);
		contentPanel.add(tfFileName);
		tfFileName.setColumns(10);
		
		tfFileDir = new JTextField();
		tfFileDir.setBounds(130, 37, 190, 19);
		tfFileDir.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent arg0) {
				ValidationResult vr = ResourcePathValidator.validateDirectory(tfFileDir.getText());
				if(vr.getType()==ValidationResultType.INVALID){
					okButton.setEnabled(false);
					lblChoosenFolderPath.setVisible(true);
				} else{
					okButton.setEnabled(true);
					lblChoosenFolderPath.setVisible(false);
				}
				
			}
		});
		contentPanel.add(tfFileDir);
		tfFileDir.setColumns(10);
		
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
					tfFileDir.setText(file.getAbsolutePath());
				}
				
			}
		});
		contentPanel.add(button);
		
		lblChoosenFolderPath = new JLabel("Error - invalid filepath");
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
				cancelButton = new JButton("Cancel");
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
	
	public int getStatus(){
		return retval;
	}
	
	public String getFileName(){
		return this.tfFileName.getText();
	}
	
	public String getFileDirectory(){
		return this.tfFileDir.getText();
	}
}