package pl.gda.pg.eti.kernelhive.common.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Util Class for file operations
 *
 * @author mschally
 *
 */
public class FileUtils {

	private static final Logger LOG = Logger.getLogger(FileUtils.class
			.getName());

	/**
	 * creates new file
	 *
	 * @param filePath - File path
	 * @return File if success, null if failure
	 * @throws IOException
	 * @throws SecurityException
	 */
	public static File createNewFile(String filePath) throws IOException,
			SecurityException {
		File file = null;
		try {
			file = new File(filePath);
			if (file.exists()) {
				return null;
			}
			String dirpath = file.getParent();
			File dir = new File(dirpath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			file.createNewFile();
		} catch (IOException e) {
			LOG.severe("Error creating new file!");
			e.printStackTrace();
			throw e;
		}
		return file;
	}

	/**
	 * creates new directory
	 *
	 * @param dirPath
	 * @return file handle to the directory
	 * @throws SecurityException
	 */
	public static File createNewDirectory(String dirPath)
			throws SecurityException {
		File file = new File(dirPath);
		if (file.exists()) {
			return null;
		} else {
			file.mkdirs();
			return file;
		}
	}

	/**
	 *
	 * @param basePath
	 * @param pathToTranslate
	 * @return translated absolute path. Null if a translation could not be made
	 */
	public static String translateRelativeToAbsolutePath(String basePath,
			String pathToTranslate) {
		return Paths.get(basePath, pathToTranslate).toString();
	}

	/**
	 *
	 * @param basePath
	 * @param pathToTranslate
	 * @return
	 */
	public static String translateAbsoluteToRelativePath(String basePath,
			String pathToTranslate) {
		return Paths.get(basePath).relativize(Paths.get(pathToTranslate)).toString();
	}

	public static String readFileToString(File file) throws IOException {
		BufferedReader br = null;
		InputStream is = null;
		InputStreamReader isr = null;
		try {
			is = new FileInputStream(file);
			isr = new InputStreamReader(is, "utf8");
			br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String buffer;
			while ((buffer = br.readLine()) != null) {
				sb.append(buffer + "\n");
			}
			return sb.toString();
		} catch (SecurityException e) {
			throw new IOException(e);
		} catch (UnsupportedEncodingException e) {
			throw new IOException(e);
		} finally {
			is.close();
		}
	}
}