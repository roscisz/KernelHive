package pl.gda.pg.eti.kernelhive.gui.component.workflow;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.frame.NodePropertiesDialog;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.impl.GenericGraphNode;
import pl.gda.pg.eti.kernelhive.gui.project.IProject;
import pl.gda.pg.eti.kernelhive.common.graph.node.util.NodeIdGenerator;
import pl.gda.pg.eti.kernelhive.common.graph.node.util.NodeNameGenerator;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel.mxChildChange;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;

/**
 * 
 * @author mschally
 * 
 */
public class WorkflowEditor extends JTabContent {

	public static enum WorkflowGraphLayout {
		VERTICAL_HIERARCHICAL("verticalHierarchical"), HORIZONTAL_HIERARCHICAL(
				"horizontalHierarchical"), VERTICAL_TREE("verticalTree"), HORIZONTAL_TREE(
				"horizontalTree"), PARALLEL_EDGES("parallelEdges"), PLACE_EDGE_LABELS(
				"placeEdgeLabels"), ORGANIC_LAYOUT("organicLayout"), VERTICAL_PARTITION(
				"verticalPartition"), HORIZONTAL_PARTITION(
				"horizontalPartition"), VERTICAL_STACK("verticalStack"), HORIZONTAL_STACK(
				"horizontalStack"), CIRCLE_LAYOUT("circleLayout");

		private final String ident;

		WorkflowGraphLayout(String ident) {
			this.ident = ident;
		}

		public String getIdent() {
			return ident;
		}
	}

	private static final long serialVersionUID = 1609157384680024544L;
	private static final Logger LOG = Logger.getLogger(WorkflowEditor.class
			.getName());

	private mxGraphComponent graphComponent;
	private String name;
	private IProject project;
	private JPopupMenu nodePopup;
	private JPopupMenu workspacePopup;
	private mxUndoManager undoManager;
	private mxIEventListener undoHandler;
	private mxIEventListener changeHandler;
	private mxIEventListener connectHandler;
	private mxRubberband rubberband;

	public WorkflowEditor(MainFrame frame, final String name, IProject project) {
		super(frame);
		this.name = name;
		this.project = project;
		setName(this.name);
		setLayout(new BorderLayout());
		setupNodePopupMenu();
		setupWorkspacePopup();
		
		undoManager = new mxUndoManager();
		mxGraph graph = loadProject(project);
		graphComponent = new mxGraphComponent(graph);
		graphComponent.getGraph().setAllowLoops(false);
		graphComponent.add(nodePopup);
		graphComponent.add(workspacePopup);
		add(graphComponent);
		rubberband = new mxRubberband(graphComponent);

		installListeners();
	}

	private void updateProjectGraphAccordingToChange(mxUndoableEdit edit) {
		List<mxUndoableChange> changes = edit.getChanges();
		for (mxUndoableChange change : changes) {
			if (change instanceof mxChildChange) {
				mxChildChange childChange = (mxChildChange) change;
				mxCell cell = (mxCell) childChange.getChild();
				if (cell.isVertex()) {// vertex
					IGraphNode node = (IGraphNode) cell.getValue();
					if (childChange.getPrevious() == null) {// restore
						project.addProjectNode(node);
						for (int i = 0; i < cell.getEdgeCount(); i++) {
							mxICell source = cell.getEdgeAt(i)
									.getTerminal(true);
							mxICell end = cell.getEdgeAt(i).getTerminal(false);
							if (cell.equals(source)) {// add following node
								node.addFollowingNode(((IGraphNode) end
										.getValue()));
							} else {
								node.addPreviousNode(((IGraphNode) source
										.getValue()));
							}
						}
						
						// restore children nodes TODO FIXME XXX reccurent
						// in-depth resolving
					} else if (childChange.getPrevious() != null) {// destroy
						Iterator<IGraphNode> iter = node.getChildrenNodes().iterator();
						while(iter.hasNext()){
							iter.next();
							iter.remove();
						}
						node.setParentNode(null);
						iter = node.getFollowingNodes().iterator();
						while(iter.hasNext()){
							iter.next();
							iter.remove();
						}
						iter = node.getPreviousNodes().iterator();
						while(iter.hasNext()){
							iter.next();
							iter.remove();
						}
						
						project.removeProjectNode(node, false);
					}
				} else {// edge
					mxICell source = cell.getSource();
					mxICell target = cell.getTarget();
					IGraphNode sourceNode = (IGraphNode) source.getValue();
					IGraphNode targetNode = (IGraphNode) target.getValue();

					if (childChange.getPrevious() == null) {// restore
						sourceNode.addFollowingNode(targetNode);
					} else if (childChange.getPrevious() != null) {// destroy
						sourceNode.removeFollowingNode(targetNode);
					}
				}
			}
		}
	}

