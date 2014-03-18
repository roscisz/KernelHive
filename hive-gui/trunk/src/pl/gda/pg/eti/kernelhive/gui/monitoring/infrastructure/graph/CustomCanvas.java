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

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.view.mxCellState;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.JLabel;

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
