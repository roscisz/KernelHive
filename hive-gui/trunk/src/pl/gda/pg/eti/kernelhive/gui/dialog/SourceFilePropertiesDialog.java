package pl.gda.pg.eti.kernelhive.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;

import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Rectangle;

public class SourceFilePropertiesDialog extends JDialog {

	private static final long serialVersionUID = 1825937840391440307L;
	
	private ISourceFile sourceFile;
	private JTextField textField;
	private JTable table;
	
	public SourceFilePropertiesDialog(Dialog dialog, ISourceFile sourceFile){
		super(dialog);
		this.sourceFile = sourceFile;
		init();
	}
	
	private void init(){
		setBounds(new Rectangle(getParent().getX(), getParent().getY(), 460, 380));
		getContentPane().setLayout(null);
		
		JLabel lblId = new JLabel("ID");
		lblId.setHorizontalAlignment(SwingConstants.CENTER);
		lblId.setBounds(12, 12, 70, 15);
		getContentPane().add(lblId);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(116, 10, 320, 19);
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(sourceFile.getId());
		
		JLabel lblProperties = new JLabel("Properties");
		lblProperties.setBounds(12, 80, 86, 15);
		getContentPane().add(lblProperties);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(116, 80, 198, 175);
		getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TableModel model = table.getModel();
				if (model instanceof PropertiesTableModel) {
					((PropertiesTableModel) model).addRow(new Object[2]);
					((PropertiesTableModel) model).fireTableDataChanged();
				}
			}
		});
		btnAdd.setBounds(326, 75, 110, 25);
		getContentPane().add(btnAdd);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int rowIndex = table.getSelectedRow();
				TableModel model = table.getModel();
				if (model instanceof PropertiesTableModel) {
					((PropertiesTableModel) model).removeRow(rowIndex);
					((PropertiesTableModel) model).fireTableDataChanged();
				}
			}
		});
		btnDelete.setBounds(326, 112, 110, 25);
		getContentPane().add(btnDelete);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		btnCancel.setBounds(326, 300, 110, 25);
		getContentPane().add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (save()) {
					close();
				} else {
					MessageDialog.showErrorDialog(SourceFilePropertiesDialog.this, "Error", "Duplicate keys in kernel properties - save failed!");
				}
			}
		});
		btnSave.setBounds(209, 300, 110, 25);
		getContentPane().add(btnSave);
		
		fillPropertiesTable(sourceFile.getProperties());
	}
	
	public SourceFilePropertiesDialog(Frame frame, ISourceFile sourceFile){
		super(frame);
		this.sourceFile = sourceFile;
		
		init();
	}
	
	private void close() {
		this.setVisible(false);
		this.dispose();
	}

	private boolean save() {
		return saveProperties();
	}

	private boolean saveProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		TableModel model = table.getModel();
		if (model.getColumnCount() == 2) {
			for (int i = 0; i < model.getRowCount(); i++) {
				String key = (String) model.getValueAt(i, 0);
				Object value = model.getValueAt(i, 1);
				if (properties.containsKey(key)) {
					return false;
				} else {
					properties.put(key, value);
				}
			}
			sourceFile.setProperties(properties);
			return true;
		} else {
			return false;
		}
	}

	private void fillPropertiesTable(Map<String, Object> properties){
		TableModel model = new PropertiesTableModel(properties);
		table.setModel(model);
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		model.addTableModelListener(table);
	}
	
	private class PropertiesTableModel extends AbstractTableModel{
		private List<Object[]> dynamicArray;

		public PropertiesTableModel(Map<String, Object> properties) {
			dynamicArray = new ArrayList<Object[]>();
			Set<String> keySet = properties.keySet();
			for (String key : keySet) {
				dynamicArray.add(new Object[] { key, properties.get(key) });
			}
		}

		@Override
		public int getRowCount() {
			return dynamicArray.size();
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "Key";
			} else if (columnIndex == 1) {
				return "Value";
			} else {
				return null;
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex < dynamicArray.size() && columnIndex < 2) {
				return dynamicArray.get(rowIndex)[columnIndex];
			} else {
				return null;
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (rowIndex < dynamicArray.size() && columnIndex < 2) {
				dynamicArray.get(rowIndex)[columnIndex] = aValue;
			}
			for (TableModelListener l : getTableModelListeners()) {
				l.tableChanged(new TableModelEvent(this, rowIndex, rowIndex,
						columnIndex, TableModelEvent.UPDATE));
			}
		}

		public void addRow(Object[] row) {
			if (row.length == 2) {
				dynamicArray.add(row);
			} else {
				return;
			}
		}

		public void removeRow(int rowIndex) {
			dynamicArray.remove(rowIndex);
		}
	}
}
