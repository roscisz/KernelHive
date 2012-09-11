package pl.gda.pg.eti.kernelhive.repository.graph.node.util;

public class NodeNameGenerator {

	public static int i = 0;
	
	public static String generateName(){
		return "name"+(++i);
	}
}
