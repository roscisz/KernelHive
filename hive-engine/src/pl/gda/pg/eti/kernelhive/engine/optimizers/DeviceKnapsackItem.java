/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
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
