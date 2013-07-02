package pl.gda.pg.eti.kernelhive.engine.interfaces;

import java.util.List;

public interface IKnapsackSolver {
	
	public boolean[] solve(List<IKnapsackItem> items, int capacity);

}
