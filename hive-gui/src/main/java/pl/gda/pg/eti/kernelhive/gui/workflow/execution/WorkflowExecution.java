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
package pl.gda.pg.eti.kernelhive.gui.workflow.execution;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.gui.networking.IExecutionEngineService;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineService;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineServiceException;
import pl.gda.pg.eti.kernelhive.gui.networking.ExecutionEngineServiceListenerAdapter;

public class WorkflowExecution implements IWorkflowExecution {
	
	private byte[] graphStream = null;
	private String username = null;
	private char[] password = null;
	private IExecutionEngineService service = null;
	private ExecutionEngineServiceListenerAdapter serviceAdapter = null;
	private List<WorkflowExecutionListener> listeners;
	
	public WorkflowExecution(){
		try {
			service = ExecutionEngineService.getInstance();
		} catch (ExecutionEngineServiceException e) {
			e.printStackTrace();
		}
		initListeners();
	}
	
	private void initListeners(){
		listeners = new ArrayList<WorkflowExecutionListener>();
		serviceAdapter = new ExecutionEngineServiceListenerAdapter() {
			@Override
			public void submitWorkflowCompleted(Integer workflowId) {
				for(WorkflowExecutionListener l : listeners){
					l.workflowSubmissionCompleted(workflowId);
				}
			}
		};
	}
	
	@Override
	public void setSerializedGraphStream(byte[] stream) {
		graphStream = stream;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void setPassword(char[] password) {
		this.password = password;
	}

	@Override
	public void submitForExecution() {
//		System.out.println(new String(graphStream));
		if(graphStream!=null&&service!=null){
			service.submitWorkflow(new String(graphStream), serviceAdapter);
		}	
	}

	@Override
	public void addWorkflowExecutionListener(WorkflowExecutionListener listener) {
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}

	@Override
	public void removeWorkflowExecutionListener(
			WorkflowExecutionListener listener) {
		if(listeners.contains(listener)){
			listeners.remove(listener);
		}
	}
}
