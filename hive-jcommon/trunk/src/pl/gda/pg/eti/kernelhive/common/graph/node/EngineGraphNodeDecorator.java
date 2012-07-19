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
	
	public EngineGraphNodeDecorator(IGraphNode node, List<IKernelString> kernels){
		super(node);
		this.kernels = kernels;
	}
	
	public List<IKernelString> getKernels(){
		return kernels;
	}
	
	public void setKernels(List<IKernelString> kernels){
		this.kernels = kernels;
	}

	@Override
	public List<ValidationResult> validate() {
		List<ValidationResult> result = new ArrayList<ValidationResult>();
		result.add(new ValidationResult("ok", ValidationResultType.VALID));
		return result;
	}

}
