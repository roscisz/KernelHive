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

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import pl.gda.pg.eti.kernelhive.common.source.IKernelFile;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public class SourceFilePropertiesDialog extends JDialog {

	private static final long serialVersionUID = 1825937840391440307L;

	private final IKernelFile sourceFile;
	private JTextField textField;
	private JTable table;
	private MainFrame frame;

	public SourceFilePropertiesDialog(final MainFrame frame,
			final IKernelFile sourceFile) {
		super(frame);
		this.frame = frame;
		this.sourceFile = sourceFile;
		init();
	}

	private void init() {
		setBounds(new Rectangle(getParent().getX(), getParent().getY(), 460,
				380));
		getContentPane().setLayout(null);

		final JLabel lblId = new JLabel("ID");
		lblId.setHorizontalAlignment(SwingConstants.CENTER);
		lblId.setBounds(12, 12, 70, 15);
		getContentPane().add(lblId);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(116, 10, 320, 19);
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(sourceFile.getId());

		final JLabel lblProperties = new JLabel("Properties");
		lblProperties.setBounds(12, 80, 86, 15);
		getContentPane().add(lblProperties);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(116, 80, 198, 175);
		getContentPane().add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		final JButton btnAdd = new JButton("Add");
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
		btnAdd.setBounds(326, 75, 110, 25);
		getContentPane().add(btnAdd);

		final JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {

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
		btnDelete.setBounds(326, 112, 110, 25);
		getContentPane().add(btnDelete);

		final JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				close();
			}
		});
		btnCancel.setBounds(326, 300, 110, 25);
		getContentPane().add(btnCancel);

		final JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (save()) {
					close();
				} else {
					MessageDialog
							.showErrorDialog(SourceFilePropertiesDialog.this,
									"Error",
									"Duplicate keys in kernel properties - save failed!");
				}
			}
		});
		btnSave.setBounds(209, 300, 110, 25);
		getContentPane().add(btnSave);

		fillPropertiesTable(sourceFile.getProperties());
	}

	public SourceFilePropertiesDialog(final Frame frame,
			final IKernelFile sourceFile) {
		super(frame);
		this.sourceFile = sourceFile;

		init();
	}

	private void close() {
		this.setVisible(false);
		this.dispose();
	}

	private boolean save() {
		if (saveProperties()) {
			frame.getController().saveProject();
			return true;
		} else {
			return false;
		}
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
			sourceFile.setProperties(properties);
			return true;
		} else {
			return false;
		}
	}

	private void fillPropertiesTable(final Map<String, Object> properties) {
		final TableModel model = new PropertiesTableModel(properties);
		table.setModel(model);
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		model.addTableModelListener(table);
	}

	private class PropertiesTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -1313805087006665662L;

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
}
