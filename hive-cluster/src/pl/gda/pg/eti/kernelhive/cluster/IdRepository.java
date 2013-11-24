/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.cluster;

/**
 *
 * @author szymon
 */
public final class IdRepository {

	private IdRepository() {
	}
	private static int id = 1;

	public static int getId() {
		return id++;
	}
}
