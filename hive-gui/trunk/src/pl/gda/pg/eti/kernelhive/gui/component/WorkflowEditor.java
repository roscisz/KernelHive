package pl.gda.pg.eti.kernelhive.gui.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.project.IProjectNode;
import pl.gda.pg.eti.kernelhive.gui.project.KernelHiveProject;
import pl.gda.pg.eti.kernelhive.gui.project.ProjectNode;
import pl.gda.pg.eti.kernelhive.gui.workflow.IWorkflowNode;
import pl.gda.pg.eti.kernelhive.gui.workflow.WorkflowGraphNode;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

public class WorkflowEditor extends JTabContent implements mxIEventListener,
		MouseListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1609157384680024544L;

	private mxGraphComponent graphComponent;
	private String name;
	private KernelHiveProject project;
	private JPopupMenu popup;

	public WorkflowEditor(MainFrame frame, String name,
			KernelHiveProject project) {
		super(frame);
		this.name = name;
		this.project = project;
		setName(this.name);
		setLayout(new BorderLayout());
		setupPopupMenu();

		 mxGraph graph = loadProject(project);

		// TODO remove
//		mxGraph graph = new mxGraph();
//		mxCell parent = (mxCell)graph.getDefaultParent();
//		graph.getModel().beginUpdate();
//		try {
//			mxCell v1 = (mxCell) graph.insertVertex(parent, "Node1",
//					new WorkflowGraphNode(new ProjectNode(project), "Node1"),
//					20, 20, 80, 30);
//			mxCell v2 = (mxCell) graph.insertVertex(parent, "Node2",
//					new WorkflowGraphNode(new ProjectNode(project), "Node2"),
//					240, 150, 80, 30);
//			graph.insertEdge(parent, "Edge", "", v1, v2);
//		} finally {
//			graph.getModel().endUpdate();
//		}
		//

		graphComponent = new mxGraphComponent(graph);
		graphComponent.getGraph().getModel().addListener(mxEvent.CHANGE, this);
		graphComponent.addKeyListener(this);
		graphComponent.getGraphControl().addMouseListener(this);
		graphComponent.add(popup);
		add(graphComponent);
	}

	private static mxGraph loadProject(KernelHiveProject project) {
		List<IProjectNode> projectNodes = project.getProjectNodes();
		Hashtable<IWorkflowNode, mxCell> mapping = new Hashtable<IWorkflowNode, mxCell>(projectNodes.size());
		
		mxGraph graph = new mxGraph();
		for(IProjectNode node : projectNodes){
			IWorkflowNode wfNode = node.getWorkflowNode();
			mxCell parent = (mxCell) graph.getDefaultParent();
			graph.getModel().beginUpdate();
			try{
				mxCell v1 =(mxCell) graph.insertVertex(parent, wfNode.getNodeId(), wfNode, wfNode.getX(), wfNode.getY(), 80, 30);
				mapping.put(wfNode, v1);
			} finally{
				graph.getModel().endUpdate();
			}
		}
		for(IProjectNode node : projectNodes){
			IWorkflowNode wfNode = node.getWorkflowNode();
			graph.getModel().beginUpdate();
			try{
				mxCell v = mapping.get(wfNode);
				if(wfNode.getParentNode()!=null){
					v.setParent(mapping.get(wfNode.getParentNode()));
				}				
			} finally{
				graph.getModel().endUpdate();
			}
		}
		for(IProjectNode node : projectNodes){
			IWorkflowNode wfNode = node.getWorkflowNode();
			graph.getModel().beginUpdate();
			try{
				mxCell v = mapping.get(wfNode);
				mxCell parent = (mxCell) v.getParent();
				List<IWorkflowNode> followingNodes = wfNode.getFollowingNodes();
				for(IWorkflowNode fNode : followingNodes){
					graph.insertEdge(parent, "", null, v, mapping.get(fNode));
				}
			} finally{
				graph.getModel().endUpdate();
			}
		}
		return graph;
	}

	// TODO i18n
	private void setupPopupMenu() {
		popup = new JPopupMenu();
		JMenuItem mi = new JMenuItem("Properties");
		mi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		popup.add(mi);
		mi = new JMenuItem("Delete");
		mi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mxCell cell = (mxCell) graphComponent.getGraph()
						.getSelectionCell();
				removeNode(cell);
			}
		});
		popup.add(mi);
	}

	@Override
	public boolean saveContent(File file) {
		try {
			project.save();
			return true;
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean loadContent(File file) {
		// TODO Auto-generated method stub
		return false;
	}

	private void removeNode(mxCell cell) {
		graphComponent.getGraph().getModel().beginUpdate();
		try {
			graphComponent.getGraph().getModel().remove(cell);
		} finally {
			graphComponent.getGraph().getModel().endUpdate();
		}
	}

	@Override
	public void invoke(Object sender, mxEventObject event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO test code
		if (e.getButton() == MouseEvent.BUTTON3) {
			mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
			if (cell != null) {
				popup.show(graphComponent, e.getX(), e.getY());
			}

		} else if (e.getButton() == MouseEvent.BUTTON2) {
			graphComponent.getGraph().getModel().beginUpdate();
			try {
				mxCell v = (mxCell) graphComponent.getGraph().insertVertex(
						graphComponent.getGraph().getDefaultParent(), "Node",
						null, e.getX(), e.getY(), 80, 30);
			} finally {
				graphComponent.getGraph().getModel().endUpdate();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
			mxCell cell = (mxCell) graphComponent.getGraph().getSelectionCell();
			removeNode(cell);
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {
	}
}