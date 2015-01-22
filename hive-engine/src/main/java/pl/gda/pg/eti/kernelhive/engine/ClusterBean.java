/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
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
package pl.gda.pg.eti.kernelhive.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.JobInfo;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.PreviewObject;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IClusterBeanRemote;

/**
 * Session Bean implementation class ClusterBean
 */
@WebService
@Stateless
public class ClusterBean implements IClusterBeanRemote {

	@Resource
	private WebServiceContext context;

	/**
	 * Default constructor.
	 */
	public ClusterBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	@WebMethod
	public int update(Cluster data) {
		String ip = getIPFromContext(this.context);
		data.updateReverseReferences();
		HiveEngine.getInstance().updateCluster(data, ip);
		System.out.println("Updated cluster: " + data + ", units: " + data.getUnitList().size());
		return data.id;
	}

	@Override
	@WebMethod
	public JobInfo getJob() {
		String ip = getIPFromContext(this.context);
		Cluster cluster = HiveEngine.getInstance().getClusterByIp(ip);
		if (cluster == null) {
			System.out.println("getJob() returning null because no such cluster " + ip);
			return null;
		}
		Job job = cluster.getJob();
		if (job == null) {
			return null;
		}
		return job.getJobInfo();
	}

	@Override
	@WebMethod
	public void reportProgress(int jobID, int progress) {
		HiveEngine.getInstance().onProgress(jobID, progress);
	}

	@Override
	@WebMethod
	public void reportOver(int jobID, String returnData) {
		HiveEngine.getInstance().onJobOver(jobID, returnData);
	}

	private String getIPFromContext(WebServiceContext wsc) {
		MessageContext mc = wsc.getMessageContext();
		HttpServletRequest hsr = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
		return hsr.getRemoteAddr();
	}

	@WebMethod
	@Override
	public void reportPreview(int jobID, byte[] data) {
		StringBuilder sb = new StringBuilder("Got preview:\n");

		List<PreviewObject> pos = new ArrayList<>();
		for (int i = 0; i + 12 < data.length; i += 12) {
			PreviewObject po = new PreviewObject();
			po.setF1(ByteBuffer.wrap(data, i, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat());
			po.setF2(ByteBuffer.wrap(data, i + 4, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat());
			po.setF3(ByteBuffer.wrap(data, i + 8, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat());
			pos.add(po);
		}

		for (PreviewObject po : pos) {
			sb.append(po.toString());
			sb.append("\n");
		}
		Logger.getLogger(getClass().getName()).info(sb.toString());

		HiveEngine.getInstance().saveJobPreview(jobID, pos);
	}
}
