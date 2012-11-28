package pl.gda.pg.eti.kernelhive.common.graph.node;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.source.IKernelFile;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNode;

/**
 * 
 * @author mschally
 * 
 */
public class GUIGraphNodeDecorator extends AbstractGraphNodeDecorator {

	List<IKernelFile> sourceFiles;
	int x, y;

	public GUIGraphNodeDecorator(IGraphNode node) {
		super(node);
		this.sourceFiles = new ArrayList<IKernelFile>();
	}

	public GUIGraphNodeDecorator(IGraphNode node, List<IKernelFile> sourceFiles) {
		super(node);
		this.sourceFiles = sourceFiles;
	}

	@Override
	public List<ValidationResult> validate() {
		//validate node
		List<ValidationResult> result = node.validate();
		//validate source files
		for (IKernelFile s : sourceFiles) {
			result.addAll(s.validate());
		}

		if (isValidationSuccess(result)) {
			List<ValidationResult> finalResult = new ArrayList<ValidationResult>();
			finalResult.add(new ValidationResult("Graph node (id: "
					+ node.getNodeId() + ", name: " + node.getName()
					+ ") and its kernels validated successfully",
					ValidationResultType.VALID));
			return finalResult;
		} else {
			return result;
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 
	 * @return
	 */
	public List<IKernelFile> getSourceFiles() {
		return sourceFiles;
	}

	/**
	 * 
	 * @param sourceFiles
	 */
	public void setSourceFiles(List<IKernelFile> sourceFiles) {
		this.sourceFiles = sourceFiles;
	}

	@Override
	public String toString() {
		return node.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sourceFiles == null) ? 0 : sourceFiles.hashCode())
				+ ((node == null) ? 0 : node.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GUIGraphNodeDecorator other = (GUIGraphNodeDecorator) obj;
		if (sourceFiles == null) {
			if (other.sourceFiles != null)
				return false;
		} else if (!sourceFiles.equals(other.sourceFiles))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (node == null) {
			if (other.getGraphNode() != null)
				return false;
		} else if (!node.equals(other.getGraphNode()))
			return false;
		return true;
	}
}
