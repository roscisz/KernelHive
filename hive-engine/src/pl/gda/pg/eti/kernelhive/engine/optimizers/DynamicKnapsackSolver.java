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

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackItem;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackSolver;

public class DynamicKnapsackSolver implements IKnapsackSolver {
	
	@Override
	public boolean[] solve(List<IKnapsackItem> items, int capacity) {
		int[][] dynamicTable = dynamicAlgorithm(items, capacity);
		return backtrack(dynamicTable, items, capacity);		
	}
	
	private int[][] dynamicAlgorithm(List<IKnapsackItem> normalizedItems, int normalizedCapacity) {
		int[][] table = new int[normalizedItems.size() + 1][normalizedCapacity + 1];
		
		for(int w = 0; w <= normalizedCapacity; w++)
			table[0][w] = 0;
		
		for(int i = 1; i <= normalizedItems.size(); i++)
			table[i][0] = 0;
		
		for(int i = 1, index = 0; i <= normalizedItems.size(); i++, index++) {
			for(int w = 0; w <= normalizedCapacity; w++) {
				int currentWeight = normalizedItems.get(index).getWeight();
				int currentValue = normalizedItems.get(index).getValue();
				if(currentWeight <= w) {
					if(currentValue + table[i - 1][w - currentWeight] > table[i-1][w])
						table[i][w] = currentValue + table[i - 1][w - currentWeight];  
					else 
						table[i][w] = table[i - 1][w];										
				} else { 
					table[i][w] = table[i - 1][w];
				}
			}			
		}

		return table;
	}
	
	private boolean[] backtrack(int[][] dynamicTable, List<IKnapsackItem> items, int remainingCapacity) {
		boolean[] ret = new boolean[items.size()];
		for(int k = dynamicTable.length - 1; k >= 1; k--) {
			if(dynamicTable[k][remainingCapacity] != dynamicTable[k - 1][remainingCapacity]) {
				remainingCapacity -= items.get(k - 1).getWeight();
				ret[k - 1] = true;
			}
		}
		return ret;
	}
}
