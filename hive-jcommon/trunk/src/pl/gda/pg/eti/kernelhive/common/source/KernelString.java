package pl.gda.pg.eti.kernelhive.common.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class KernelString implements IKernelString {

	private String id;
	private String kernel;
	private Map<String, Object> properties;
	
	public KernelString(String id, String kernel){
		this.id = id;
		this.kernel = kernel;
		this.properties = new HashMap<String, Object>();
	}
	
	public KernelString(String id, String kernel, Map<String, Object> properties){
		this.id = id;
		this.kernel = kernel;
		if(properties!=null){
			this.properties = properties;
		} else{
			this.properties = new HashMap<String, Object>();
		}
	}
	
	@Override
	public String getKernel() {
		return kernel;
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int[] getGlobalSize() {
		try{
			String s = (String) properties.get(GLOBAL_SIZES);
			String[] sizesStr = s.trim().split(" ");
			if(sizesStr.length<=3){
				int[] globalSize = new int[3];
				for(int i=0; i<sizesStr.length; i++){
					globalSize[i] = Integer.parseInt(sizesStr[i]);
				}
				return globalSize;
			}
		} catch (NullPointerException e){
			return null;
		}		
		return null;
	}

	@Override
	public int[] getLocalSize() {
		try{
			String s = (String) properties.get(LOCAL_SIZES);
			String[] sizesStr = s.trim().split(" ");
			if(sizesStr.length<=3){
				int[] localSize = new int[3];
				for(int i=0; i<sizesStr.length; i++){
					localSize[i] = Integer.parseInt(sizesStr[i]);
				}
				return localSize;
			}
		} catch (NullPointerException e){
			return null;
		}		
		return null;
	}

	@Override
	public int[] getOffset() {
		try{
			String s = (String) properties.get(OFFSETS);
			String[] sizesStr = s.trim().split(" ");
			if(sizesStr.length<=3){
				int[] offset = new int[3];
				for(int i=0; i<sizesStr.length; i++){
					offset[i] = Integer.parseInt(sizesStr[i]);
				}
				return offset;
			}
		} catch (NullPointerException e){
			return null;
		}		
		return null;
	}

	@Override
	public List<ValidationResult> validate() {
		List<ValidationResult> result = new ArrayList<ValidationResult>();
		if(getGlobalSize()==null){
			result.add(new ValidationResult("no global size defined", ValidationResultType.INVALID));
		}
		if(getLocalSize()==null){
			result.add(new ValidationResult("no local size defined", ValidationResultType.INVALID));
		}
		if(getOffset()==null){
			result.add(new ValidationResult("no offset defined", ValidationResultType.INVALID));
		}
		if(result.size()==0){//everything ok
			result.add(new ValidationResult("ok", ValidationResultType.VALID));
		}
		return result;
	}

}
