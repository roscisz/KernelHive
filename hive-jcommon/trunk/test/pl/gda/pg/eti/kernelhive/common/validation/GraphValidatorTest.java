package pl.gda.pg.eti.kernelhive.common.validation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;


public class GraphValidatorTest {

	private List<IGraphNode> projectNodes = null;
	
	@Before
	public void setUp() throws Exception {
		projectNodes = new ArrayList<IGraphNode>();
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testValidateGraphPass() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testValidateGraphFail(){
		
	}

}
