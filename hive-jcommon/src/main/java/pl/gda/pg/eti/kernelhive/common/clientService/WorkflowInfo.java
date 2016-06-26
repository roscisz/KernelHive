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
package pl.gda.pg.eti.kernelhive.common.clientService;

public class WorkflowInfo {
	
	public enum WorkflowState {
		PENDING("pending"), COMPLETED("completed"), TERMINATED("terminated"), SUBMITTED(
				"submitted"), ERROR("error"), WARNING("warning"), PROCESSING(
				"processing");

		private String state;

		WorkflowState(String state) {
			this.state = state;
		}

		@Override
		public String toString() {
			return state;
		}
	}
	
	public int ID;
	public String name;
	public WorkflowState state;
	public String result;
	public double elapsedTime;

}
