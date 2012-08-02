package pl.gda.pg.eti.kernelhive.common.source;

import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;

public interface IKernelString {

	String getKernel();
	Map<String, Object> getProperties();
	String getId();
	int[] getGlobalSize();
	int[] getLocalSize();
	int[] getOffset();
	List<ValidationResult> validate();
}
