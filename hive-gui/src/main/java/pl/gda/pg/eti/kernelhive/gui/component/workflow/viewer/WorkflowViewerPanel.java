/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 * Copyright (c) 2014 Szymon Bultrowicz
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
package pl.gda.pg.eti.kernelhive.gui.component.workflow.viewer;

import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo.WorkflowState;
import pl.gda.pg.eti.kernelhive.gui.dialog.MessageDialog;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WorkflowViewerPanel extends JPanel {

	private static final long serialVersionUID = -2548380853826858288L;
	private final JTable table;
	private final JButton btnRefresh;
	private WorkflowExecutionsTableModel model;
	private JPopupMenu contextMenu;
	private List<WorkflowInfo> workflows;
	private WorkflowViewerHandler workflowViewerHandler;
	private int selectedRow;

	public WorkflowViewerPanel() {
		setLayout(new BorderLayout(0, 0));

		final JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		btnRefresh = new JButton("Refresh");
		btnRefresh.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(btnRefresh);

		final JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		scrollPane.setViewportView(table);

		prepareContextMenu();

		fillWorkflowExecutionsTable(null);
	}

	private void prepareContextMenu() {
		contextMenu = new JPopupMenu();
		JMenuItem showProgressMenuItem = new JMenuItem("Show progress");
		showProgressMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			if (workflows != null && selectedRow >= 0) {
				WorkflowInfo workflowInfo = workflows.get(selectedRow);
				if (workflowViewerHandler != null && workflowInfo != null) {
					workflowViewerHandler.showProgress(workflowInfo);
				}
			}
			}
		});
		contextMenu.add(showProgressMenuItem);

		JMenuItem workPreviewMenuItem = new JMenuItem("Preview work");
		workPreviewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			if (workflows != null && selectedRow >= 0) {
				WorkflowInfo workflowInfo = workflows.get(selectedRow);
				if (workflowViewerHandler != null && workflowInfo != null) {
					workflowViewerHandler.previewWork(workflowInfo);
				}
			}
			}
		});
		contextMenu.add(workPreviewMenuItem);

		JMenuItem terminateMenuItem = new JMenuItem("Terminate");
		terminateMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			if (workflows != null && selectedRow >= 0) {
				WorkflowInfo workflowInfo = workflows.get(selectedRow);
				if (workflowViewerHandler != null && workflowInfo != null) {
					Logger.getLogger(getClass().getName()).info("panel terminate");
					workflowViewerHandler.terminate(workflowInfo);
				}
			}
			}
		});
		contextMenu.add(terminateMenuItem);
	}

	private void fillWorkflowExecutionsTable(final List<WorkflowInfo> workflows) {
		this.workflows = workflows;
		model = new WorkflowExecutionsTableModel(workflows);
		table.setModel(model);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		model.addTableModelListener(table);
		addTableListeners();
	}

	private void addTableListeners() {
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent ev) {
				if (ev.getButton() == MouseEvent.BUTTON3 && !ev.isConsumed()) {
					int rowAtPoint = table.rowAtPoint(new Point(ev.getX(), ev.getY()));
					if (rowAtPoint >= 0 && rowAtPoint < table.getRowCount()) {
						table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
					} else {
						table.clearSelection();
					}
					selectedRow = table.getSelectedRow();
					if (selectedRow >= 0) {
						contextMenu.show(table, ev.getX(), ev.getY());
						ev.consume();
					}
				} else if (ev.getClickCount() == 2 && !ev.isConsumed()) {
					ev.consume();
					final int col = table.getSelectedColumn();
					final int row = table.getSelectedRow();
					if (model.isResultColumn(col)) {
						final String val = (String) table.getValueAt(row, col);
						try {
							final URL url = new URL(val);
							if (Desktop.isDesktopSupported()) {
								Desktop.getDesktop().browse(
										new URI(url.toExternalForm()));
								MessageDialog
										.showSuccessDialog(
										WorkflowViewerPanel.this,
										"Please wait",
										"Default internet browser will open to download the requested file...");
							} else {
								MessageDialog
										.showErrorDialog(
										WorkflowViewerPanel.this,
										"Error",
										"Java Dialog API not supported - could not open web browser");
							}
						} catch (final MalformedURLException e1) {
							e1.printStackTrace();
							MessageDialog.showErrorDialog(
									WorkflowViewerPanel.this, "Error",
									"The results data URL is invalid!");
						} catch (final IOException e) {
							e.printStackTrace();
							MessageDialog.showErrorDialog(
									WorkflowViewerPanel.this, "Error",
									"Could not open web browser!");
						} catch (final URISyntaxException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	public void addRefreshBtnActionListener(final ActionListener l) {
		btnRefresh.addActionListener(l);
	}

	public void removeRefreshBtnActionListener(final ActionListener l) {
		btnRefresh.removeActionListener(l);
	}

	public void reloadTableContents(final List<WorkflowInfo> workflows) {
		fillWorkflowExecutionsTable(workflows);
	}

	public void selectAllTableContents() {
		table.selectAll();
	}

	public void clearSelection() {
		table.clearSelection();
	}

	public void setWorkflowViewerHandler(WorkflowViewerHandler workflowViewerHandler) {
		this.workflowViewerHandler = workflowViewerHandler;
	}

	private class WorkflowExecutionsTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 4135149168329669058L;
		private final int columnSize = 5;
		private final String[] columnNames = new String[]{"ID", "Name",
			"Status", "Results", "Elapsed time"};
		@SuppressWarnings("rawtypes")
		private final Class[] columnClasses = new Class[]{Integer.class,
			String.class, WorkflowState.class, String.class, Double.class};
		List<Object[]> data = new ArrayList<>();

		public WorkflowExecutionsTableModel(final List<WorkflowInfo> workflows) {
			super();
			if (workflows != null) {
				for (final WorkflowInfo wi : workflows) {
					data.add(new Object[]{wi.ID, wi.name, wi.state, wi.result, wi.elapsedTime});
				}
			}
		}

		public boolean isResultColumn(final int col) {
			if (col == 3) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public int getColumnCount() {
			return columnSize;
		}

		@Override
		public Object getValueAt(final int rowIndex, final int columnIndex) {
			if (columnIndex < columnSize && rowIndex < data.size()) {
				return data.get(rowIndex)[columnIndex];
			} else {
				return null;
			}
		}

		@Override
		public String getColumnName(final int columnIndex) {
			if (columnIndex < columnSize) {
				return columnNames[columnIndex];
			} else {
				return null;
			}
		}

		@Override
		public Class<?> getColumnClass(final int columnIndex) {
			if (columnIndex < columnSize) {
				return columnClasses[columnIndex];
			} else {
				return null;
			}
		}

		@Override
		public boolean isCellEditable(final int rowIndex, final int columnIndex) {
			return false;
		}

		@Override
		public void setValueAt(final Object aValue, final int rowIndex,
				final int columnIndex) {
			if (rowIndex < data.size() && columnIndex < columnSize) {
				data.get(rowIndex)[columnIndex] = aValue;
			}
			for (final TableModelListener l : this.getTableModelListeners()) {
				l.tableChanged(new TableModelEvent(this, rowIndex, rowIndex,
						columnIndex, TableModelEvent.UPDATE));
			}
		}
	}
}
