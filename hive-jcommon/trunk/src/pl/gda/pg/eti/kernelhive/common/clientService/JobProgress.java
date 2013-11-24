/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.common.clientService;

import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

/**
 *
 * @author szymon
 */
public class JobProgress {

	JobState state;
	int progress;
	int id;
	GraphNodeType type;

	public JobProgress() {
	}

	public JobProgress(int id, GraphNodeType type, JobState state, int progress) {
		this.id = id;
		this.progress = progress;
		this.state = state;
		this.type = type;
	}

	public JobState getState() {
		return state;
	}

	public void setState(JobState state) {
		this.state = state;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GraphNodeType getType() {
		return type;
	}

	public void setType(GraphNodeType type) {
		this.type = type;
	}
}
