package pl.gda.pg.eti.kernelhive.gui.file.io;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Class for File IO operations
 * @author mschally
 *
 */
public class FileUtils {

	private static final Logger LOG = Logger.getLogger(FileUtils.class.getName());
	
	/**
	 * creates new file 
	 * @param filePath - File path
	 * @return File if success, null if failure
	 */
	public static File createNewFile(String filePath) throws IOException{
		File file = null;
		try {
			String dirpath = filePath.substring(0, filePath.lastIndexOf(System.getProperty("file.separator")));
			File dir = new File(dirpath);
			if(!dir.exists()){
				dir.mkdirs();
			}			
			file = new File(filePath);
			file.createNewFile();
		} catch (IOException e) {
			LOG.severe("Error creating new file!");
			e.printStackTrace();
			throw e;
		} 
		return file;
	}
	
	public static File createNewDirectory(String dirPath) throws IOException{
		File file = new File(dirPath);
		file.mkdirs();
		return file;
	}
}
