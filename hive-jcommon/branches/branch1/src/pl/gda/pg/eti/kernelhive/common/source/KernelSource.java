package pl.gda.pg.eti.kernelhive.common.source;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;

/**
 * 
 * @author mschally
 *
 */
public abstract class KernelSource implements IKernelSource {
	
	protected String id;
	protected Map<String, Object> properties;
	
	public KernelSource(String id, Map<String, Object> properties){
		this.id = id;
		if(properties!=null){
			this.properties = properties;
		} else{
			this.properties = new HashMap<String, Object>();
		}
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
	public abstract List<ValidationResult> validate();

	@Override
	public int getOutputSize() {
		int outputSize = -1;
		try{
			String outputSizeStr = (String) properties.get(OUTPUT_SIZE);
			outputSize = Integer.parseInt(outputSizeStr);
		} catch(NumberFormatException e){
			return -1;
		}
		return outputSize;
	}

}
