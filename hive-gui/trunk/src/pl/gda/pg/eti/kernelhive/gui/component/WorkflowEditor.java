package pl.gda.pg.eti.kernelhive.gui.component;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public class WorkflowEditor extends JTabContent implements mxIEventListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1609157384680024544L;

	private mxGraphComponent graphComponent;
	private String name;
	
	
	public WorkflowEditor(MainFrame frame, String name) {
		super(frame);
		this.name = name;
		setName(this.name);
		setLayout(new BorderLayout());
		
		mxGraph graph = new mxGraph();
		
		//TODO remove
		Object parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();
		try{
			mxCell v1 = (mxCell)graph.insertVertex(parent, "Node1", null, 20, 20, 80, 30);
			mxCell v2 = (mxCell)graph.insertVertex(parent, "Node2", null, 240, 150, 80, 30);
			graph.insertEdge(parent, "Edge", "", v1, v2);
		}finally{
			graph.getModel().endUpdate();
		}
		//
		
		graphComponent = new mxGraphComponent(graph);
		graphComponent.getGraph().getModel().addListener(mxEvent.CHANGE, this);
		graphComponent.getGraphControl().addMouseListener(this);
		add(graphComponent);
	}

	@Override
	public boolean saveContent(File file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void invoke(Object sender, mxEventObject event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO test code
		if(e.getButton()==MouseEvent.BUTTON3){
			graphComponent.getGraph().getModel().beginUpdate();
			try{
				mxCell v = (mxCell) graphComponent.getGraph().
						insertVertex(graphComponent.getGraph().getDefaultParent(),
								"Node", 
								null, 
								e.getX(), 
								e.getY(), 
								80, 
								30);
			} finally{
				graphComponent.getGraph().getModel().endUpdate();
			}
		} else if(e.getButton()==MouseEvent.BUTTON2){
			graphComponent.getGraph().getModel().beginUpdate();
			try{
				mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
				graphComponent.getGraph().getModel().remove(cell);
			} finally{
				graphComponent.getGraph().getModel().endUpdate();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}