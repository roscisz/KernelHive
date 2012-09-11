package pl.gda.pg.eti.kernelhive.common.validation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.graph.builder.IGraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.common.graph.builder.impl.GraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.source.IKernelFile;
import pl.gda.pg.eti.kernelhive.common.source.IKernelSource;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;
import pl.gda.pg.eti.kernelhive.common.source.KernelFile;
import pl.gda.pg.eti.kernelhive.common.source.KernelString;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphValidatorTest {

	private List<EngineGraphNodeDecorator> engineGraphNodes;
	private List<GUIGraphNodeDecorator> guiGraphNodes;

	@Before
	public void setUp() throws Exception {
		engineGraphNodes = new ArrayList<EngineGraphNodeDecorator>();
		guiGraphNodes = new ArrayList<GUIGraphNodeDecorator>();
		
		IGraphNodeBuilder builder = new GraphNodeBuilder();
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(IKernelSource.GLOBAL_SIZES, "1 1 1");
		props.put(IKernelSource.LOCAL_SIZES, "1 1 1");
		props.put(IKernelSource.OFFSETS, "1 1 1");
		props.put(IKernelSource.OUTPUT_SIZE, "1024");
		IGraphNode node1 = builder.setId("test1").setName("test1")
				.setType(GraphNodeType.PARTITIONER).build();
		IGraphNode node2 = builder.setId("test2").setName("test2")
				.setType(GraphNodeType.PROCESSOR).build();
		IGraphNode node3 = builder.setId("test3").setName("test3")
				.setType(GraphNodeType.PROCESSOR).build();
		IGraphNode node4 = builder.setId("test4").setName("test4")
				.setType(GraphNodeType.MERGER).build();
		node1.addFollowingNode(node2);
		node1.addFollowingNode(node3);
		node2.addFollowingNode(node4);
		node3.addFollowingNode(node4);
		
		List<IKernelString> kernelStrings = new ArrayList<IKernelString>();
		kernelStrings.add(new KernelString("k1", " ", props));
		List<IKernelFile> kernelFiles = new ArrayList<IKernelFile>();
		kernelFiles.add(new KernelFile(File.createTempFile("test", "test"), "k1", props));
		engineGraphNodes.add(new EngineGraphNodeDecorator(node1, kernelStrings));
		engineGraphNodes.add(new EngineGraphNodeDecorator(node2, kernelStrings));
		engineGraphNodes.add(new EngineGraphNodeDecorator(node3, kernelStrings));
		engineGraphNodes.add(new EngineGraphNodeDecorator(node4, kernelStrings));
		
		guiGraphNodes.add(new GUIGraphNodeDecorator(node1, kernelFiles));
		guiGraphNodes.add(new GUIGraphNodeDecorator(node2, kernelFiles));
		guiGraphNodes.add(new GUIGraphNodeDecorator(node3, kernelFiles));
		guiGraphNodes.add(new GUIGraphNodeDecorator(node4, kernelFiles));
	}

	@Test
	public final void testValidateGraphForGUI() {
		List<ValidationResult> result = GraphValidator.validateGraphForGUI(guiGraphNodes);
		for(ValidationResult vr : result){
			assertEquals(vr.getMesssage(), ValidationResultType.VALID, vr.getType());
		}
	}

	@Test
	public final void testValidateGraphForEngine() {
		List<ValidationResult> result = GraphValidator.validateGraphForEngine(engineGraphNodes);
		for(ValidationResult vr : result){
			assertEquals(vr.getMesssage(), ValidationResultType.VALID, vr.getType());
		}
	}

}

