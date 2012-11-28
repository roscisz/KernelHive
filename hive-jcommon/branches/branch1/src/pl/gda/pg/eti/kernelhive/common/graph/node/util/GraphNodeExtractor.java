package pl.gda.pg.eti.kernelhive.common.graph.node.util;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNode;

/**
 * 
 * @author mschally
 * 
 */
public class GraphNodeExtractor {

	/**
	 * 
	 * @param decorators
	 * @return
	 */
	public static List<IGraphNode> extractGraphNodesForGUI(
			List<GUIGraphNodeDecorator> decorators) {
		List<IGraphNode> result = new ArrayList<IGraphNode>();
		for (GUIGraphNodeDecorator a : decorators) {
			result.add(a.getGraphNode());
		}
		return result;
	}

	/**
	 * 
	 * @param decorators
	 * @return
	 */
	public static List<IGraphNode> extractGraphNodesForEngine(
			List<EngineGraphNodeDecorator> decorators) {
		List<IGraphNode> result = new ArrayList<IGraphNode>();
		for (EngineGraphNodeDecorator a : decorators) {
			result.add(a.getGraphNode());
		}
		return result;
	}
}
