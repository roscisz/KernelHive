package pl.gda.pg.eti.kernelhive.gui.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.gui.validator.ValidationResult.ValidationResultType;

public class ValidationResultTest {

	ValidationResult result;
	String message;
	ValidationResultType type;
	
	@Before
	public void setUp() throws Exception {
		message = "test";
		type = ValidationResultType.VALID;
		result = new ValidationResult(message, type);
	}

	@Test
	public void testGetMesssage() {
		assertEquals(message, result.getMesssage());
	}

	@Test
	public void testGetType() {
		assertEquals(type, result.getType());
	}

}
