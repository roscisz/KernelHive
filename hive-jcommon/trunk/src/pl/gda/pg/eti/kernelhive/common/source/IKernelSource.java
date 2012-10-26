package pl.gda.pg.eti.kernelhive.common.source;

import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRoleEnum;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;

/**
 * 
 * @author mschally
 * 
 */
public interface IKernelSource {

	static final String GLOBAL_SIZES = "globalSizes";
	static final String LOCAL_SIZES = "localSizes";
	static final String OFFSETS = "offsets";
	static final String OUTPUT_SIZE = "outputSize";
	static final String KERNEL_ROLE = "role";

	Map<String, Object> getProperties();

	String getId();

	int[] getGlobalSize();

	int[] getLocalSize();

	int[] getOffset();

	int getOutputSize();

	KernelRoleEnum getKernelRole();

	List<ValidationResult> validate();
}
