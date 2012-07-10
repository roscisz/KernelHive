package pl.gda.pg.eti.kernelhive.common.file;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileUtilsTest {
	
	static String filePathValid;
	static String filePathInvalid;
	static String dirPathValid;
	static String dirPathInvalid;
	static File createdDir;
	static File createdFile;
	static File testFile;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		createdDir = new File("./testdir/");
		boolean result = createdDir.mkdir();
		createdFile = new File("./test.file");
		result &= createdFile.createNewFile();
		if(!result){
			throw new Exception("cannot create test stub");
		}
		
		filePathValid = "./testvalid.file";
		dirPathValid = "./testvaliddir/";
		dirPathInvalid = "./testdir/";
		filePathInvalid = "./test.file";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		createdDir.delete();
		createdFile.delete();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		if(testFile!=null){
			testFile.delete();
		} 
	}

	@Test
	public void testCreateNewFileFileNotExists() {
		try {
			testFile = FileUtils.createNewFile(filePathValid);
			assertNotNull(testFile);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateNewFileFileExists(){
		try {
			testFile = FileUtils.createNewFile(filePathInvalid);
			assertNull(testFile);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateNewDirectoryDirectoryNotExists() {
		try{
			testFile = FileUtils.createNewDirectory(dirPathValid);
			assertNotNull(testFile);
		} catch(SecurityException e){
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCreateNewDirectoryDirectoryExists() {
		try{
			testFile = FileUtils.createNewDirectory(dirPathInvalid);
			assertNull(testFile);
		} catch(SecurityException e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTranslateRelativeToAbsolutePath(){
		String base = "/bin/bash";
		String relative = "../../etc";
		
		String absolutePath = FileUtils.translateRelativeToAbsolutePath(base, relative);
		assertEquals("/etc", absolutePath);
	}
	
	@Test
	public void testTranslateAbsoluteToRelativePath(){
		//TODO test!
	}

}
