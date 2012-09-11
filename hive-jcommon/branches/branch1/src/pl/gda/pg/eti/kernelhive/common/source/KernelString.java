package pl.gda.pg.eti.kernelhive.common.source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class KernelString extends KernelSource implements IKernelString {

	private String kernel;
	
	
	public KernelString(String id, String kernel){
		super(id, null);
		this.kernel = kernel;
	}
	
	public KernelString(String id, String kernel, Map<String, Object> properties){
		super(id, properties);
		this.kernel = kernel;
	}
	
	@Override
	public String getKernel() {
		return kernel;
	}

	@Override
	public List<ValidationResult> validate() {
		List<ValidationResult> result = new ArrayList<ValidationResult>();
		if(getGlobalSize()==null){
			result.add(new ValidationResult("Kernel with id: "+id+" has no global size defined", ValidationResultType.INVALID));
		}
		if(getLocalSize()==null){
			result.add(new ValidationResult("Kernel with id: "+id+" has no local size defined", ValidationResultType.INVALID));
		}
		if(getOffset()==null){
			result.add(new ValidationResult("Kernel with id: "+id+" has no offset defined", ValidationResultType.INVALID));
		}
		if(getOutputSize()==-1){
			result.add(new ValidationResult("Kernel with id: "+id+" has no output size defined", ValidationResultType.INVALID));
		}
		if(result.size()==0){//everything ok
			result.add(new ValidationResult("Kernel with id: "+id+" validated correctly", ValidationResultType.VALID));
		}
		return result;
	}	
}
