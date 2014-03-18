/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
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
package pl.gda.pg.eti.kernelhive.engine;

import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.JobProgress;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.impl.EngineGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IClientBeanRemote;

/**
 * Session Bean implementation class ClientBean
 */
@WebService
@Stateless
public class ClientBean implements IClientBeanRemote {

	/**
	 * Default constructor.
	 */
	public ClientBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	@WebMethod
	public Integer submitWorkflow(String serializedGraphConf) {
		EngineGraphConfiguration egc = new EngineGraphConfiguration();
		try {
			List<EngineGraphNodeDecorator> nodes = egc.loadGraphForEngine(new StringReader(serializedGraphConf));
			// FIXME:
			return HiveEngine.getInstance().runWorkflow(nodes, egc.getProjectName(), egc.getInputDataURL());
		} catch (ConfigurationException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Error deserializing graph configuration", e);
			return null;
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Error submiting workflow", e);
			return null;
		}
	}

	@Override
	@WebMethod
	public List<WorkflowInfo> browseWorkflows() {
		return HiveEngine.getInstance().browseWorkflows();
	}

	@Override
	@WebMethod
	public String getWorkflowResults(Integer workflowID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod
	public void terminateWorkflow(Integer workflowID) {
		Logger.getLogger(getClass().getName()).info("bean terminate");
		HiveEngine.getInstance().terminateWorkflow(workflowID);
	}

	@Override
	@WebMethod
	public List<ClusterInfo> browseInfrastructure() {
		return HiveEngine.getInstance().getInfrastructureInfo();
	}

	@Override
	@WebMethod
	public List<JobProgress> getWorkflowProgress(Integer workflowID) {
		return HiveEngine.getInstance().getWorkflowProgress(workflowID);
	}
}
