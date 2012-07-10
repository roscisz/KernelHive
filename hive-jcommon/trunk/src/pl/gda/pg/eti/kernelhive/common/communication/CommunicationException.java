package pl.gda.pg.eti.kernelhive.common.communication;

public class CommunicationException extends Exception {

	public CommunicationException(Exception e) {
		initCause(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5915676265511646148L;

}
