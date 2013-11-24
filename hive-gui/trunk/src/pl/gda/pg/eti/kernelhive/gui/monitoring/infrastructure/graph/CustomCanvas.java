/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.monitoring.infrastructure.graph;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.view.mxCellState;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.JLabel;

/**
 *
 * @author szymon
 */
public class CustomCanvas extends mxInteractiveCanvas {

	protected CellRendererPane rendererPane = new CellRendererPane();
	protected mxGraphComponent graphComponent;

	public CustomCanvas(mxGraphComponent graphComponent) {
		this.graphComponent = graphComponent;
	}

	public void drawVertex(mxCellState state, String label) {
		Object value = ((mxCell) state.getCell()).getValue();
		if (value instanceof ClusterVertex) {
			ClusterVertex vertex = (ClusterVertex) value;
			rendererPane.paintComponent(g, vertex, graphComponent,
					(int) state.getX() + translate.x, (int) state.getY()
					+ translate.y, 200,
					50, true);
		} else {
			JLabel vertex = new JLabel(label);
			rendererPane.paintComponent(g, vertex, graphComponent,
					(int) state.getX() + translate.x, (int) state.getY()
					+ translate.y, (int) state.getWidth(),
					(int) state.getHeight(), true);
		}

	}
}
