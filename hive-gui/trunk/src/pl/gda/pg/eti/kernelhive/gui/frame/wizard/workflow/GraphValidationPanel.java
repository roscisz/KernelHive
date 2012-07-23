package pl.gda.pg.eti.kernelhive.gui.frame.wizard.workflow;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import java.awt.Dimension;

public class GraphValidationPanel extends JPanel {
	private static final long serialVersionUID = -5013549974156444286L;
	
	private JTable table;
	private JButton btnRevalidate;
	
	public GraphValidationPanel() {
		setSize(new Dimension(450, 300));
		setPreferredSize(new Dimension(450, 300));
		setMinimumSize(new Dimension(450, 300));
		setLayout(null);
		
		JLabel lblValidationResults = new JLabel("Validation Results");
		lblValidationResults.setBounds(12, 12, 137, 15);
		add(lblValidationResults);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 39, 395, 212);
		add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		btnRevalidate = new JButton("Revalidate");
		btnRevalidate.setBounds(12, 263, 117, 25);
		add(btnRevalidate);
	}
	
	void addButtonActionListener(ActionListener l){
		btnRevalidate.addActionListener(l);
	}
	
	public void displayGraphValidationResults(List<ValidationResult> validationResults){
		table.setModel(new ValidationResultsTableModel(validationResults));
	}
	
	private class ValidationResultsTableModel extends AbstractTableModel{

		private static final long serialVersionUID = 3267983913177896245L;
		
		private List<Object[]> data;
		
		public ValidationResultsTableModel(List<ValidationResult> validationResults){
			data = new ArrayList<Object[]>(validationResults.size());
			for(ValidationResult vr : validationResults){
				data.add(new Object[] {vr.getMesssage(), vr.getType()});
			}
		}
		
		@Override
		public String getColumnName(int index){
			if(index==0){
				return "Message";
			} else if(index==1){
				return "Result";
			} else{
				return null;
			}
		}
		
		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(columnIndex<2&&rowIndex<data.size()){
				return data.get(rowIndex)[columnIndex];
			} else{
				 return null;
			}
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if(columnIndex==0){
				return String.class;
			} else if(columnIndex==1){
				return ValidationResult.class;
			} else{
				return null;
			}
		}		
	}
}