	private static mxGraph loadProject(IProject project) {
		List<IGraphNode> projectNodes = project.getProjectNodes();
		Hashtable<IGraphNode, mxCell> mapping = new Hashtable<IGraphNode, mxCell>(
				projectNodes.size());

		mxGraph graph = new mxGraph();
		// initial load of all nodes
		for (IGraphNode node : projectNodes) {
			mxCell parent = (mxCell) graph.getDefaultParent();
			graph.getModel().beginUpdate();
			try {
				mxCell v1 = (mxCell) graph.insertVertex(parent,
						node.getNodeId(), node, node.getX(), node.getY(), 80,
						30);
				mapping.put(node, v1);
			} finally {
				graph.getModel().endUpdate();
			}
		}
		// linking nodes to their parents
		for (IGraphNode node : projectNodes) {
			graph.getModel().beginUpdate();
			try {
				mxCell v = mapping.get(node);
				if (node.getParentNode() != null) {
					v.setParent(mapping.get(node.getParentNode()));
				}
			} finally {
				graph.getModel().endUpdate();
			}
		}
		// linking following nodes
		for (IGraphNode node : projectNodes) {
			graph.getModel().beginUpdate();
			try {
				mxCell v = mapping.get(node);
				mxCell parent = (mxCell) v.getParent();
				List<IGraphNode> followingNodes = node.getFollowingNodes();
				for (IGraphNode fNode : followingNodes) {
					graph.insertEdge(parent, "", null, v, mapping.get(fNode));
				}
			} finally {
				graph.getModel().endUpdate();
			}
		}
		return graph;
	}

