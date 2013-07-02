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
