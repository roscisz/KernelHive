package pl.gda.pg.eti.kernelhive.gui.file;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Util Class for file operations
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
	 * creates new directory
	 * @param dirPath
	 * @return file handle to the directory
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
	
	/**
	 * 
	 * @param basePath
	 * @param pathToTranslate
	 * @return translated absolute path. Null if a translation could not be made
	 */
	public static String translateRelativeToAbsolutePath(String basePath, String pathToTranslate){
		File f = new File(basePath);
		String[] tokens = pathToTranslate.split(System.getProperty("file.separator"));
		
		for(String t : tokens){
			if(t.equalsIgnoreCase("..")){
				f = f.getParentFile();
			} else if(t.equalsIgnoreCase(".")){
				//do nothing
			} else if(t.equalsIgnoreCase("")){
				//do nothing
			} else {
				f = new File(f.getAbsolutePath()+System.getProperty("file.separator")+t);
				if(!f.exists()){
					return null;
				}
			}
		}		
		return f.getAbsolutePath();
	}
	
	public static String translateAbsoluteToRelativePath(String basePath, String pathToTranslate){
		File baseFile = new File(basePath);
		File file = new File(pathToTranslate);
		return baseFile.toURI().relativize(file.toURI()).toString();
	}
}