	// TODO i18n
	private void setupNodePopupMenu() {
		nodePopup = new JPopupMenu();
		JMenuItem mi = new JMenuItem("Properties");
		mi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				mxCell cell = (mxCell) graphComponent.getGraph()
						.getSelectionCell();
				if (cell != null) {
					IGraphNode node = (IGraphNode) cell.getValue();
					NodePropertiesDialog npd = new NodePropertiesDialog(getFrame(), node);
					npd.setVisible(true);
					npd.addWindowListener(new WindowAdapter() {
						public void windowClosed(WindowEvent e){
							refresh();
						}
						
						public void windowClosing(WindowEvent e){
							refresh();
						}
					});
				}
			}
		});
		nodePopup.add(mi);
		mi = new JMenuItem("Delete");
		mi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mxCell cell = (mxCell) graphComponent.getGraph()
						.getSelectionCell();
				removeNode(graphComponent.getGraph(), cell);
			}
		});
		nodePopup.add(mi);
	}

	// TODO i18n
	private void setupWorkspacePopup() {
		workspacePopup = new JPopupMenu();
		JMenu layoutMenu = new JMenu("Layout");
		JMenuItem verticalHierarchical = new JMenuItem("Vertical Hierarchical");
		verticalHierarchical.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setGraphLayout(WorkflowGraphLayout.VERTICAL_HIERARCHICAL, true);
			}
		});
		layoutMenu.add(verticalHierarchical);
		JMenuItem horizontalHierarchical = new JMenuItem(
				"Horizontal Hierarchical");
		horizontalHierarchical.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGraphLayout(WorkflowGraphLayout.HORIZONTAL_HIERARCHICAL,
						true);
			}
		});
		layoutMenu.add(horizontalHierarchical);
		JMenuItem verticalTree = new JMenuItem("Vertical Tree");
		verticalTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGraphLayout(WorkflowGraphLayout.VERTICAL_TREE, true);
			}
		});
		layoutMenu.add(verticalTree);
		JMenuItem horizontalTree = new JMenuItem("Horizontal Tree");
		horizontalTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGraphLayout(WorkflowGraphLayout.HORIZONTAL_TREE, true);
			}
		});
		layoutMenu.add(horizontalTree);
		JMenuItem parallelEdges = new JMenuItem("Parallel Edges");
		parallelEdges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGraphLayout(WorkflowGraphLayout.PARALLEL_EDGES, true);
			}
		});
		layoutMenu.add(parallelEdges);
		JMenuItem placeEdgeLabels = new JMenuItem("Place Edge Labels");
		placeEdgeLabels.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGraphLayout(WorkflowGraphLayout.PLACE_EDGE_LABELS, true);
			}
		});
		layoutMenu.add(placeEdgeLabels);
		JMenuItem organicLayout = new JMenuItem("Organic Layout");
		organicLayout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGraphLayout(WorkflowGraphLayout.ORGANIC_LAYOUT, true);
			}
		});
		JMenuItem verticalPartition = new JMenuItem("Vertical Partition");
		verticalPartition.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGraphLayout(WorkflowGraphLayout.VERTICAL_PARTITION, true);
			}
		});
		layoutMenu.add(verticalPartition);
		JMenuItem horizontalPartition = new JMenuItem("Horizontal Partition");
		horizontalPartition.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGraphLayout(WorkflowGraphLayout.HORIZONTAL_PARTITION, true);
			}
		});
		layoutMenu.add(horizontalPartition);
		JMenuItem verticalStack = new JMenuItem("Vertical Stack");
		verticalStack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGraphLayout(WorkflowGraphLayout.VERTICAL_STACK, true);
			}
		});
		layoutMenu.add(verticalStack);
		JMenuItem horizontalStack = new JMenuItem("Horizontal Stack");
		horizontalStack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGraphLayout(WorkflowGraphLayout.HORIZONTAL_STACK, true);
			}
		});
		layoutMenu.add(horizontalStack);
		JMenuItem circleLayout = new JMenuItem("Circle Layout");
		circleLayout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGraphLayout(WorkflowGraphLayout.CIRCLE_LAYOUT, true);
			}
		});
		layoutMenu.add(circleLayout);
		workspacePopup.add(layoutMenu);
	}

	@Override
	public boolean saveContent(File file) {
		try {
			project.save();
			setDirty(false);
			if (getTabPanel() != null) {
				getTabPanel().getLabel().setText(name);
			}
			return true;
		} catch (ConfigurationException e) {
			LOG.warning("KH: could not save graph to file: "
					+ project.getProjectFile().getAbsolutePath());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean loadContent(File file) {
		return false;
	}

	private void removeNode(mxGraph graph, mxCell cell) {
		graph.getModel().beginUpdate();
		try {
			graph.getModel().remove(cell);
		} finally {
			graph.getModel().endUpdate();
		}
		if (cell.isVertex()) {
			IGraphNode node = (IGraphNode) cell.getValue();
			Iterator<IGraphNode> iter = node.getChildrenNodes().iterator();
			while(iter.hasNext()){
				iter.next();
				iter.remove();
			}
			node.setParentNode(null);
			iter = node.getFollowingNodes().iterator();
			while(iter.hasNext()){
				iter.next();
				iter.remove();
			}
			iter = node.getPreviousNodes().iterator();
			while(iter.hasNext()){
				iter.next();
				iter.remove();
			}
			project.removeProjectNode(node, false);
		} else {// edge
			mxICell source = cell.getSource();
			mxICell target = cell.getTarget();
			IGraphNode sourceNode = (IGraphNode) source.getValue();
			IGraphNode targetNode = (IGraphNode) target.getValue();
			sourceNode.removeFollowingNode(targetNode);
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
		undoHandler = new mxIEventListener() {

			@Override
			public void invoke(Object sender, mxEventObject evt) {
				undoManager.undoableEditHappened((mxUndoableEdit) evt
						.getProperty("edit"));
				Object[] cells = graphComponent.getGraph().getSelectionCells();
				for (Object cell : cells) {
					if(((mxCell) cell).isVertex()){
						int x = (int) graphComponent.getGraph().getView()
								.getState(cell).getX();
						int y = (int) graphComponent.getGraph().getView()
								.getState(cell).getY();
						IGraphNode wfNode = (IGraphNode) ((mxCell) cell).getValue();
						wfNode.setX(x);
						wfNode.setY(y);
					}
				}
			}
		};
		changeHandler = new mxIEventListener() {

			@Override
			public void invoke(Object sender, mxEventObject evt) {
				setDirty(true);
				if (getTabPanel() != null) {
					getTabPanel().getLabel().setText("*" + name);
				}
			}
		};
		connectHandler = new mxIEventListener() {

			@Override
			public void invoke(Object sender, mxEventObject evt) {
				mxCell cell = (mxCell) evt.getProperty("cell");
				if (cell.isEdge()) {
					mxICell source = cell.getSource();
					mxICell terminal = cell.getTarget();
					IGraphNode wfSource = (IGraphNode) source.getValue();
					wfSource.addFollowingNode((IGraphNode) terminal.getValue());
				}
			}
		};

		graphComponent.getGraph().getModel()
				.addListener(mxEvent.CHANGE, changeHandler);
		graphComponent.getGraph().getModel()
				.addListener(mxEvent.UNDO, undoHandler);
		graphComponent.getGraph().getView()
				.addListener(mxEvent.UNDO, undoHandler);
		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT,
				connectHandler);

		mxIEventListener undoHandler = new mxIEventListener() {

			@Override
			public void invoke(Object sender, mxEventObject evt) {
				mxUndoableEdit edit = (mxUndoableEdit) evt.getProperty("edit");
				graphComponent.getGraph().setSelectionCells(
						graphComponent.getGraph().getSelectionCellsForChanges(
								edit.getChanges()));
				updateProjectGraphAccordingToChange(edit);
			}
		};
		undoManager.addListener(mxEvent.UNDO, undoHandler);
		undoManager.addListener(mxEvent.REDO, undoHandler);

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
				rubberband.mouseClicked(e);
				
				if (e.getButton() == MouseEvent.BUTTON3) {
					mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(),
							e.getY());
					if (cell != null && cell.isVertex()) {
						nodePopup.show(graphComponent, e.getX(), e.getY());
					} else {
						workspacePopup.show(graphComponent, e.getX(), e.getY());
					}
				} else if (e.getButton() == MouseEvent.BUTTON2) {// XXX test
																	// code
					IGraphNode pNode = new GenericGraphNode(NodeIdGenerator
							.generateId(), NodeNameGenerator.generateName());
					pNode.setX(e.getX());
					pNode.setY(e.getY());
					project.addProjectNode(pNode);
					graphComponent.getGraph().getModel().beginUpdate();
					try {
						addNode(graphComponent.getGraph(), graphComponent
								.getGraph().getDefaultParent(), pNode
								.getNodeId(), pNode, e.getX(), e.getY(), 80, 30);
					} finally {
						graphComponent.getGraph().getModel().endUpdate();
					}
				}
			}
			
			@Override
			public void mouseMoved(MouseEvent e){
				rubberband.mouseMoved(e);
			}
			

			@Override
			public void mouseEntered(MouseEvent e) {
				rubberband.mouseEntered(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				rubberband.mouseExited(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
				rubberband.mousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				rubberband.mouseReleased(e);
			}
		});

		KeyListener keyTracker = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
					Object[] cells = graphComponent.getGraph().getSelectionCells();
					for(Object cell : cells){
						removeNode(graphComponent.getGraph(), (mxCell) cell);
					}
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
	
	@Override
	public void refresh(){
		graphComponent.getGraph().getModel().beginUpdate();
		try{
			graphComponent.getGraph().refresh();
		} finally{
			graphComponent.getGraph().getModel().endUpdate();
		}
	}

	private void setGraphLayout(WorkflowGraphLayout layout, boolean animate) {
		final mxIGraphLayout newLayout = createLayout(layout, animate);

		if (newLayout != null) {
			final mxGraph graph = graphComponent.getGraph();
			Object cell = graph.getSelectionCell();

			if (cell == null || graph.getModel().getChildCount(cell) == 0) {
				cell = graph.getDefaultParent();
			}

			graph.getModel().beginUpdate();
			try {
				newLayout.execute(cell);
			} finally {
				mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);
				morph.addListener(mxEvent.DONE, new mxIEventListener() {

					@Override
					public void invoke(Object sender, mxEventObject evt) {
						graph.getModel().endUpdate();
					}
				});
				morph.startAnimation();
			}
		}
	}

	private mxIGraphLayout createLayout(WorkflowGraphLayout layout,
			boolean animate) {
		mxIGraphLayout newLayout = null;

		if (layout != null) {
			mxGraph graph = graphComponent.getGraph();

			if (layout.getIdent().equalsIgnoreCase("verticalHierarchical")) {
				newLayout = new mxHierarchicalLayout(graph);
			} else if (layout.getIdent().equalsIgnoreCase(
					"horizontalHierarchical")) {
				newLayout = new mxHierarchicalLayout(graph, JLabel.WEST);
			} else if (layout.getIdent().equalsIgnoreCase("verticalTree")) {
				newLayout = new mxCompactTreeLayout(graph, false);
			} else if (layout.getIdent().equalsIgnoreCase("horizontalTree")) {
				newLayout = new mxCompactTreeLayout(graph, true);
			} else if (layout.getIdent().equalsIgnoreCase("parallelEdges")) {
				newLayout = new mxParallelEdgeLayout(graph);
			} else if (layout.getIdent().equalsIgnoreCase("placeEdgeLabels")) {
				newLayout = new mxEdgeLabelLayout(graph);
			} else if (layout.getIdent().equalsIgnoreCase("organicLayout")) {
				newLayout = new mxOrganicLayout(graph);
			}
			if (layout.getIdent().equalsIgnoreCase("verticalPartition")) {
				newLayout = new mxPartitionLayout(graph, false) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (layout.getIdent()
					.equalsIgnoreCase("horizontalPartition")) {
				newLayout = new mxPartitionLayout(graph, true) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (layout.getIdent().equalsIgnoreCase("verticalStack")) {
				newLayout = new mxStackLayout(graph, false) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (layout.getIdent().equalsIgnoreCase("horizontalStack")) {
				newLayout = new mxStackLayout(graph, true) {
					/**
					 * Overrides the empty implementation to return the size of
					 * the graph control.
					 */
					public mxRectangle getContainerSize() {
						return graphComponent.getLayoutAreaSize();
					}
				};
			} else if (layout.getIdent().equalsIgnoreCase("circleLayout")) {
				newLayout = new mxCircleLayout(graph);
			}
		}

		return newLayout;
	}
}