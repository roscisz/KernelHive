package pl.gda.pg.eti.kernelhive.gui.validator;

import java.io.File;

import pl.gda.pg.eti.kernelhive.gui.validator.ValidationResult.ValidationResultType;

/**
 * Validates the given resource path
 * @author mschally
 *
 */
public class ResourcePathValidator {

	public static ValidationResult validateDirectory(String dirPath) {
		ValidationResult vr = null;
		File f = new File(dirPath);
		if (f.exists() && f.isDirectory()) {
			vr = new ValidationResult("OK", ValidationResultType.VALID);
		} else {
			vr = new ValidationResult("NOT OK", ValidationResultType.INVALID);
		}

		return vr;
	}
	
	public static ValidationResult validateFile(String filePath){
		ValidationResult vr = null;
		File f = new File(filePath);
		if(f.exists() && f.isFile()){
			vr = new ValidationResult("OK", ValidationResultType.VALID);
		} else {
			vr = new ValidationResult("NOT OK", ValidationResultType.INVALID);
		}
		return vr;
	}

}
