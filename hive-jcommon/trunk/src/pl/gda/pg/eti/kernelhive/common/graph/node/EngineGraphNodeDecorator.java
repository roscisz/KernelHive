/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.common.graph.node;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRoleEnum;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

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
