package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.List;

import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackItem;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackSolver;

public class DynamicKnapsackSolver implements IKnapsackSolver {

	@Override
	public boolean[] solve(List<IKnapsackItem> items, int capacity) {
		return new boolean[2];
	}

}
