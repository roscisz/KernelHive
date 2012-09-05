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
