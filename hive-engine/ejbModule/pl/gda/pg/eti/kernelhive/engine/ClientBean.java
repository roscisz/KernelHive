package pl.gda.pg.eti.kernelhive.engine;

import java.io.StringReader;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Workflow;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.impl.EngineGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IClientBeanRemote;

/**
 * Session Bean implementation class ClientBean
 */
@WebService
@Stateless
public class ClientBean implements IClientBeanRemote {
	
	//private static String serializedGraphConf = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><kh:project><kh:node hash=\"957924158\" id=\"4a5a50af-0f69-4736-b378-386ece17c948\" name=\"end node\" parent-id=\"\" type=\"generic\"><kh:first-children-nodes/><kh:send-to/><kh:properties/><kh:kernels/></kh:node><kh:node hash=\"-1919695949\" id=\"1f9ce14f-6925-464c-bb5f-78649331951e\" name=\"process node 3\" parent-id=\"\" type=\"generic\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"4a5a50af-0f69-4736-b378-386ece17c948\"/></kh:send-to><kh:properties/><kh:kernels/></kh:node><kh:node hash=\"-1151135753\" id=\"c123adbc-0fb3-472b-bc45-7ff1d3ea8ab8\" name=\"process node 2\" parent-id=\"\" type=\"generic\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"1f9ce14f-6925-464c-bb5f-78649331951e\"/></kh:send-to><kh:properties/><kh:kernels/></kh:node><kh:node hash=\"-300588188\" id=\"5bfc2f36-e6a8-4521-9a07-2b6a403b71ad\" name=\"process node 1\" parent-id=\"\" type=\"generic\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"c123adbc-0fb3-472b-bc45-7ff1d3ea8ab8\"/></kh:send-to><kh:properties/><kh:kernels><kh:kernel id=\"2\" src=\"\"/></kh:kernels></kh:node><kh:node hash=\"-1025183831\" id=\"025b9746-d00b-421a-9ca0-0002f97e6847\" name=\"start node\" parent-id=\"\" type=\"generic\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"5bfc2f36-e6a8-4521-9a07-2b6a403b71ad\"/></kh:send-to><kh:properties><kh:property key=\"run-on\" value=\"cpu\"/></kh:properties><kh:kernels><kh:kernel id=\"1\" src=\"\"/></kh:kernels></kh:node></kh:project>";
	//private static String serializedGraphConf = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><kh:project><kh:node hash=\"-1341250129\" id=\"e038c1db-c4d9-4a61-b188-495c81200125\" name=\"name1\" parent-id=\"\" type=\"partitioner\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"8b584317-997f-4ef6-998a-86a1f21ff60b\"/><kh:following-node id=\"cbb7d846-ec04-4b8a-a9f6-a85bac9c694f\"/></kh:send-to><kh:properties/><kh:kernels><kh:kernel id=\"eff1aa1a-686b-49bb-8ba5-47f607f27a45\" src=\"kernel src\"><kh:property key=\"globalSizes\" value=\"1\"/><kh:property key=\"offsets\" value=\"0\"/><kh:property key=\"numberOfDimensions\" value=\"1\"/><kh:property key=\"localSizes\" value=\"1\"/></kh:kernel></kh:kernels></kh:node><kh:node hash=\"912777918\" id=\"a296c544-84cf-4886-a786-d13ae2a580a8\" name=\"name2\" parent-id=\"\" type=\"merger\"><kh:first-children-nodes/><kh:send-to/><kh:properties/><kh:kernels><kh:kernel id=\"680d7b83-c327-4d73-887f-3d4ed3ae14c4\" src=\"kernel src\"><kh:property key=\"globalSizes\" value=\"1\"/><kh:property key=\"offsets\" value=\"0\"/><kh:property key=\"numberOfDimensions\" value=\"1\"/><kh:property key=\"localSizes\" value=\"1\"/></kh:kernel></kh:kernels></kh:node><kh:node hash=\"1319965959\" id=\"8b584317-997f-4ef6-998a-86a1f21ff60b\" name=\"name3\" parent-id=\"\" type=\"processor\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"a296c544-84cf-4886-a786-d13ae2a580a8\"/></kh:send-to><kh:properties/><kh:kernels><kh:kernel id=\"bc0514aa-1716-46b1-8d5f-5a33dbf9f677\" src=\"kernel src\"><kh:property key=\"globalSizes\" value=\"1\"/><kh:property key=\"offsets\" value=\"0\"/><kh:property key=\"numberOfDimensions\" value=\"1\"/><kh:property key=\"localSizes\" value=\"1\"/></kh:kernel></kh:kernels></kh:node><kh:node hash=\"-1094065835\" id=\"cbb7d846-ec04-4b8a-a9f6-a85bac9c694f\" name=\"name5\" parent-id=\"\" type=\"processor\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"a296c544-84cf-4886-a786-d13ae2a580a8\"/></kh:send-to><kh:properties/><kh:kernels><kh:kernel id=\"bc0514aa-1716-46b1-8d5f-5a33dbf9f677\" src=\"kernel src\"><kh:property key=\"globalSizes\" value=\"1\"/><kh:property key=\"offsets\" value=\"0\"/><kh:property key=\"numberOfDimensions\" value=\"1\"/><kh:property key=\"localSizes\" value=\"1\"/></kh:kernel></kh:kernels></kh:node></kh:project>";
	private static String serializedGraphConf = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><kh:project><kh:node hash=\"-1341250129\" id=\"e038c1db-c4d9-4a61-b188-495c81200125\" name=\"name1\" parent-id=\"\" type=\"partitioner\"><kh:first-children-nodes/><kh:send-to><kh:following-node id=\"8b584317-997f-4ef6-998a-86a1f21ff60b\"/><kh:following-node id=\"cbb7d846-ec04-4b8a-a9f6-a85bac9c694f\"/></kh:send-to><kh:properties/><kh:kernels><kh:kernel id=\"eff1aa1a-686b-49bb-8ba5-47f607f27a45\" src=\"kernel src\"><kh:property key=\"globalSizes\" value=\"1 0 0\"/><kh:property key=\"offsets\" value=\"0 0 0\"/><kh:property key=\"numberOfDimensions\" value=\"1\"/><kh:property key=\"localSizes\" value=\"1 0 0\"/><kh:property key=\"globalSizes\" value=\"1 0 0\"/><kh:property key=\"offsets\" value=\"0 0 0\"/><kh:property key=\"numberOfDimensions\" value=\"1\"/><kh:property key=\"localSizes\" value=\"1 0 0\"/><kh:property key=\"globalSizes\" value=\"1 0 0\"/><kh:property key=\"offsets\" value=\"0 0 0\"/><kh:property key=\"numberOfDimensions\" value=\"1\"/><kh:property key=\"localSizes\" value=\"1 0 0\"/><kh:property key=\"globalSizes\" value=\"1 0 0\"/><kh:property key=\"offsets\" value=\"0 0 0\"/><kh:property key=\"numberOfDimensions\" value=\"1\"/><kh:property key=\"localSizes\" value=\"1 0 0\"/></kh:kernel></kh:kernels></kh:node></kh:project>"; 
	
    /**
     * Default constructor. 
     */
    public ClientBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	@WebMethod
	public Integer submitWorkflow(String serializedGraphConf) {
		EngineGraphConfiguration egc = new EngineGraphConfiguration();
		try {
			List<EngineGraphNodeDecorator> nodes = egc.loadGraphForEngine(new StringReader(ClientBean.serializedGraphConf));
			return HiveEngine.getInstance().runWorkflow(nodes);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			return null;
		}	
	}

	@Override
	@WebMethod
	public List<WorkflowInfo> browseWorkflows() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod
	public String getWorkflowResults(Integer taskID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod
	public void terminateWorkflow(Integer taskID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@WebMethod
	public List<ClusterInfo> browseInfrastructure() {	
		return HiveEngine.getInstance().getInfrastructureInfo();
	}

}
