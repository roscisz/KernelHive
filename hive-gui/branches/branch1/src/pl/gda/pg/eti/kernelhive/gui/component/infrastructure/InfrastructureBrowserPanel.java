package pl.gda.pg.eti.kernelhive.gui.component.infrastructure;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;

/**
 * 
 * @author marcel
 * 
 */
public class InfrastructureBrowserPanel extends JPanel {

	private static final long serialVersionUID = 5693611653576023015L;
	private final JTree tree;
	private TreeModel model;
	private final JButton btnRefresh;

	/**
	 * 
	 */
	public InfrastructureBrowserPanel() {
		setLayout(new BorderLayout(0, 0));

		final JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		btnRefresh = new JButton("Refresh");
		panel.add(btnRefresh);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(600, 500));
		add(scrollPane, BorderLayout.CENTER);
		model = new InfrastructureTreeModel(new ArrayList<ClusterInfo>());
		tree = new JTree(model);
		tree.setCellRenderer(new InfrastructureCellRenderer(tree
				.getCellRenderer()));
		scrollPane.setViewportView(tree);
	}

	private void fillInfrastructureTree(final List<ClusterInfo> infrastructure) {
		model = new InfrastructureTreeModel(infrastructure);
		tree.setModel(model);
		tree.updateUI();
	}

	public void addRefreshBtnActionListener(final ActionListener l) {
		btnRefresh.addActionListener(l);
	}

	public void removeRefreshBtnActionListener(final ActionListener l) {
		btnRefresh.removeActionListener(l);
	}

	public void reloadTreeContents(final List<ClusterInfo> infrastructure) {
		fillInfrastructureTree(infrastructure);
	}

}
