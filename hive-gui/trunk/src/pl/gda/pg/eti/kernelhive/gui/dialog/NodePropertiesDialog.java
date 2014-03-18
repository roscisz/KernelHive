/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
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

import java.awt.Dimension;
import java.awt.Rectangle;
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
import pl.gda.pg.eti.kernelhive.common.source.IKernelFile;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public class NodePropertiesDialog extends JDialog {

	private static final long serialVersionUID = -7313937306855473619L;

	private final JTextField textFieldName;
	private final JTextField textFieldType;
	private final JTextField textFieldId;
	private final JLabel lblSourceFiles;
	private final JList list;
	private final MainFrame frame;
	private final GUIGraphNodeDecorator node;
	private final JTable table;

	public NodePropertiesDialog(final MainFrame frame,
			final GUIGraphNodeDecorator node) {
		super(frame);
		getContentPane().setSize(new Dimension(460, 500));
		getContentPane().setPreferredSize(new Dimension(460, 500));
		setPreferredSize(new Dimension(460, 500));
		setBounds(new Rectangle(getParent().getX(), getParent().getY(), 460,
				500));
		this.frame = frame;
		this.node = node;
		getContentPane().setLayout(null);

		final JLabel lblName = new JLabel("Name");
		lblName.setBounds(12, 41, 46, 15);
		getContentPane().add(lblName);

		textFieldName = new JTextField();
		textFieldName.setBounds(76, 39, 338, 19);
		getContentPane().add(textFieldName);
		textFieldName.setColumns(10);
		textFieldName.setText(node.getGraphNode().getName());

		final JLabel lblType = new JLabel("Type");
		lblType.setBounds(12, 68, 46, 15);
		getContentPane().add(lblType);

		textFieldType = new JTextField();
		textFieldType.setEditable(false);
		textFieldType.setBounds(76, 66, 338, 19);
		getContentPane().add(textFieldType);
		textFieldType.setColumns(10);
		textFieldType.setText(node.getGraphNode().getType().toString());

		final JLabel lblId = new JLabel("ID");
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

		final JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(333, 421, 81, 25);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				close();
			}
		});
		getContentPane().add(btnCancel);

		final JButton btnSave = new JButton("Save");
		btnSave.setBounds(240, 421, 81, 25);
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (save()) {
					close();
				} else {
					MessageDialog
							.showErrorDialog(NodePropertiesDialog.this,
									"Error",
									"Duplicate keys in graph node properties - save failed!");
				}
			}
		});
		getContentPane().add(btnSave);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(115, 97, 193, 106);
		getContentPane().add(scrollPane);

		list = new JList();
		scrollPane.setViewportView(list);

		final JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(322, 215, 92, 25);
		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final TableModel model = table.getModel();
				if (model instanceof PropertiesTableModel) {
					((PropertiesTableModel) model).addRow(new Object[2]);
					((PropertiesTableModel) model).fireTableDataChanged();
				}
			}
		});
		getContentPane().add(btnAdd);

		final JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(323, 252, 91, 25);
		btnRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final int rowIndex = table.getSelectedRow();
				final TableModel model = table.getModel();
				if (model instanceof PropertiesTableModel) {
					((PropertiesTableModel) model).removeRow(rowIndex);
					((PropertiesTableModel) model).fireTableDataChanged();
				}
			}
		});
		getContentPane().add(btnRemove);

		final JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(115, 215, 193, 181);
		getContentPane().add(scrollPane_1);

		table = new JTable();
		scrollPane_1.setViewportView(table);

		final JLabel lblProperties = new JLabel("Properties");
		lblProperties.setBounds(12, 220, 92, 15);
		getContentPane().add(lblProperties);

		final JButton btnDetails = new JButton("Details");
		btnDetails.setBounds(320, 97, 94, 25);
		btnDetails.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				displaySourceFileProperties();
			}
		});
		getContentPane().add(btnDetails);

		final JButton btnEdit = new JButton("Edit");
		btnEdit.setBounds(320, 134, 94, 25);
		btnEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final IKernelFile file = (IKernelFile) list.getSelectedValue();
				if (file != null) {
					NodePropertiesDialog.this.frame.getController().openTab(
							file.getFile());
				}
			}
		});
		getContentPane().add(btnEdit);

		fillSourceFilesList(node.getSourceFiles());
		fillPropertiesTable(node.getGraphNode().getProperties());
	}

	private void displaySourceFileProperties() {
		final IKernelFile file = (IKernelFile) list.getSelectedValue();
		if (file != null) {
			final SourceFilePropertiesDialog dialog = new SourceFilePropertiesDialog(
					frame, file);
			dialog.setVisible(true);
		}
	}

	private boolean save() {
		node.getGraphNode().setName(textFieldName.getText());
		if (saveProperties()) {
			frame.getController().saveProject();
			return true;
		}
		return false;
	}

	private boolean saveProperties() {
		final Map<String, Object> properties = new HashMap<String, Object>();
		final TableModel model = table.getModel();
		if (model.getColumnCount() == 2) {
			for (int i = 0; i < model.getRowCount(); i++) {
				final String key = (String) model.getValueAt(i, 0);
				final Object value = model.getValueAt(i, 1);
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

	private void fillSourceFilesList(final List<IKernelFile> sourceFiles) {
		final ListModel model = new SourceFilesListModel(sourceFiles);
		list.setModel(model);
		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 2) {
					e.consume();
					final IKernelFile file = (IKernelFile) list
							.getSelectedValue();
					frame.getController().openTab(file.getFile());
				}
			}

		});
	}

	private void fillPropertiesTable(final Map<String, Object> properties) {
		final TableModel model = new PropertiesTableModel(properties);
		table.setModel(model);
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		model.addTableModelListener(table);
	}

	private class PropertiesTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1731110689845777045L;
		private final List<Object[]> dynamicArray;

		public PropertiesTableModel(final Map<String, Object> properties) {
			dynamicArray = new ArrayList<Object[]>();
			final Set<String> keySet = properties.keySet();
			for (final String key : keySet) {
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
		public String getColumnName(final int columnIndex) {
			if (columnIndex == 0) {
				return "Key";
			} else if (columnIndex == 1) {
				return "Value";
			} else {
				return null;
			}
		}

		@Override
		public Class<?> getColumnClass(final int columnIndex) {
			return String.class;
		}

		@Override
		public boolean isCellEditable(final int rowIndex, final int columnIndex) {
			return true;
		}

		@Override
		public Object getValueAt(final int rowIndex, final int columnIndex) {
			if (rowIndex < dynamicArray.size() && columnIndex < 2) {
				return dynamicArray.get(rowIndex)[columnIndex];
			} else {
				return null;
			}
		}

		@Override
		public void setValueAt(final Object aValue, final int rowIndex,
				final int columnIndex) {
			if (rowIndex < dynamicArray.size() && columnIndex < 2) {
				dynamicArray.get(rowIndex)[columnIndex] = aValue;
			}
			for (final TableModelListener l : getTableModelListeners()) {
				l.tableChanged(new TableModelEvent(this, rowIndex, rowIndex,
						columnIndex, TableModelEvent.UPDATE));
			}
		}

		public void addRow(final Object[] row) {
			if (row.length == 2) {
				dynamicArray.add(row);
			} else {
				return;
			}
		}

		public void removeRow(final int rowIndex) {
			dynamicArray.remove(rowIndex);
		}
	}

	private class SourceFilesListModel implements ListModel {

		List<IKernelFile> list;
		List<ListDataListener> listDataListeners;

		public SourceFilesListModel(final List<IKernelFile> list) {
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
		public IKernelFile getElementAt(final int index) {
			if (list != null) {
				return list.get(index);
			} else {
				return null;
			}
		}

		@Override
		public void addListDataListener(final ListDataListener l) {
			if (!listDataListeners.contains(l)) {
				listDataListeners.add(l);
			}
		}

		@Override
		public void removeListDataListener(final ListDataListener l) {
			if (listDataListeners.contains(l)) {
				listDataListeners.remove(l);
			}
		}
	}
}
