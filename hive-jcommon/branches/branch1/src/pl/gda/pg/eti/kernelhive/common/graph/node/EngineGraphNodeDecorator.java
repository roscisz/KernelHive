package pl.gda.pg.eti.kernelhive.common.graph.node;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.source.IKernelString;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.repository.kernel.repository.KernelRoleEnum;

public class EngineGraphNodeDecorator extends AbstractGraphNodeDecorator {

	List<IKernelString> kernels;

	public EngineGraphNodeDecorator(final IGraphNode node) {
		super(node);
		kernels = new ArrayList<IKernelString>();
	}

	public EngineGraphNodeDecorator(final IGraphNode node,
			final List<IKernelString> kernels) {
		super(node);
		this.kernels = kernels;
	}

	public List<IKernelString> getKernels() {
		return kernels;
	}

	public void setKernels(final List<IKernelString> kernels) {
		this.kernels = kernels;
	}

	public List<IKernelString> getPartitionKernels() {
		final List<IKernelString> result = new ArrayList<IKernelString>();
		for (final IKernelString kernel : kernels) {
			if (kernel.getKernelRole().equals(KernelRoleEnum.PARTITION)) {
				result.add(kernel);
			}
		}
		return result;
	}

	public List<IKernelString> getMergeKernels() {
		final List<IKernelString> result = new ArrayList<IKernelString>();
		for (final IKernelString kernel : kernels) {
			if (kernel.getKernelRole().equals(KernelRoleEnum.MERGE)) {
				result.add(kernel);
			}
		}
		return result;
	}

	public List<IKernelString> getProcessKernels() {
		final List<IKernelString> result = new ArrayList<IKernelString>();
		for (final IKernelString kernel : kernels) {
			if (kernel.getKernelRole().equals(KernelRoleEnum.PROCESS)) {
				result.add(kernel);
			}
		}
		return result;
	}

	@Override
	public List<ValidationResult> validate() {
		// validate node
		final List<ValidationResult> result = node.validate();
		// validate kernels
		for (final IKernelString ks : kernels) {
			result.addAll(ks.validate());
		}

		if (isValidationSuccess(result)) {
			final List<ValidationResult> finalResult = new ArrayList<ValidationResult>();
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
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final EngineGraphNodeDecorator other = (EngineGraphNodeDecorator) obj;
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
