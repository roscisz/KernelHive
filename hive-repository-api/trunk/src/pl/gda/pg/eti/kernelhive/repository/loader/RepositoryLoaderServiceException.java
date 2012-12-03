package pl.gda.pg.eti.kernelhive.repository.loader;

public class RepositoryLoaderServiceException extends Exception {

	private static final long serialVersionUID = -5418318485910911913L;

	public RepositoryLoaderServiceException() {
		super();
	}

	public RepositoryLoaderServiceException(final String message) {
		super(message);
	}

	public RepositoryLoaderServiceException(final Throwable t) {
		super(t);
	}

	public RepositoryLoaderServiceException(final String message,
			final Throwable t) {
		super(message, t);
	}
}
