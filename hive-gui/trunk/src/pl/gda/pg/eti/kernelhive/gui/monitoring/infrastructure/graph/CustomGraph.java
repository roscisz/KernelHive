/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.monitoring.infrastructure.graph;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

/**
 *
 * @author szymon
 */
public class CustomGraph extends mxGraph {

	@Override
	public void drawState(mxICanvas canvas, mxCellState state,
			boolean drawLabel) {
		String label = (drawLabel) ? state.getLabel() : "";

		// Indirection for wrapped swing canvas inside image canvas (used for creating
		// the preview image when cells are dragged)
		if (getModel().isVertex(state.getCell())
				&& canvas instanceof mxImageCanvas
				&& ((mxImageCanvas) canvas).getGraphicsCanvas() instanceof CustomCanvas) {
			((CustomCanvas) ((mxImageCanvas) canvas).getGraphicsCanvas())
					.drawVertex(state, label);
		} // Redirection of drawing vertices in SwingCanvas
		else if (getModel().isVertex(state.getCell())
				&& canvas instanceof CustomCanvas) {
			((CustomCanvas) canvas).drawVertex(state, label);
		} else {
			super.drawState(canvas, state, drawLabel);
		}
	}
}
