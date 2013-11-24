/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine;

/**
 *
 * @author szymon
 */
public final class ClusterHelper {

	private static int lastClusterId = -1;

	private ClusterHelper() {
	}

	public static int getNextId() {
		lastClusterId++;
		return lastClusterId;
	}
}
