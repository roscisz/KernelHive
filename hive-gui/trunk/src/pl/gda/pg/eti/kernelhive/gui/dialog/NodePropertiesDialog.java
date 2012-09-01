package pl.gda.pg.eti.kernelhive.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import java.awt.Rectangle;
import java.awt.Dimension;

/**
 * graph node properties dialog
 * 
 * @author mschally
 * 
 */
public class NodePropertiesDialog extends JDialog {

	private static final long serialVersionUID = -7313937306855473619L;

	private JTextField textFieldName;
	private JTextField textFieldType;
	private JTextField textFieldId;
	private JLabel lblSourceFiles;
	private JList list;
	private MainFrame frame;
	private GUIGraphNodeDecorator node;
	private JTable table;

	public NodePropertiesDialog(MainFrame frame, GUIGraphNodeDecorator node) {
		super(frame);
		getContentPane().setSize(new Dimension(460, 500));
		getContentPane().setPreferredSize(new Dimension(460, 500));
		setPreferredSize(new Dimension(460, 500));
		setBounds(new Rectangle(getParent().getX(), getParent().getY(), 460, 500));
		this.frame = frame;
		this.node = node;
		getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Name");
		lblName.setBounds(12, 41, 46, 15);
		getContentPane().add(lblName);

		textFieldName = new JTextField();
		textFieldName.setBounds(76, 39, 338, 19);
		getContentPane().add(textFieldName);
		textFieldName.setColumns(10);
		textFieldName.setText(node.getGraphNode().getName());

		JLabel lblType = new JLabel("Type");
		lblType.setBounds(12, 68, 46, 15);
		getContentPane().add(lblType);

		textFieldType = new JTextField();
		textFieldType.setEditable(false);
		textFieldType.setBounds(76, 66, 338, 19);
		getContentPane().add(textFieldType);
		textFieldType.setColumns(10);
		textFieldType.setText(node.getGraphNode().getType().toString());

		JLabel lblId = new JLabel("ID");
		lblId.setBounds(12, 12, 46, 15);
		getContentPane().add(lblId);

		textFieldId = new JTextField();
		textFieldId.setEditable(false);
		textFieldId.setBounds(76, 10, 338, 19);
		getContentPane().add(textFieldId);
		textFieldId.setColumns(10);
		textFieldId.setText(node.getGraphNode().getNodeId());

		lblSourceFiles = new JLabel("Source Files");
		lblSourceFiles.setBounds(12, 107, 92, 15);
		getContentPane().add(lblSourceFiles);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(333, 421, 81, 25);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		getContentPane().add(btnCancel);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(240, 421, 81, 25);
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (save()) {
					close();
				} else {
					MessageDialog.showErrorDialog(NodePropertiesDialog.this, "Error", "Duplicate keys in graph node properties - save failed!");
				}
			}
		});
		getContentPane().add(btnSave);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(115, 97, 193, 106);
		getContentPane().add(scrollPane);

		list = new JList();
		scrollPane.setViewportView(list);

		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(322, 215, 92, 25);
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
		getContentPane().add(btnAdd);

		JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(323, 252, 91, 25);
		btnRemove.addActionListener(new ActionListener() {

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
		getContentPane().add(btnRemove);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(115, 215, 193, 181);
		getContentPane().add(scrollPane_1);

		table = new JTable();
		scrollPane_1.setViewportView(table);

		JLabel lblProperties = new JLabel("Properties");
		lblProperties.setBounds(12, 220, 92, 15);
		getContentPane().add(lblProperties);
		
		JButton btnDetails = new JButton("Details");
		btnDetails.setBounds(320, 97, 94, 25);
		btnDetails.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				displaySourceFileProperties();
			}
		});
		getContentPane().add(btnDetails);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.setBounds(320, 134, 94, 25);
		btnEdit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ISourceFile file = (ISourceFile) list.getSelectedValue();
				NodePropertiesDialog.this.frame.getController().openTab(file.getFile());
			}
		});
		getContentPane().add(btnEdit);
		
		fillSourceFilesList(node.getSourceFiles());
		fillPropertiesTable(node.getGraphNode().getProperties());
	}
	
	private void displaySourceFileProperties(){
		ISourceFile file = (ISourceFile) list.getSelectedValue();
		if(file!=null){
			SourceFilePropertiesDialog dialog = new SourceFilePropertiesDialog(this, file);
			dialog.setVisible(true);
		}
	}

	private boolean save() {
		node.getGraphNode().setName(textFieldName.getText());
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
			node.getGraphNode().setProperties(properties);
			return true;
		} else {
			return false;
		}
	}

	private void close() {
		this.setVisible(false);
		this.dispose();
	}

	private void fillSourceFilesList(List<ISourceFile> sourceFiles) {
		ListModel model = new SourceFilesListModel(sourceFiles);
		list.setModel(model);
		list.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					e.consume();
					ISourceFile file = (ISourceFile) list.getSelectedValue();
					frame.getController().openTab(file.getFile());
				}
			}

		});
	}

	private void fillPropertiesTable(Map<String, Object> properties) {
		TableModel model = new PropertiesTableModel(properties);
		table.setModel(model);
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		model.addTableModelListener(table);
	}

	private class PropertiesTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1731110689845777045L;
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

	private class SourceFilesListModel implements ListModel {

		List<ISourceFile> list;
		List<ListDataListener> listDataListeners;

		public SourceFilesListModel(List<ISourceFile> list) {
			this.list = list;
			listDataListeners = new ArrayList<ListDataListener>();
		}

		@Override
		public int getSize() {
			if (list != null) {
				return list.size();
			} else {
				return -1;
			}
		}

		@Override
		public ISourceFile getElementAt(int index) {
			if (list != null) {
				return list.get(index);
			} else {
				return null;
			}
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			if (!listDataListeners.contains(l)) {
				listDataListeners.add(l);
			}
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			if (listDataListeners.contains(l)) {
				listDataListeners.remove(l);
			}
		}
	}
}
