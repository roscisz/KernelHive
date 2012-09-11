package pl.gda.pg.eti.kernelhive.common.source;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

/**
 * 
 * @author mschally
 *
 */
public class KernelFile extends KernelSource implements IKernelFile, Serializable {

	private static final long serialVersionUID = 192995314148788181L;
	protected File file;
	
	public KernelFile(File file, String id){
		super(id, null);
		this.file = file;
	}
	
	public KernelFile(File file, String id, Map<String, Object> properties){
		super(id, properties);
		this.file = file;
	}
	
	@Override
	public File getFile() {
		return file;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String toString(){
		return this.file.getAbsolutePath();
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
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
		if(!file.exists()||!file.canRead()||!file.isFile()){
			result.add(new ValidationResult("Kernel with id: "+id+" has no valid file attached", ValidationResultType.INVALID));
		}
		if(result.size()==0){//everything ok
			result.add(new ValidationResult("Kernel with id: "+id+" validated correctly", ValidationResultType.VALID));
		}
		return result;	
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
