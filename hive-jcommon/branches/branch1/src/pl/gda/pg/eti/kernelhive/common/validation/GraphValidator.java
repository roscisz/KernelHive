package pl.gda.pg.eti.kernelhive.common.validation;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.util.GraphNodeExtractor;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNode;

/**
 * @author mschally
 * 
 */
public class GraphValidator {

	/**
	 * validates graph
	 * 
	 * @param graph
	 *            - list of graph node decorators
	 * @return list of {@link ValidationResult} objects
	 */
	public static List<ValidationResult> validateGraphForGUI(
			List<GUIGraphNodeDecorator> graph) {
		List<ValidationResult> result = new ArrayList<ValidationResult>();
		List<IGraphNode> graphNodes = GraphNodeExtractor
				.extractGraphNodesForGUI(graph);
		result.addAll(validateStartGraphNodes(graphNodes));
		result.addAll(validateEndGraphNodes(graphNodes));
		result.addAll(validateChildrenGraphNodes(graphNodes));
		for (GUIGraphNodeDecorator node : graph) {
			result.addAll(node.validate());
		}
		return result;
	}

	/**
	 * 
	 * @param graph
	 * @return
	 */
	public static List<ValidationResult> validateGraphForEngine(
			List<EngineGraphNodeDecorator> graph) {
		List<ValidationResult> result = new ArrayList<ValidationResult>();
		List<IGraphNode> graphNodes = GraphNodeExtractor
				.extractGraphNodesForEngine(graph);
		result.addAll(validateStartGraphNodes(graphNodes));
		result.addAll(validateEndGraphNodes(graphNodes));
		result.addAll(validateChildrenGraphNodes(graphNodes));
		for (EngineGraphNodeDecorator a : graph) {
			result.addAll(a.validate());
		}
		return result;
	}

	// private static List<ValidationResult> validateGraphNodes(
	// List<IGraphNode> graphNodes) {
	// List<ValidationResult> results = new ArrayList<ValidationResult>();
	// results.addAll(validateStartGraphNodes(graphNodes));
	// results.addAll(validateEndGraphNodes(graphNodes));
	// for (IGraphNode node : graphNodes) {
	// results.addAll(node.validate());
	// }
	// results.addAll(validateChildrenGraphNodes(graphNodes));
	// return results;
	// }

	private static List<ValidationResult> validateStartGraphNodes(
			List<IGraphNode> graphNodes) {
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		int count = 0;
		for (IGraphNode node : graphNodes) {
			if ((node.getParentNode() == null)
					&& (node.getPreviousNodes().size() == 0)) {
				count++;
			}
		}
		if (count > 1) {
			results.add(new ValidationResult(
					"More than 1 start node is not allowed",
					ValidationResultType.INVALID));
		} else if (count == 0) {
			results.add(new ValidationResult(
					"Cyclic graph detected - only directed acyclic graphs are permitted",
					ValidationResultType.INVALID));
		} else {
			results.add(new ValidationResult("Start Graph Nodes Validation OK",
					ValidationResultType.VALID));
		}
		return results;
	}

	private static List<ValidationResult> validateEndGraphNodes(
			List<IGraphNode> graphNodes) {
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		int count = 0;
		for (IGraphNode node : graphNodes) {
			if ((node.getParentNode() == null)
					&& (node.getFollowingNodes().size() == 0)) {
				count++;
			}
		}
		if (count > 1) {
			results.add(new ValidationResult(
					"More than 1 end node is not allowed",
					ValidationResultType.INVALID));
		} else if (count == 0) {
			results.add(new ValidationResult(
					"Cyclic graph detected - only directed acyclic graphs are permitted",
					ValidationResultType.INVALID));
		} else {
			results.add(new ValidationResult("End Graph Nodes Validation OK",
					ValidationResultType.VALID));
		}
		return results;
	}

	private static List<ValidationResult> validateChildrenGraphNodes(
			List<IGraphNode> graphNodes) {
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		for (IGraphNode parent : graphNodes) {
			if (parent.getChildrenNodes().size() > 0) {
				int eCount = 0, sCount = 0;
				for (IGraphNode node : parent.getChildrenNodes()) {
					if (node.getFollowingNodes().size() == 0) {
						eCount++;
					}
					if (node.getPreviousNodes().size() == 0) {
						sCount++;
					}
					for (IGraphNode followingNode : node.getFollowingNodes()) {
						if (followingNode.getParentNode() != parent) {
							results.add(new ValidationResult("Node ("
									+ node.getNodeId() + ") and Node ("
									+ followingNode.getNodeId()
									+ ") do not have the same parent",
									ValidationResultType.INVALID));
						}
					}
				}
				if (eCount == 0 || sCount == 0) {
					results.add(new ValidationResult(
							"Cyclic graph in parent node ("
									+ parent.getNodeId()
									+ ") detected - only directed acyclic graphs are permitted",
							ValidationResultType.INVALID));
				}
			}
		}
		if (results.size() == 0) {
			results.add(new ValidationResult("", ValidationResultType.VALID));
		}

		return results;
	}
}
