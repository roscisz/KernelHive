/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
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
