package pl.gda.pg.eti.kernelhive.gui.project.util;

import java.util.UUID;

/**
 * simple wrapper class for UUID generation in String format 
 * @author mschally
 *
 */
public class NodeIdGenerator {

	/**
	 * generates UUID for node id
	 * @return node id
	 */
	public static String generateId(){
		return UUID.randomUUID().toString();
	}
}
