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
package pl.gda.pg.eti.kernelhive.engine.http.file.utils;

import java.io.IOException;

public interface IHttpFileUploadClient {

	/**
	 * Sends HTTP POST Request to the file upload servlet via Apache HttpCLient
	 * library. It takes byte array, saves it to temp file and then sends
	 * appropriate post request.
	 * 
	 * @param uploadServletUrl
	 *            url of the file upload servlet
	 * @param fileName
	 *            file name
	 * @param bytes
	 *            output data to be saved
	 * 
	 * @throws IOException
	 *             thrown when there has been error in sending request or the
	 *             uploadServletUrl is malformed
	 */
	void postFileUpload(String uploadServletUrl, String fileName, byte[] bytes)
			throws IOException;

	/**
	 * Sends HTTP POST Request to the file upload servlet via Apache HttpCLient
	 * library. It takes byte array, saves it to temp file and then sends
	 * appropriate post request.
	 * 
	 * @param uploadServletUrl
	 *            url of the file upload servlet
	 * @param bytes
	 *            output data to be saved
	 * @return uploaded file name
	 * @throws IOException
	 */
	String postFileUpload(String uploadServletUrl, byte[] bytes)
			throws IOException;
}
