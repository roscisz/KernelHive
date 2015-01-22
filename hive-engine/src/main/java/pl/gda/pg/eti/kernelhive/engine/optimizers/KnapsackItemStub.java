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

import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackItem;

public class KnapsackItemStub implements IKnapsackItem {

	private int weight;
	private int value;
	private String name;
	
	public KnapsackItemStub(int weight, int value, String name) {
		this.weight = weight;
		this.value = value;
		this.name = name;
	}
	
	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public int getValue() {
		return value;		
	}
	
	public String getName() { 
		return name;
	}
	
	@Override
	public int compareTo(IKnapsackItem o) {
		Float myDensity = (float)this.getValue() / (float)this.getWeight();
		Float oDensity = (float)o.getValue() / (float)o.getWeight();
		return -myDensity.compareTo(oDensity);
		
		//Integer myValue = (Integer) value;
		//Integer oValue = (Integer) o.getValue();
		//return -myValue.compareTo(oValue);
		
		//Integer myWeight = (Integer) weight;
		//Integer oWeight = (Integer) o.getWeight();
		//return myWeight.compareTo(oWeight);
	}
}
