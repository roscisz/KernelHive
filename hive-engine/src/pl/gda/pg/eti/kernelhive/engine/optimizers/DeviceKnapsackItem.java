package pl.gda.pg.eti.kernelhive.engine.optimizers;

import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackItem;

public class DeviceKnapsackItem implements IKnapsackItem {
	
	public Device device;
	
	public DeviceKnapsackItem(Device device) {
		this.device = device;
	}

	@Override
	public int getWeight() {
		if(device.name.matches(".*Tesla.*")) return 150;
		else if(device.name.matches(".*Quadr.*")) return 60;
		else if(device.name.matches(".*480.*")) return 175;
		else if(device.name.matches(".*560.*")) return 165;
		else if(device.name.matches(".*GTS.*")) return 100;
		else if(device.name.matches(".*Intel.*")) return 75;
		else return 31337;
	}

	@Override
	public int getValue() {
		if(device.name.matches(".*Tesla.*")) return 1677;
		else if(device.name.matches(".*Quadr.*")) return 503;
		else if(device.name.matches(".*480.*")) return 4545;
		else if(device.name.matches(".*560.*")) return 2272;
		else if(device.name.matches(".*GTS.*")) return 3623;
		else if(device.name.matches(".*Intel.*")) return 86;
		else return 0;
	}

	@Override
	public int compareTo(IKnapsackItem o) {
		Float myDensity = (float)this.getValue() / (float)this.getWeight();
		Float oDensity = (float)o.getValue() / (float)o.getWeight();
		return -myDensity.compareTo(oDensity);	
	}

}
