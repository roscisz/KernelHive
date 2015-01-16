/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
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

public enum WorkflowGraphLayoutType {
	VERTICAL_HIERARCHICAL("verticalHierarchical"), HORIZONTAL_HIERARCHICAL(
			"horizontalHierarchical"), VERTICAL_TREE("verticalTree"), HORIZONTAL_TREE(
			"horizontalTree"), PARALLEL_EDGES("parallelEdges"), PLACE_EDGE_LABELS(
			"placeEdgeLabels"), ORGANIC_LAYOUT("organicLayout"), VERTICAL_PARTITION(
			"verticalPartition"), HORIZONTAL_PARTITION("horizontalPartition"), VERTICAL_STACK(
			"verticalStack"), HORIZONTAL_STACK("horizontalStack"), CIRCLE_LAYOUT(
			"circleLayout");

	private final String ident;

	private WorkflowGraphLayoutType(final String ident) {
		this.ident = ident;
	}

	public String getIdent() {
		return ident;
	}
}
