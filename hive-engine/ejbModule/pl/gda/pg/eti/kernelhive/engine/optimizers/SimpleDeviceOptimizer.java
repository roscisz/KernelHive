package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IDeviceOptimizer;

public class SimpleDeviceOptimizer implements IDeviceOptimizer {

	@Override
	public List<Device> arrangeDevices(Collection<Cluster> infrastructure) {
		List<Device> ret = new ArrayList<Device>();
		
		for(Cluster cluster : infrastructure)
			for(Unit unit : cluster.unitList)
				for(Device device : unit.devices)
					ret.add(device);
		
		return ret;
	}

}
