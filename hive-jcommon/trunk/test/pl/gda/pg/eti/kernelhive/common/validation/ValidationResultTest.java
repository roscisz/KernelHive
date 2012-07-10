package pl.gda.pg.eti.kernelhive.common.validation;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

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
