package pl.gda.pg.eti.kernelhive.engine;

import java.io.StringReader;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Task;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.impl.EngineGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IClientBeanRemote;

/**
 * Session Bean implementation class ClientBean
 */
@WebService
@Stateless
public class ClientBean implements IClientBeanRemote {
	
	private static String serializedGraphConf = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><kh:project><kh:node hash=\"957924158\" id=\"4a5a50af-0f69-4736-b378-386ece17c948\" name=\"end node\" parent-id=\"\" type=\"generic\"><kh:first-children-nodes/><kh:send-to/><kh:properties/><kh:kernels/></kh:node><kh:node hash=\"-1919695949\" id=\"1f9ce14f-6925-464c-bb5f-78649331951e\" name=\"process node 3\" parent-id=\"\" type=\"generic\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"4a5a50af-0f69-4736-b378-386ece17c948\"/></kh:send-to><kh:properties/><kh:kernels/></kh:node><kh:node hash=\"-1151135753\" id=\"c123adbc-0fb3-472b-bc45-7ff1d3ea8ab8\" name=\"process node 2\" parent-id=\"\" type=\"generic\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"1f9ce14f-6925-464c-bb5f-78649331951e\"/></kh:send-to><kh:properties/><kh:kernels/></kh:node><kh:node hash=\"-300588188\" id=\"5bfc2f36-e6a8-4521-9a07-2b6a403b71ad\" name=\"process node 1\" parent-id=\"\" type=\"generic\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"c123adbc-0fb3-472b-bc45-7ff1d3ea8ab8\"/></kh:send-to><kh:properties/><kh:kernels><kh:kernel id=\"2\" src=\"\"/></kh:kernels></kh:node><kh:node hash=\"-1025183831\" id=\"025b9746-d00b-421a-9ca0-0002f97e6847\" name=\"start node\" parent-id=\"\" type=\"generic\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"5bfc2f36-e6a8-4521-9a07-2b6a403b71ad\"/></kh:send-to><kh:properties><kh:property key=\"run-on\" value=\"cpu\"/></kh:properties><kh:kernels><kh:kernel id=\"1\" src=\"\"/></kh:kernels></kh:node></kh:project>";
	
    /**
     * Default constructor. 
     */
    public ClientBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	@WebMethod
	public Integer runTask(String serializedGraphConf) {
		EngineGraphConfiguration egc = new EngineGraphConfiguration();
		try {
			List<EngineGraphNodeDecorator> nodes = egc.loadGraphForEngine(new StringReader(ClientBean.serializedGraphConf));
			return HiveEngine.getInstance().runTask(nodes);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			return null;
		}	
	}

	@Override
	public List<Task> browseTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getResults(Integer taskID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTask(Integer taskID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Cluster> browseInfrastructure() {
		// TODO Auto-generated method stub
		return null;
	}

}
