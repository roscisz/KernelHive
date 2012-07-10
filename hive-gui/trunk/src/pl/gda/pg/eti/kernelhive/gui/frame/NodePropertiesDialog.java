package pl.gda.pg.eti.kernelhive.gui.frame;

import javax.swing.JDialog;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JList;

public class NodePropertiesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7313937306855473619L;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JLabel lblSourceFiles;

	
	public NodePropertiesDialog(){
		getContentPane().setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(12, 41, 46, 15);
		getContentPane().add(lblName);
		
		textField = new JTextField();
		textField.setBounds(76, 39, 114, 19);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblType = new JLabel("Type");
		lblType.setBounds(12, 68, 46, 15);
		getContentPane().add(lblType);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(76, 66, 114, 19);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(12, 12, 46, 15);
		getContentPane().add(lblId);
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(76, 10, 114, 19);
		getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		lblSourceFiles = new JLabel("Source Files");
		lblSourceFiles.setBounds(12, 107, 92, 15);
		getContentPane().add(lblSourceFiles);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(355, 230, 81, 25);
		getContentPane().add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(262, 230, 81, 25);
		getContentPane().add(btnSave);
		
	}
}
