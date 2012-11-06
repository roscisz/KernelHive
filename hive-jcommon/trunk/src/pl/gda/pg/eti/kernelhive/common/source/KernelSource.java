package pl.gda.pg.eti.kernelhive.common.source;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRoleEnum;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;

/**
 * 
 * @author mschally
 * 
 */
public abstract class KernelSource implements IKernelSource {

	protected String id;
	protected Map<String, Object> properties;

	public KernelSource(final String id, final Map<String, Object> properties) {
		this.id = id;
		if (properties != null) {
			this.properties = properties;
		} else {
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
		try {
			final String s = (String) properties.get(GLOBAL_SIZES);
			final String[] sizesStr = s.trim().split(" ");
			if (sizesStr.length <= 3) {
				final int[] globalSize = new int[] { 1, 1, 1 };
				for (int i = 0; i < sizesStr.length; i++) {
					globalSize[i] = Integer.parseInt(sizesStr[i]);
				}
				return globalSize;
			}
		} catch (final NullPointerException e) {
			return null;
		}
		return null;
	}

	@Override
	public int[] getLocalSize() {
		try {
			final String s = (String) properties.get(LOCAL_SIZES);
			final String[] sizesStr = s.trim().split(" ");
			if (sizesStr.length <= 3) {
				final int[] localSize = new int[] { 1, 1, 1 };
				for (int i = 0; i < sizesStr.length; i++) {
					localSize[i] = Integer.parseInt(sizesStr[i]);
				}
				return localSize;
			}
		} catch (final NullPointerException e) {
			return null;
		}
		return null;
	}

	@Override
	public int[] getOffset() {
		try {
			final String s = (String) properties.get(OFFSETS);
			final String[] sizesStr = s.trim().split(" ");
			if (sizesStr.length <= 3) {
				final int[] offset = new int[3];
				for (int i = 0; i < sizesStr.length; i++) {
					offset[i] = Integer.parseInt(sizesStr[i]);
				}
				return offset;
			}
		} catch (final NullPointerException e) {
			return null;
		}
		return null;
	}

	@Override
	public abstract List<ValidationResult> validate();

	@Override
	public int getOutputSize() {
		int outputSize = -1;
		try {
			final String outputSizeStr = (String) properties.get(OUTPUT_SIZE);
			outputSize = Integer.parseInt(outputSizeStr);
		} catch (final NumberFormatException e) {
			return -1;
		}
		return outputSize;
	}

	@Override
	public KernelRoleEnum getKernelRole() {
		final String kernelRoleString = (String) properties.get(KERNEL_ROLE);
		return KernelRoleEnum.getRoleByName(kernelRoleString);
	}

}
