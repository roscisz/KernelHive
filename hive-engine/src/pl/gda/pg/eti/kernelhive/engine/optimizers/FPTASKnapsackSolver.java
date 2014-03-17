package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackItem;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackSolver;

public class FPTASKnapsackSolver implements IKnapsackSolver {
	
	private int scalingFactor;
	
	public FPTASKnapsackSolver(int scalingFactor) {
		this.scalingFactor = scalingFactor;		
	}

	@Override
	public boolean[] solve(List<IKnapsackItem> items, int capacity) {	
		int normalizedCapacity = capacity / this.scalingFactor;	
		List<IKnapsackItem> normalizedItems = normalizeItems(items, capacity);		
		int[][] dynamicTable = dynamicAlgorithm(normalizedItems, normalizedCapacity);
		/*for(int i = 0; i != dynamicTable.length; i++) {
			for(int j = 0; j != dynamicTable[i].length; j++)
				System.out.print(dynamicTable[i][j] + " ");
			System.out.println();
		}*/
		return backtrack(dynamicTable, normalizedCapacity, normalizedItems);		
	}

	private List<IKnapsackItem> normalizeItems(List<IKnapsackItem> items, int capacity) {
		List<IKnapsackItem> normalizedItems = new ArrayList<IKnapsackItem>();
		for(IKnapsackItem item : items)
			normalizedItems.add(new KnapsackItemStub(item.getWeight() / scalingFactor, item.getValue(), ""));
		return normalizedItems;
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
	
	private boolean[] backtrack(int[][] dynamicTable, int remainingCapacity, List<IKnapsackItem> items) {
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
