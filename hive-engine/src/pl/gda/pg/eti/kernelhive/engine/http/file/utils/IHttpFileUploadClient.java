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
}
