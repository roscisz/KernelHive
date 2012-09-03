package pl.gda.pg.eti.kernelhive.gui.component.infrastructure;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JButton;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;

public class InfrastructureBrowserPanel extends JPanel {
	
	private static final long serialVersionUID = 5693611653576023015L;
	private JTree tree;
	private JButton btnRefresh;
	
	public InfrastructureBrowserPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		
		btnRefresh = new JButton("Refresh");
		panel.add(btnRefresh);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(600, 500));
		add(scrollPane, BorderLayout.CENTER);
		
		tree = new JTree();
		scrollPane.setViewportView(tree);
	}
	
	private void fillInfrastructureTree(List<ClusterInfo> infrastructure){
		
	}
	
	public void addRefreshBtnActionListener(ActionListener l){
		btnRefresh.addActionListener(l);
	}
	
	public void removeRefreshBtnActionListener(ActionListener l){
		btnRefresh.removeActionListener(l);
	}
	
	public void reloadTreeContents(List<ClusterInfo> infrastructure){
		fillInfrastructureTree(infrastructure);
	}

}
