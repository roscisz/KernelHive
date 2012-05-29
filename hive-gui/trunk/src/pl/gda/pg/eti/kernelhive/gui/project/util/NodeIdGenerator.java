package pl.gda.pg.eti.kernelhive.gui.project.util;

import java.util.UUID;

public class NodeIdGenerator {

	public static String generateId(){
		return UUID.randomUUID().toString();
	}
}
