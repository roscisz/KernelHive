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
package pl.gda.pg.eti.kernelhive.gui.monitoring.infrastructure;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingConstants;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.ClusterDefinition;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.DeviceDefinition;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.UnitDefinition;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;
import pl.gda.pg.eti.kernelhive.gui.helpers.WorkspaceHelper;
import pl.gda.pg.eti.kernelhive.gui.monitoring.resourcemonitor.ResourceMonitor;

public class InfrastructureGraphPanel extends javax.swing.JPanel {

	private final MainFrame frame;
	private mxGraph graph;
	private mxGraphComponent graphComponent;
	private Map<Object, UnitDefinition> cellToUnitMap = new HashMap<>();
	private Map<Integer, ClusterDefinition> clusters = new HashMap<>();

	/**
	 * Creates new form InfrastructureGraphPanel
	 */
	public InfrastructureGraphPanel(final MainFrame frame) {
		this.frame = frame;
		setLayout(new BorderLayout());

		//graph = new CustomGraph();
		graph = new mxGraph();
		graph.setAutoSizeCells(true);
		//graph.setCellsResizable(true);

		graphComponent = new mxGraphComponent(graph)/* {
				 private static final long serialVersionUID = 4683716829748931448L;
				 @Override
				 public mxInteractiveCanvas createCanvas() {
				 return new CustomCanvas(this);
				 }
				 }*/;
		add(graphComponent, BorderLayout.CENTER);

		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Object cell = graphComponent.getCellAt(e.getX(), e.getY());
				if (cell != null) {
					if (cellToUnitMap.containsKey(cell)) {
						UnitDefinition unit = cellToUnitMap.get(cell);
						ClusterDefinition cluster = clusters.get(unit.getClusterId());
						Logger.getLogger(getClass().getName()).severe("unit " + unit.getHostname());
						String title = String.format("Resource monitor (%s/%s)",
								cluster.getHostname(), unit.getHostname());
						ResourceMonitor tab = new ResourceMonitor(frame, title, unit);
						new WorkspaceHelper().addTab(frame.getWorkspacePane(), tab, true);
					}
				}
			}
		});
	}

	private String getClusterId(long clusterId) {
		return String.format("cluster-%d", clusterId);
	}

	private String getUnitId(UnitDefinition unit) {
		return String.format("cluster-%d-%d", unit.getClusterId(), unit.getUnitId());
	}

	private String getUnitId(long clusterId, int unitId) {
		return String.format("cluster-%d-%d", clusterId, unitId);
	}

	public void setInfrastructure(List<ClusterDefinition> clusters,
			List<UnitDefinition> units,
			List<DeviceDefinition> devices) {
		graph.getModel().beginUpdate();
		graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
		try {
			Object parent = graph.getDefaultParent();
			Object engineVertex = graph.insertVertex(parent, null, "Engine", 100, 100, 120, 30);
			Map<String, Object> clusterVertices = new HashMap<>();
			this.clusters.clear();
			for (ClusterDefinition cluster : clusters) {
				this.clusters.put(new BigDecimal(cluster.getId()).intValue(), cluster);
				Object clusterVertex = graph.insertVertex(parent, getClusterId(cluster.getId()),
						String.format("Cluster (%s)", cluster.getHostname()),
						//new ClusterVertex(cluster),
						100, 100, 150, 100);
				graph.insertEdge(parent, null, "", engineVertex, clusterVertex);
				clusterVertices.put(getClusterId(cluster.getId()), clusterVertex);
				graph.updateCellSize(clusterVertex);
			}
			Map<String, Object> unitVertices = new HashMap<>();
			cellToUnitMap.clear();
			for (UnitDefinition unit : units) {
				Object clusterVertex = clusterVertices.get(getClusterId(unit.getClusterId()));
				if (clusterVertex != null) {
					Object unitVertex = graph.insertVertex(parent, getUnitId(unit),
							String.format("Unit (%s)", unit.getHostname()),
							100, 100, 80, 30);
					graph.insertEdge(parent, null, "", clusterVertex, unitVertex);
					unitVertices.put(getUnitId(unit), unitVertex);
					cellToUnitMap.put(unitVertex, unit);
					graph.updateCellSize(unitVertex);
				}
			}
			for (DeviceDefinition device : devices) {
				Object unitVertex = unitVertices.get(getUnitId(device.getClusterId(), device.getUnitId()));
				if (unitVertex != null) {
					Object deviceVertex = graph.insertVertex(parent, null,
							String.format("Device (%s)", device.getName()),
							100, 100, 80, 30);
					graph.insertEdge(parent, null, "", unitVertex, deviceVertex);
					graph.updateCellSize(deviceVertex);
				}
			}
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Error while adding grap nodes", ex);
		} finally {
			graph.getModel().endUpdate();
		}
		releayout();
	}

	public void releayout() {
		mxHierarchicalLayout layout = new mxHierarchicalLayout(graph, SwingConstants.NORTH);
		layout.execute(graph.getDefaultParent());

		mxRectangle graphSize = graph.getGraphBounds();
		Dimension viewPortSize = graphComponent.getViewport().getSize();

		double ratiox = viewPortSize.getWidth() / graphSize.getWidth();
		double ratioy = viewPortSize.getHeight() / graphSize.getHeight();
		double ratio = ratiox < ratioy ? ratiox : ratioy;

		graphComponent.zoom(ratio);
	}
}
