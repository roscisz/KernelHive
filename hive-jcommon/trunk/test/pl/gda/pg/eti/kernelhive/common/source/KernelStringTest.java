package pl.gda.pg.eti.kernelhive.common.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
		ks.getProperties().put("globalSizes", "1 0 0");
		ks.getProperties().put("localSizes", "1 0 0");
		ks.getProperties().put("offsets", "0 0 0");
	}

	@Test
	public void testGetKernel() {
		assertEquals(kernel, ks.getKernel());
	}

	@Test
	public void testGetId() {
		assertEquals(id, ks.getId());
	}
	
	@Test
	public void testGetGlobalSize(){
		assertNotNull(ks.getGlobalSize());
	}
	
	@Test
	public void testGetLocalSize(){
		assertNotNull(ks.getLocalSize());
	}
	
	@Test
	public void testGetOffset(){
		assertNotNull(ks.getOffset());
	}

}
