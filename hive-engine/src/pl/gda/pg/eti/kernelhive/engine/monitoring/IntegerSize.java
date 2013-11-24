/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring;

/**
 *
 * @author szymon
 */
public enum IntegerSize {

	BYTE(1),
	SHORT_INT(2),
	INT(4);
	private int value;

	private IntegerSize(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
