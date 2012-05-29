package pl.gda.pg.eti.kernelhive.gui.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.project.IProject;
import pl.gda.pg.eti.kernelhive.gui.project.IProjectNode;
import pl.gda.pg.eti.kernelhive.gui.project.ProjectNode;
import pl.gda.pg.eti.kernelhive.gui.project.util.NodeIdGenerator;
import pl.gda.pg.eti.kernelhive.gui.workflow.IWorkflowNode;
import pl.gda.pg.eti.kernelhive.gui.workflow.WorkflowGraphNode;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;

public class WorkflowEditor extends JTabContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1609157384680024544L;

	private mxGraphComponent graphComponent;
	private String name;
	private IProject project;
	private JPopupMenu popup;
	private mxUndoManager undoManager;
	private mxIEventListener undoHandler;
	private mxIEventListener changeHandler;
	private mxIEventListener connectHandler;

	public WorkflowEditor(MainFrame frame, String name,
			IProject project) {
		super(frame);
		this.name = name;
		this.project = project;
		setName(this.name);
		setLayout(new BorderLayout());
		setupPopupMenu();

		undoManager = new mxUndoManager();
		undoHandler = new mxIEventListener() {

			@Override
			public void invoke(Object sender, mxEventObject evt) {
				undoManager.undoableEditHappened((mxUndoableEdit) evt
						.getProperty("edit"));
			}
		};
		changeHandler = new mxIEventListener() {

			@Override
			public void invoke(Object sender, mxEventObject evt) {
				setDirty(true);
			}
		};
		connectHandler = new mxIEventListener() {
			
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				mxCell edge = (mxCell) evt.getProperty("cell");
				//TODO join two nodes
			}
		};

		mxGraph graph = loadProject(project);

		graphComponent = new mxGraphComponent(graph);
		graphComponent.getGraph().getModel()
				.addListener(mxEvent.CHANGE, changeHandler);
		graphComponent.getGraph().getModel()
				.addListener(mxEvent.UNDO, undoHandler);
		graphComponent.getGraph().getView()
				.addListener(mxEvent.UNDO, undoHandler);
		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, connectHandler);
		graphComponent.getGraph().setAllowLoops(false);
		graphComponent.add(popup);
		add(graphComponent);

		mxIEventListener undoHandler = new mxIEventListener() {

			@Override
			public void invoke(Object sender, mxEventObject evt) {
				List<mxUndoableChange> changes = ((mxUndoableEdit) evt
						.getProperty("edit")).getChanges();
				graphComponent.getGraph().setSelectionCells(
						graphComponent.getGraph().getSelectionCellsForChanges(
								changes));
			}
		};
		undoManager.addListener(mxEvent.UNDO, undoHandler);
		undoManager.addListener(mxEvent.REDO, undoHandler);

		installListeners();
	}

	private static mxGraph loadProject(IProject project) {
		List<IProjectNode> projectNodes = project.getProjectNodes();
		Hashtable<IWorkflowNode, mxCell> mapping = new Hashtable<IWorkflowNode, mxCell>(
				projectNodes.size());

		mxGraph graph = new mxGraph();
		// initial load of all nodes
		for (IProjectNode node : projectNodes) {
			IWorkflowNode wfNode = node.getWorkflowNode();
			mxCell parent = (mxCell) graph.getDefaultParent();
			graph.getModel().beginUpdate();
			try {
				mxCell v1 = (mxCell) graph.insertVertex(parent,
						wfNode.getNodeId(), wfNode, wfNode.getX(),
						wfNode.getY(), 80, 30);
				mapping.put(wfNode, v1);
			} finally {
				graph.getModel().endUpdate();
			}
		}
		// linking nodes to their parents
		for (IProjectNode node : projectNodes) {
			IWorkflowNode wfNode = node.getWorkflowNode();
			graph.getModel().beginUpdate();
			try {
				mxCell v = mapping.get(wfNode);
				if (wfNode.getParentNode() != null) {
					v.setParent(mapping.get(wfNode.getParentNode()));
				}
			} finally {
				graph.getModel().endUpdate();
			}
		}
		// linking following nodes
		for (IProjectNode node : projectNodes) {
			IWorkflowNode wfNode = node.getWorkflowNode();
			graph.getModel().beginUpdate();
			try {
				mxCell v = mapping.get(wfNode);
				mxCell parent = (mxCell) v.getParent();
				List<IWorkflowNode> followingNodes = wfNode.getFollowingNodes();
				for (IWorkflowNode fNode : followingNodes) {
					graph.insertEdge(parent, "", null, v, mapping.get(fNode));
				}
			} finally {
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
				removeNode(graphComponent.getGraph(), cell);
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

	private void removeNode(mxGraph graph, mxCell cell) {
		graph.getModel().beginUpdate();
		try {
			graph.getModel().remove(cell);
		} finally {
			graph.getModel().endUpdate();
		}
	}

	private void addNode(mxGraph graph, Object parent, String id, Object value,
			double x, double y, double width, double height) {
		graph.getModel().beginUpdate();
		try {
			graph.insertVertex(parent, id, value, x, y, width, height);
		} finally {
			graph.getModel().endUpdate();
		}
	}

	@Override
	public void redoAction() {
		undoManager.redo();
	}

	@Override
	public void undoAction() {
		undoManager.undo();
	}

	private void installListeners() {
		MouseWheelListener wheelTracker = new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getSource() instanceof mxGraphComponent
						&& e.isControlDown()) {
					if (e.getWheelRotation() < 0) {
						graphComponent.zoomIn();
					} else {
						graphComponent.zoomOut();
					}
				}

			}
		};
		graphComponent.addMouseWheelListener(wheelTracker);

		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(),
							e.getY());
					if (cell != null && cell.isVertex()) {
						popup.show(graphComponent, e.getX(), e.getY());
					}

				} else if (e.getButton() == MouseEvent.BUTTON2) {//XXX test
																	// code
					IProjectNode pNode = new ProjectNode(project);
					IWorkflowNode wNode = new WorkflowGraphNode(pNode, NodeIdGenerator.generateId());
					graphComponent.getGraph().getModel().beginUpdate();
					try {
						addNode(graphComponent.getGraph(), graphComponent
								.getGraph().getDefaultParent(), wNode.getNodeId(), wNode, e
								.getX(), e.getY(), 80, 30);
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
		});

		KeyListener keyTracker = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
					mxCell cell = (mxCell) graphComponent.getGraph()
							.getSelectionCell();
					removeNode(graphComponent.getGraph(), cell);
				} else if (ke.getKeyCode() == KeyEvent.VK_U) {
					undoAction();
				} else if (ke.getKeyCode() == KeyEvent.VK_R) {
					redoAction();
				}
			}

			@Override
			public void keyTyped(KeyEvent ke) {
			}
		};
		graphComponent.addKeyListener(keyTracker);
	}

	@Override
	public void cut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void copy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void paste() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAll() {
		graphComponent.getGraph().selectAll();
	}
}