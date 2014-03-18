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

import java.util.Collections;
import java.util.List;

import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackItem;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackSolver;

public class GreedyKnapsackSolver implements IKnapsackSolver {

	@Override
	public boolean[] solve(List<IKnapsackItem> items, int capacity) {
		boolean[] ret = new boolean[items.size()];
		
		/*System.out.println("Greedy items before sort:");
		for(IKnapsackItem i : items)
			System.out.println(i.getWeight() +", "+ i.getValue() + " ( " + (float)i.getValue()/(float)i.getWeight()+ ")");
			*/
		Collections.sort(items);
		/*System.out.println("Greedy items after sort:");
		for(IKnapsackItem i : items) 
			System.out.println(i.getWeight() +", "+ i.getValue() + " ( " + (float)i.getValue()/(float)i.getWeight()+ ")");
			*/		
			
		
		int usedWeight = 0;
		for(int i = 0; i != items.size(); i++) {
			if(usedWeight + items.get(i).getWeight() <= capacity) {
				usedWeight += items.get(i).getWeight();
				ret[i] = true;				
			}				
		}
		return ret;		
	}

}
