package pl.gda.pg.eti.kernelhive.common.validation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.factory.IGraphNodeFactory;
import pl.gda.pg.eti.kernelhive.common.graph.factory.impl.GraphNodeFactory;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.util.NodeIdGenerator;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;


public class GraphValidatorTest {

	private List<IGraphNode> projectNodes = null;
	
	@Before
	public void setUp() throws Exception {
		projectNodes = new ArrayList<IGraphNode>();
		IGraphNodeFactory factory = new GraphNodeFactory();
		IGraphNode node1 = factory.createGraphNode(GraphNodeType.GENERIC);
		node1.setNodeId(NodeIdGenerator.generateId());
		IGraphNode node2 = factory.createGraphNode(GraphNodeType.GENERIC);
		node2.setNodeId(NodeIdGenerator.generateId());
		projectNodes.add(node1);
		projectNodes.add(node2);
		
	}

	@Test
	public void testValidateGraphPass() {
		projectNodes.get(0).addFollowingNode(projectNodes.get(1));
		List<ValidationResult> list = GraphValidator.validateGraph(projectNodes);
		for(ValidationResult res : list){
			assertEquals(res.getMesssage(), ValidationResultType.VALID, res.getType());
		}
	}
	
	@Test
	public void testValidateGraphFail(){
		fail("not yest implemented");
	}

}
