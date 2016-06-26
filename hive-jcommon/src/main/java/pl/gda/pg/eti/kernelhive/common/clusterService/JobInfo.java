/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2016 Adrian Boguszewski
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

import java.util.Objects;

public class JobInfo {

	// basic info
	public GraphNodeType jobType;
	public Job.JobState state;
	public Integer unitID;
	public Integer ID;
	public String clusterHost;
	public Integer clusterTCPPort;
	public Integer clusterUDPPort;
	public String dataString;
	public String resultDataHost;
	public Integer resultDataPort;
	public String kernelString;
	public String inputDataUrl;
	// opencl info
	public String kernelHost;
	public Integer kernelPort;
	public Object kernelID;
	public String deviceID;
	public String offsets;
	public String globalSizes;
	public String localSizes;
	public String outputSize;
	public Integer nOutputs;
	public Integer dimensions;
	// encoder info
	public Integer fps;
	public String codec;
	public String format;
	// image info
	public Integer frameWidth;
	public Integer frameHeight;
	// convolution info
	public String filterMatrix;
	// sum info
	public Float firstImageWeight;
	public Float secondImageWeight;

	@Override
	public String toString() {
		return " " + Objects.toString(jobType.toString(), "") +
				" " + Objects.toString(ID, "") +
				" " + Objects.toString(clusterHost, "") +
				" " + Objects.toString(clusterTCPPort, "") +
				" " + Objects.toString(clusterUDPPort, "") +
				" " + Objects.toString(dataString, "") +
				" " + Objects.toString(nOutputs, "") +
				" " + Objects.toString(resultDataHost, "") +
				" " + Objects.toString(resultDataPort, "") +
				" " + Objects.toString(deviceID, "") +
				" " + Objects.toString(dimensions, "") +
				" " + Objects.toString(offsets, "") +
				" " + Objects.toString(globalSizes, "") +
				" " + Objects.toString(localSizes, "") +
				" " + Objects.toString(outputSize, "") +
				" " + Objects.toString(kernelHost, "") +
				" " + Objects.toString(kernelPort, "") +
				" " + Objects.toString(kernelID, "") +
				" " + Objects.toString(frameWidth, "") +
				" " + Objects.toString(frameHeight, "") +
				" " + Objects.toString(fps, "") +
				" " + Objects.toString(codec, "") +
				" " + Objects.toString(format, "") +
				" " + Objects.toString(filterMatrix, "") +
				" " + Objects.toString(firstImageWeight, "") +
				" " + Objects.toString(secondImageWeight, "");
	}


}
