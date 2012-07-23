package pl.gda.pg.eti.kernelhive.gui.frame.wizard.workflow;

import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Dimension;

/**
 * 
 * @author marcel
 *
 */
public class InputDataPanel extends JPanel {
	
	private static final long serialVersionUID = 3430605808965461808L;
	
	private JTextField textField;
	private JLabel lblUrlIsInvalid;
	private JButton btnValidate;
	
	public InputDataPanel() {
		setSize(new Dimension(450, 300));
		setPreferredSize(new Dimension(450, 300));
		setMinimumSize(new Dimension(450, 300));
		setLayout(null);
		
		JLabel lblInputDataUrl = new JLabel("Input data URL");
		lblInputDataUrl.setBounds(12, 12, 105, 15);
		add(lblInputDataUrl);
		
		textField = new JTextField();
		textField.setBounds(12, 39, 311, 19);
		add(textField);
		textField.setColumns(10);
		
		lblUrlIsInvalid = new JLabel("URL is invalid!");
		lblUrlIsInvalid.setVisible(false);
		lblUrlIsInvalid.setBounds(12, 70, 105, 15);
		add(lblUrlIsInvalid);
		
		btnValidate = new JButton("Validate");
		btnValidate.setBounds(335, 36, 93, 25);
		add(btnValidate);
		
	}
	
	public void addValidateButtonActionListener(ActionListener l){
		btnValidate.addActionListener(l);
	}
	
	public void setInvalidUrlLabelVisible(boolean visible){
		lblUrlIsInvalid.setVisible(visible);
	}
	
	public String getInputDataUrlString(){
		return textField.getText();
	}
}
