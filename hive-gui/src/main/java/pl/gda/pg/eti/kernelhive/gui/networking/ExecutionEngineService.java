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
package pl.gda.pg.eti.kernelhive.gui.networking;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import javax.xml.ws.WebServiceException;

import pl.gda.pg.eti.kernelhive.common.clientService.ClientBean;
import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.gui.helpers.WebServiceHelper;

public class ExecutionEngineService implements IExecutionEngineService {

	private static ExecutionEngineService _service;
	private ExecutorService executorService;
	private ClientBean clientService;

	public static ExecutionEngineService getInstance() throws ExecutionEngineServiceException {
		if (_service == null) {
			_service = new ExecutionEngineService();
		}
		return _service;
	}

	private ExecutionEngineService() throws ExecutionEngineServiceException {
		executorService = Executors.newFixedThreadPool(5);
		try {
			clientService = new WebServiceHelper().getClientService();
		} catch (WebServiceException e) {
			throw new ExecutionEngineServiceException(e);
		}
	}

	@Override
	public void browseInfrastructure(final ExecutionEngineServiceListener listener) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				List<ClusterInfo> clusterInfo = clientService.browseInfrastructure();
				listener.infrastractureBrowseCompleted(clusterInfo);
			}
		};
		executorService.execute(r);
	}

	@Override
	public void getWorkflowResults(final Integer workflowId,
			final ExecutionEngineServiceListener listener) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				String results = clientService.getWorkflowResults(workflowId);
				listener.getWorkflowResultsCompleted(results);
			}
		};
		executorService.execute(r);
	}

	@Override
	public void terminateWorkflow(final Integer workflowId,
			final ExecutionEngineServiceListener listener) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				clientService.terminateWorkflow(workflowId);
				if (listener != null) {
					listener.terminateWorkflowCompleted();
				}
			}
		};
		executorService.execute(r);
	}

	@Override
	public void browseWorkflows(final ExecutionEngineServiceListener listener) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				List<WorkflowInfo> workflowInfo = clientService.browseWorkflows();
				listener.workflowBrowseCompleted(workflowInfo);
			}
		};
		executorService.execute(r);
	}

	@Override
	public void submitWorkflow(final String workflowStream,
			final ExecutionEngineServiceListener listener) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				Integer workflowId = clientService.submitWorkflow(workflowStream);
				listener.submitWorkflowCompleted(workflowId);
			}
		};
		executorService.execute(r);
	}
}
