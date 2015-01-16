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
package pl.gda.pg.eti.kernelhive.common.clusterService;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

public class JobInfo {
	
	public int unitID;
	// change to localhost? 
	private int resultDataPort = 31339;
	public String kernelString;
	public GraphNodeType jobType;
	public String inputDataUrl;
	
	public String kernelHost;
	public int kernelPort;
	public int kernelID;
	public int ID;
	public String clusterHost;
	public int clusterTCPPort;
	public int clusterUDPPort;
	public String deviceID;
	public String offsets;
	public String globalSizes;
	public String localSizes;
	public String outputSize;
	public String dataString;
	public int nOutputs;
	public String resultDataHost;
	public Job.JobState state;
	
	@Override
	public String toString() {	
		StringBuilder ret = new StringBuilder();
		
		ret.append(" " + jobType.toString());
		ret.append(" " + ID);
		ret.append(" " + clusterHost);
		ret.append(" " + clusterTCPPort);
		ret.append(" " + clusterUDPPort);
		ret.append(" " + deviceID);		
		ret.append(" 3");
		ret.append(" " + offsets);
		ret.append(" " + globalSizes);
		ret.append(" " + localSizes);
		//ret.append(" 3 0 0 0 512 1 1 64 1 1");
		ret.append(" " + outputSize);
		ret.append(" " + kernelHost);
		ret.append(" " + kernelPort);
		ret.append(" " + kernelID);
		ret.append(" " + dataString);
		
		//ret.append(" " + nOutputs)
		
		// TODO: dynamic output number assignment
		//if(jobType == GraphNodeType.PARTITIONER)
		//	ret.append(" 2");
		//else ret.append(" 1");
		ret.append(" " + nOutputs);
		
		ret.append(" " + resultDataHost);
		ret.append(" " + resultDataPort);
		
		return ret.toString();
	}
	
	
}
