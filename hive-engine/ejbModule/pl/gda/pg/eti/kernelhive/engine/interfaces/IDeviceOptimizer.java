package pl.gda.pg.eti.kernelhive.engine.interfaces;

import java.util.Collection;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;

public interface IDeviceOptimizer {
	
	public List<Device> arrangeDevices(Collection<Cluster> infrastructure);

}
