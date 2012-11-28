package pl.gda.pg.eti.kernelhive.common.kernel.repository.impl;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.repository.kernel.repository.IKernelRepository;

public class KernelRepositoryTest {

	private IKernelRepository repo;

	@Before
	public void setUp() throws Exception {
		// repo = new
		// KernelRepository(KernelRepositoryTest.class.getResource("/kernels/repository.xml"));
	}

	@Test
	public void testGetEntryForGraphNodeType() throws ConfigurationException {
		// KernelRepositoryEntry entry =
		// repo.getEntryForGraphNodeType(GraphNodeType.GENERIC);
		// assertNotNull(entry);
	}

	@Test
	public void testGetEntries() throws ConfigurationException {
		// assertNotNull(repo.getEntries());
	}

}
