package pl.gda.pg.eti.kernelhive.engine;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.JobInfo;
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
		data.updateReverseReferences();
		HiveEngine.getInstance().updateCluster(ip, data);
	}
	
	@Override
	@WebMethod
	public JobInfo getJob() {
		String ip = getIPFromContext(this.context);
		Cluster cluster = HiveEngine.getInstance().getCluster(ip);
		if(cluster == null) return null;
		return cluster.getJob().getJobInfo();		
	}
	
	
	
	private String getIPFromContext(WebServiceContext wsc) {
		MessageContext mc = wsc.getMessageContext();
		HttpServletRequest hsr = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
		return hsr.getRemoteAddr();
	}
}
