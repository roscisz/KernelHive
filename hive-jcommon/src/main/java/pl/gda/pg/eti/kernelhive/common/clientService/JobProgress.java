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
package pl.gda.pg.eti.kernelhive.common.clientService;

import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

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
