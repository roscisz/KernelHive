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
	 * @throws IOException
	 * @throws SecurityException
	 */
	public static File createNewFile(String filePath) throws IOException, SecurityException{
		File file = null;
		try {
			file = new File(filePath);
			if(file.exists()){
				return null;
			}
			String dirpath = file.getParent();
			File dir = new File(dirpath);
			if(!dir.exists()){
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
	 * 
	 * @param dirPath
	 * @return
	 * @throws SecurityException
	 */
	public static File createNewDirectory(String dirPath) throws SecurityException{
		File file = new File(dirPath);
		if(file.exists()){
			return null;
		} else{
			file.mkdirs();
			return file;
		}
	}
}