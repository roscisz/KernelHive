package pl.gda.pg.eti.kernelhive.common.source;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class KernelStringTest {
	
	private KernelString ks;
	private String kernel;
	private String id;

	@Before
	public void setUp() throws Exception {
		id = "test";
		kernel = "test";
		ks = new KernelString(id, kernel);
	}

	@Test
	public void testGetKernel() {
		assertEquals(kernel, ks.getKernel());
	}

	@Test
	public void testGetId() {
		assertEquals(id, ks.getId());
	}

}
