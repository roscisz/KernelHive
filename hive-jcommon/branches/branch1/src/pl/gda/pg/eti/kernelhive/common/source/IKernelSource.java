package pl.gda.pg.eti.kernelhive.common.source;

import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;

/**
 * 
 * @author mschally
 *
 */
public interface IKernelSource {

	public static final String GLOBAL_SIZES = "globalSizes";
	public static final String LOCAL_SIZES = "localSizes";
	public static final String OFFSETS = "offsets";
	public static final String OUTPUT_SIZE = "outputSize";
	
	Map<String, Object> getProperties();
	String getId();
	int[] getGlobalSize();
	int[] getLocalSize();
	int[] getOffset();
	int getOutputSize();
	List<ValidationResult> validate();
}
