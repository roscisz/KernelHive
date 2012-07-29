package pl.gda.pg.eti.kernelhive.engine;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
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
	public void update(Cluster data) {		
		String ip = getIPFromContext(this.context);
		HiveEngine.getInstance().updateCluster(ip, data);		
	}
	
	@Override
	public Job getJob() {
		String ip = getIPFromContext(this.context);
		Cluster cluster = HiveEngine.getInstance().getCluster(ip);
		try 
		{
			// wait until there is a task for this Cluster, then return it
			while(true) {
				synchronized (cluster) {
					cluster.wait();
					if(cluster.jobsToRun.size() > 0) {
						Job jobToRun = cluster.jobsToRun.get(0);
						cluster.jobsToRun.remove(0);
						return jobToRun;
					}
				}
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	private String getIPFromContext(WebServiceContext wsc) {
		MessageContext mc = wsc.getMessageContext();
		HttpServletRequest hsr = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
		return hsr.getRemoteAddr();
	}
}
