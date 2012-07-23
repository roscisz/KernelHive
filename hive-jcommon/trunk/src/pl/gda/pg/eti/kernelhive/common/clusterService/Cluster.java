package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.structure.HasID;
import pl.gda.pg.eti.kernelhive.common.structure.Unit;

public class Cluster extends HasID {
	
	public List<Unit> unitList = new ArrayList<Unit>();

}
