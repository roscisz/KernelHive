package pl.gda.pg.eti.kernelhive.gui.component.workflow.viewer;

import java.awt.BorderLayout;
import java.awt.Desktop;
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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.gui.dialog.MessageDialog;

/**
 * 
 * @author marcel
 * 
 */
public class WorkflowViewerPanel extends JPanel {

	private static final long serialVersionUID = -2548380853826858288L;
	private final JTable table;
	private final JButton btnRefresh;

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

		fillWorkflowExecutionsTable(null);
	}

	private void fillWorkflowExecutionsTable(final List<WorkflowInfo> workflows) {
		final WorkflowExecutionsTableModel model = new WorkflowExecutionsTableModel(
				workflows);
		table.setModel(model);
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		model.addTableModelListener(table);
		addTableListeners();
	}

	private void addTableListeners() {
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent ev) {
				if (ev.getClickCount() > 2) {
					ev.consume();
					final int col = table.getSelectedColumn();
					final int row = table.getSelectedRow();
					final String val = (String) table.getValueAt(row, col);
					try {
						final URL url = new URL(val);
						if (Desktop.isDesktopSupported()) {
							Desktop.getDesktop().browse(
									new URI(url.toExternalForm()));
						} else {
							MessageDialog
									.showErrorDialog(WorkflowViewerPanel.this,
											"Error",
											"Java Dialog API not supported - could not open web browser");
						}
					} catch (final MalformedURLException e1) {
						// silent exception, continue
					} catch (final IOException e) {
						e.printStackTrace();
						MessageDialog.showErrorDialog(WorkflowViewerPanel.this,
								"Error", "Could not open web browser!");
					} catch (final URISyntaxException e) {
						e.printStackTrace();
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

	private class WorkflowExecutionsTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 4135149168329669058L;
		private final int columnSize = 4;
		private final String[] columnNames = new String[] { "ID", "Name",
				"Status", "Results" };
		@SuppressWarnings("rawtypes")
		private final Class[] columnClasses = new Class[] { Integer.class,
				String.class, String.class, String.class };
		List<Object[]> data = new ArrayList<Object[]>();

		public WorkflowExecutionsTableModel(final List<WorkflowInfo> workflows) {
			super();
			if (workflows != null) {
				for (final WorkflowInfo wi : workflows) {
					data.add(new Object[] { wi.ID, wi.name, wi.state, wi.result });
				}
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
