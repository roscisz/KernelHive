package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.List;

import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackItem;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackSolver;

public class KnapsackSolverStub implements IKnapsackSolver {

	/**
	 * Nevermind capacity, we schedule all
	 */
	@Override
	public boolean[] solve(List<IKnapsackItem> items, int capacity) {
		boolean[] ret = new boolean[items.size()];
		for(int i = 0; i != items.size(); i++)
			ret[i] = true;
		return ret;
	}

}
