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
package pl.gda.pg.eti.kernelhive.gui.component.workflow.editor;

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
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.dialog.MessageDialog;
import pl.gda.pg.eti.kernelhive.gui.dialog.NodePropertiesDialog;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.project.IProject;

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
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxStyleUtils;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;

public class WorkflowEditor extends JTabContent {

	private static final long serialVersionUID = 1609157384680024544L;
	private static final Logger LOG = Logger.getLogger(WorkflowEditor.class
			.getName());

	private static mxIGraphLayout createLayout(
			final WorkflowGraphLayoutType layout,
			final mxGraphComponent graphComponent, final boolean animate) {
		mxIGraphLayout newLayout = null;

		if (layout != null) {
			final mxGraph graph = graphComponent.getGraph();

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
					@Override
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
					@Override
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
					@Override
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
					@Override
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

	private static mxGraph loadProject(final IProject project) {
		final List<GUIGraphNodeDecorator> projectNodes = project
				.getProjectNodes();
		final Map<IGraphNode, mxCell> mapping = new Hashtable<IGraphNode, mxCell>(
				projectNodes.size());

		final mxGraph graph = new mxGraph();
		// initial load of all nodes
		for (final GUIGraphNodeDecorator node : projectNodes) {
			final mxCell parent = (mxCell) graph.getDefaultParent();
			graph.getModel().beginUpdate();
			try {
				mxCell v1;
				if (node.getGraphNode().canAddChildNode(null)) {
					v1 = (mxCell) graph.insertVertex(parent, node
							.getGraphNode().getNodeId(), node, node.getX(),
							node.getY(), 80, 100, mxStyleUtils.setStyle("",
							mxConstants.STYLE_SHAPE,
							mxConstants.SHAPE_SWIMLANE));
				} else {
					v1 = (mxCell) graph.insertVertex(parent, node
							.getGraphNode().getNodeId(), node, node.getX(),
							node.getY(), 80, 30, "");
				}
				mapping.put(node.getGraphNode(), v1);
			} finally {
				graph.getModel().endUpdate();
			}
		}
		// linking nodes to their parents
		for (final GUIGraphNodeDecorator node : projectNodes) {
			graph.getModel().beginUpdate();
			try {
				final mxCell v = mapping.get(node.getGraphNode());
				if (node.getGraphNode().getParentNode() != null) {
					graph.addCell(v,
							mapping.get(node.getGraphNode().getParentNode()));
				}
			} finally {
				graph.getModel().endUpdate();
			}
		}
		for (final GUIGraphNodeDecorator node : projectNodes) {
			graph.getModel().beginUpdate();
			try {
				final mxCell v = mapping.get(node.getGraphNode());
				graph.extendParent(v);
			} finally {
				graph.getModel().endUpdate();
			}
		}
		// linking following nodes
		for (final GUIGraphNodeDecorator node : projectNodes) {
			graph.getModel().beginUpdate();
			try {
				final mxCell v = mapping.get(node.getGraphNode());
				final mxCell parent = (mxCell) v.getParent();
				final List<IGraphNode> followingNodes = node.getGraphNode()
						.getFollowingNodes();
				for (final IGraphNode fNode : followingNodes) {
					graph.insertEdge(parent, "", null, v, mapping.get(fNode));
				}
			} finally {
				graph.getModel().endUpdate();
			}
		}
		return graph;
	}
	protected mxGraphComponent graphComponent;
	protected mxGraph graph;
	protected String name;
	protected IProject project;
	protected JPopupMenu nodePopup;
	protected JPopupMenu workspacePopup;
	protected mxUndoManager undoManager;
	protected mxIEventListener undoHandler;
	protected mxIEventListener changeHandler;
	protected mxIEventListener connectHandler;
	protected mxRubberband rubberband;
	protected WorkflowEditorDropTargetListener dropTargetListener;

	public WorkflowEditor(final MainFrame frame, final String name,
			final IProject project) {
		super(frame);
		this.name = name;
		this.project = project;
		setName(this.name);
		setLayout(new BorderLayout());
		setupNodePopupMenu();
		setupWorkspacePopup();

		undoManager = new mxUndoManager();
		graph = WorkflowEditor.loadProject(project);
		graphComponent = new mxGraphComponent(graph);
		graphComponent.setGridVisible(true);
		graphComponent.getGraph().setAllowLoops(false);
		graphComponent.getGraph().setAllowDanglingEdges(false);
		graphComponent.add(nodePopup);
		graphComponent.add(workspacePopup);
		add(graphComponent);

		rubberband = new mxRubberband(graphComponent);

		installListeners();
	}

	public void initGraphComponent() {
	}

	@Override
	public void copy() {
	}

	@Override
	public void cut() {
	}

	private void installGraphListeners() {
		undoHandler = new mxIEventListener() {
			@Override
			public void invoke(final Object sender, final mxEventObject evt) {
				undoManager.undoableEditHappened((mxUndoableEdit) evt
						.getProperty("edit"));
				final Object[] cells = graphComponent.getGraph()
						.getSelectionCells();
				for (final Object cell : cells) {
					if (((mxCell) cell).isVertex()) {
						if (((mxCell) cell).getValue() instanceof GUIGraphNodeDecorator) {
							final GUIGraphNodeDecorator wfNode = (GUIGraphNodeDecorator) ((mxCell) cell)
									.getValue();
							wfNode.setX((int) graphComponent.getGraph()
									.getView().getState(cell).getX());
							wfNode.setY((int) graphComponent.getGraph()
									.getView().getState(cell).getY());
						}
					}
				}
			}
		};
		changeHandler = new mxIEventListener() {
			@SuppressWarnings("unchecked")
			private void checkParentChanges(final mxEventObject evt) {
				final Object obj = evt.getProperty("changes");
				if (obj instanceof List) {
					final List<Object> list = (List<Object>) obj;
					for (final Object o : list) {
						if (o instanceof mxChildChange) {
							final mxChildChange childChange = (mxChildChange) o;
							final mxCell cell = (mxCell) childChange.getChild();
							final mxCell parent = (mxCell) childChange
									.getParent();
							final mxCell previousParent = (mxCell) childChange
									.getPrevious();
							if (cell.isVertex()
									&& cell.getValue() instanceof GUIGraphNodeDecorator) {
								if (parent != null && previousParent != null
										&& !parent.equals(previousParent)) {
									final GUIGraphNodeDecorator node = (GUIGraphNodeDecorator) cell
											.getValue();
									if (parent.getValue() instanceof GUIGraphNodeDecorator) {
										node.getGraphNode().setParentNode(
												((GUIGraphNodeDecorator) parent
												.getValue())
												.getGraphNode());
									} else if (parent == graphComponent
											.getGraph().getDefaultParent()) {
										node.getGraphNode().setParentNode(null);
									}
									refresh();
								}
							}
						}
					}
				}
			}

			@Override
			public void invoke(final Object sender, final mxEventObject evt) {
				setDirty(true);
				if (getTabPanel() != null) {
					getTabPanel().getLabel().setText("* " + name);
				}
				checkParentChanges(evt);
			}
		};
		graphComponent.getGraph().getModel()
				.addListener(mxEvent.CHANGE, changeHandler);
		graphComponent.getGraph().getModel()
				.addListener(mxEvent.UNDO, undoHandler);
		graphComponent.getGraph().getView()
				.addListener(mxEvent.UNDO, undoHandler);

		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				rubberband.mouseClicked(e);

				if (e.getButton() == MouseEvent.BUTTON3) {
					final mxCell cell = (mxCell) graphComponent.getCellAt(
							e.getX(), e.getY());
					if (cell != null && cell.isVertex()) {
						nodePopup.show(graphComponent, e.getX(), e.getY());
					} else {
						workspacePopup.show(graphComponent, e.getX(), e.getY());
					}
				}
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				rubberband.mouseEntered(e);
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				rubberband.mouseExited(e);
			}

			@Override
			public void mouseMoved(final MouseEvent e) {
				rubberband.mouseMoved(e);
			}

			@Override
			public void mousePressed(final MouseEvent e) {

				rubberband.mousePressed(e);
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				rubberband.mouseReleased(e);
			}
		});
	}

	private void installListeners() {
		installGraphListeners();

		connectHandler = new mxIEventListener() {
			@Override
			public void invoke(final Object sender, final mxEventObject evt) {
				final mxCell cell = (mxCell) evt.getProperty("cell");
				if (cell.isEdge()) {
					final mxICell source = cell.getSource();
					final mxICell terminal = cell.getTarget();
					final IGraphNodeDecorator wfSource = (IGraphNodeDecorator) source
							.getValue();
					final IGraphNodeDecorator wfTerminal = (IGraphNodeDecorator) terminal
							.getValue();

					if (wfSource.getGraphNode().canAddFollowingNode(
							wfTerminal.getGraphNode())
							&& wfTerminal.getGraphNode().canAddPreviousNode(
							wfSource.getGraphNode())) {
						wfSource.getGraphNode().addFollowingNode(
								wfTerminal.getGraphNode());
					} else {
						LOG.info("KH: Selected nodes ("
								+ wfSource.getGraphNode().getNodeId() + ", "
								+ wfTerminal.getGraphNode().getNodeId()
								+ ")cannot be connected.");
						graph.getModel().beginUpdate();
						try {
							graph.getModel().remove(cell);
						} finally {
							graph.getModel().endUpdate();
						}
						MessageDialog
								.showMessageDialog(WorkflowEditor.this, "",
								"Selected nodes cannot be connected. Invalid graph state!");
					}

				}
			}
		};

		graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT,
				connectHandler);

		final mxIEventListener undoHandler = new mxIEventListener() {
			@Override
			public void invoke(final Object sender, final mxEventObject evt) {
				final mxUndoableEdit edit = (mxUndoableEdit) evt
						.getProperty("edit");
				graphComponent.getGraph().setSelectionCells(
						graphComponent.getGraph().getSelectionCellsForChanges(
						edit.getChanges()));
				updateProjectGraphAccordingToChange(edit);
			}
		};
		undoManager.addListener(mxEvent.UNDO, undoHandler);
		undoManager.addListener(mxEvent.REDO, undoHandler);

		final MouseWheelListener wheelTracker = new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(final MouseWheelEvent e) {
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

		final KeyListener keyTracker = new KeyListener() {
			@Override
			public void keyPressed(final KeyEvent arg0) {
			}

			@Override
			public void keyReleased(final KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
					final Object[] cells = graphComponent.getGraph()
							.getSelectionCells();
					for (final Object cell : cells) {
						removeNode(graphComponent.getGraph(), (mxCell) cell);
					}
				}
			}

			@Override
			public void keyTyped(final KeyEvent ke) {
			}
		};
		graphComponent.addKeyListener(keyTracker);

		dropTargetListener = new WorkflowEditorDropTargetListener(this);
	}

	@Override
	public boolean loadContent() {
		return false;
	}

	@Override
	public boolean loadContent(final File file) {
		return false;
	}

	@Override
	public void paste() {
	}

	@Override
	public void redoAction() {
		undoManager.redo();
	}

	@Override
	public void refresh() {
		graph = WorkflowEditor.loadProject(project);
		graph.setAllowDanglingEdges(true);
		graphComponent.setGraph(graph);
		installGraphListeners();
		graphComponent.getGraph().getModel().beginUpdate();
		try {
			graphComponent.getGraph().refresh();
		} finally {
			graphComponent.getGraph().getModel().endUpdate();
		}
	}

	private void removeNode(final mxGraph graph, final mxCell cell) {
		graph.getModel().beginUpdate();
		try {
			graph.getModel().remove(cell);
		} finally {
			graph.getModel().endUpdate();
		}
		if (cell.isVertex()) {
			final GUIGraphNodeDecorator node = (GUIGraphNodeDecorator) cell
					.getValue();
			Iterator<IGraphNode> iter = node.getGraphNode().getChildrenNodes()
					.iterator();
			while (iter.hasNext()) {
				iter.next();
				iter.remove();
			}
			node.getGraphNode().setParentNode(null);
			iter = node.getGraphNode().getFollowingNodes().iterator();
			while (iter.hasNext()) {
				iter.next();
				iter.remove();
			}
			iter = node.getGraphNode().getPreviousNodes().iterator();
			while (iter.hasNext()) {
				iter.next();
				iter.remove();
			}
			project.removeProjectNode(node, false);
		} else {// edge
			final mxICell source = cell.getSource();
			final mxICell target = cell.getTarget();
			final GUIGraphNodeDecorator sourceNode = (GUIGraphNodeDecorator) source
					.getValue();
			final GUIGraphNodeDecorator targetNode = (GUIGraphNodeDecorator) target
					.getValue();
			sourceNode.getGraphNode().removeFollowingNode(
					targetNode.getGraphNode());
		}
	}

	@Override
	public boolean saveContent() {
		try {
			project.save();
			setDirty(false);
			if (getTabPanel() != null) {
				getTabPanel().getLabel().setText(name);
			}
			return true;
		} catch (final ConfigurationException e) {
			LOG.warning("KH: could not save graph to file: "
					+ project.getProjectFile().getAbsolutePath());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean saveContent(final File file) {
		return saveContent();
	}

	@Override
	public void selectAll() {
		graphComponent.getGraph().selectAll();
	}

	private void setGraphLayout(final WorkflowGraphLayoutType layout,
			final boolean animate) {
		final mxIGraphLayout newLayout = WorkflowEditor.createLayout(layout,
				graphComponent, animate);

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
				final mxMorphing morph = new mxMorphing(graphComponent, 20,
						1.2, 20);
				morph.addListener(mxEvent.DONE, new mxIEventListener() {
					@Override
					public void invoke(final Object sender,
							final mxEventObject evt) {
						graph.getModel().endUpdate();
					}
				});
				morph.startAnimation();
			}
		}
	}

	// TODO i18n
	private void setupNodePopupMenu() {
		nodePopup = new JPopupMenu();
		JMenuItem mi = new JMenuItem("Properties");
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ae) {
				final mxCell cell = (mxCell) graphComponent.getGraph()
						.getSelectionCell();
				if (cell != null) {
					final GUIGraphNodeDecorator node = (GUIGraphNodeDecorator) cell
							.getValue();
					final NodePropertiesDialog npd = new NodePropertiesDialog(
							getFrame(), node);
					npd.setVisible(true);
					npd.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(final WindowEvent e) {
							refresh();
						}

						@Override
						public void windowClosing(final WindowEvent e) {
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
			public void actionPerformed(final ActionEvent arg0) {
				final mxCell cell = (mxCell) graphComponent.getGraph()
						.getSelectionCell();
				removeNode(graphComponent.getGraph(), cell);
			}
		});
		nodePopup.add(mi);
	}

	// TODO i18n
	private void setupWorkspacePopup() {
		workspacePopup = new JPopupMenu();
		final JMenu layoutMenu = new JMenu("Layout");
		final JMenuItem verticalHierarchical = new JMenuItem(
				"Vertical Hierarchical");
		verticalHierarchical.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				setGraphLayout(WorkflowGraphLayoutType.VERTICAL_HIERARCHICAL,
						true);
			}
		});
		layoutMenu.add(verticalHierarchical);
		final JMenuItem horizontalHierarchical = new JMenuItem(
				"Horizontal Hierarchical");
		horizontalHierarchical.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setGraphLayout(WorkflowGraphLayoutType.HORIZONTAL_HIERARCHICAL,
						true);
			}
		});
		layoutMenu.add(horizontalHierarchical);
		final JMenuItem verticalTree = new JMenuItem("Vertical Tree");
		verticalTree.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setGraphLayout(WorkflowGraphLayoutType.VERTICAL_TREE, true);
			}
		});
		layoutMenu.add(verticalTree);
		final JMenuItem horizontalTree = new JMenuItem("Horizontal Tree");
		horizontalTree.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setGraphLayout(WorkflowGraphLayoutType.HORIZONTAL_TREE, true);
			}
		});
		layoutMenu.add(horizontalTree);
		final JMenuItem parallelEdges = new JMenuItem("Parallel Edges");
		parallelEdges.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setGraphLayout(WorkflowGraphLayoutType.PARALLEL_EDGES, true);
			}
		});
		layoutMenu.add(parallelEdges);
		final JMenuItem placeEdgeLabels = new JMenuItem("Place Edge Labels");
		placeEdgeLabels.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setGraphLayout(WorkflowGraphLayoutType.PLACE_EDGE_LABELS, true);
			}
		});
		layoutMenu.add(placeEdgeLabels);
		final JMenuItem organicLayout = new JMenuItem("Organic Layout");
		organicLayout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setGraphLayout(WorkflowGraphLayoutType.ORGANIC_LAYOUT, true);
			}
		});
		final JMenuItem verticalPartition = new JMenuItem("Vertical Partition");
		verticalPartition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setGraphLayout(WorkflowGraphLayoutType.VERTICAL_PARTITION, true);
			}
		});
		layoutMenu.add(verticalPartition);
		final JMenuItem horizontalPartition = new JMenuItem(
				"Horizontal Partition");
		horizontalPartition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setGraphLayout(WorkflowGraphLayoutType.HORIZONTAL_PARTITION,
						true);
			}
		});
		layoutMenu.add(horizontalPartition);
		final JMenuItem verticalStack = new JMenuItem("Vertical Stack");
		verticalStack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setGraphLayout(WorkflowGraphLayoutType.VERTICAL_STACK, true);
			}
		});
		layoutMenu.add(verticalStack);
		final JMenuItem horizontalStack = new JMenuItem("Horizontal Stack");
		horizontalStack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setGraphLayout(WorkflowGraphLayoutType.HORIZONTAL_STACK, true);
			}
		});
		layoutMenu.add(horizontalStack);
		final JMenuItem circleLayout = new JMenuItem("Circle Layout");
		circleLayout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setGraphLayout(WorkflowGraphLayoutType.CIRCLE_LAYOUT, true);
			}
		});
		layoutMenu.add(circleLayout);
		workspacePopup.add(layoutMenu);
	}

	@Override
	public void undoAction() {
		undoManager.undo();
	}

	protected IProject getProject() {
		return project;
	}

	private void updateProjectGraphAccordingToChange(final mxUndoableEdit edit) {
		final List<mxUndoableChange> changes = edit.getChanges();
		for (final mxUndoableChange change : changes) {
			if (change instanceof mxChildChange) {
				final mxChildChange childChange = (mxChildChange) change;
				final mxCell cell = (mxCell) childChange.getChild();
				if (cell.isVertex()) {// vertex
					final GUIGraphNodeDecorator node = (GUIGraphNodeDecorator) cell
							.getValue();
					if (childChange.getPrevious() == null) {// restore
						project.addProjectNode(node);
						for (int i = 0; i < cell.getEdgeCount(); i++) {
							final mxICell source = cell.getEdgeAt(i)
									.getTerminal(true);
							final mxICell end = cell.getEdgeAt(i).getTerminal(
									false);
							if (cell.equals(source)) {// add following node
								node.getGraphNode()
										.addFollowingNode(
										((GUIGraphNodeDecorator) end
										.getValue())
										.getGraphNode());
							} else {
								node.getGraphNode().addPreviousNode(
										((GUIGraphNodeDecorator) source
										.getValue()).getGraphNode());
							}
						}

						// restore children nodes TODO FIXME XXX reccurent
						// in-depth resolving
					} else if (childChange.getPrevious() != null) {// destroy
						Iterator<IGraphNode> iter = node.getGraphNode()
								.getChildrenNodes().iterator();
						while (iter.hasNext()) {
							iter.next();
							iter.remove();
						}
						node.getGraphNode().setParentNode(null);
						iter = node.getGraphNode().getFollowingNodes()
								.iterator();
						while (iter.hasNext()) {
							iter.next();
							iter.remove();
						}
						iter = node.getGraphNode().getPreviousNodes()
								.iterator();
						while (iter.hasNext()) {
							iter.next();
							iter.remove();
						}

						project.removeProjectNode(node, false);
					}
				} else {// edge
					final mxICell source = cell.getSource();
					final mxICell target = cell.getTarget();
					final GUIGraphNodeDecorator sourceNode = (GUIGraphNodeDecorator) source
							.getValue();
					final GUIGraphNodeDecorator targetNode = (GUIGraphNodeDecorator) target
							.getValue();

					if (childChange.getPrevious() == null) {// restore
						sourceNode.getGraphNode().addFollowingNode(
								targetNode.getGraphNode());
					} else if (childChange.getPrevious() != null) {// destroy
						sourceNode.getGraphNode().removeFollowingNode(
								targetNode.getGraphNode());
					}
				}
			}
		}
	}

	@Override
	public void clearSelection() {
		graphComponent.getGraph().clearSelection();
	}
}
