package pl.gda.pg.eti.kernelhive.common.graph.node;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.source.IKernelString;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class EngineGraphNodeDecorator extends AbstractGraphNodeDecorator {

	List<IKernelString> kernels;

	public EngineGraphNodeDecorator(IGraphNode node) {
		super(node);
		kernels = new ArrayList<IKernelString>();
	}

	public EngineGraphNodeDecorator(IGraphNode node, List<IKernelString> kernels) {
		super(node);
		this.kernels = kernels;
	}

	public List<IKernelString> getKernels() {
		return kernels;
	}

	public void setKernels(List<IKernelString> kernels) {
		this.kernels = kernels;
	}

	@Override
	public List<ValidationResult> validate() {
		//validate node
		List<ValidationResult> result = node.validate();
		// validate kernels
		for (IKernelString ks : kernels) {
			result.addAll(ks.validate());
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kernels == null) ? 0 : kernels.hashCode())
				+ ((node == null) ? 0 : node.hashCode());
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
		EngineGraphNodeDecorator other = (EngineGraphNodeDecorator) obj;
		if (kernels == null) {
			if (other.kernels != null)
				return false;
		} else if (!kernels.equals(other.kernels))
			return false;
		if (node == null) {
			if (other.getGraphNode() != null)
				return false;
		} else if (!node.equals(other.getGraphNode()))
			return false;
		return true;
	}
}
