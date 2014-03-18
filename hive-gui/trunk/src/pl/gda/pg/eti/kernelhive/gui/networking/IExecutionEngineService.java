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
package pl.gda.pg.eti.kernelhive.gui.networking;

public interface IExecutionEngineService {
	/**
	 * invokes browse infrastructure action 
	 * @param listener, listener implementing callback method for this action
	 */
	void browseInfrastructure(ExecutionEngineServiceListener listener);
	/**
	 * invokes get workflow results action
	 * @param workflowId, id of the specific workflow we want to retrieve results
	 * @param listener, listener implementing callback method for this action
	 */
	void getWorkflowResults(Integer workflowId, ExecutionEngineServiceListener listener);
	/**
	 * invokes terminate workflow action
	 * @param workflowId, id of the workflow to terminate
	 * @param listener, listener implementing callback method for this action
	 */
	void terminateWorkflow(Integer workflowId, ExecutionEngineServiceListener listener);
	/**
	 * invokes browse workflows action
	 * @param listener, listener implementing callback method for this action
	 */
	void browseWorkflows(ExecutionEngineServiceListener listener);
	/**
	 * invokes submit workflow for execution action
	 * @param workflowStream, workflow description serialized to xml format
	 * @param listener, listener implementing callback method for this action
	 */
	void submitWorkflow(String workflowStream, ExecutionEngineServiceListener listener);
	
}
