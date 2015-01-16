/**
 * Copyright (c) 2014 Gdansk University of Technology
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
package pl.gda.pg.eti.kernelhive.gui.monitoring.infrastructure.graph;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

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
