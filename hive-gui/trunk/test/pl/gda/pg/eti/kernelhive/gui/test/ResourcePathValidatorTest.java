package pl.gda.pg.eti.kernelhive.gui.test;

import static org.junit.Assert.*;

import java.io.File;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.gui.validator.ResourcePathValidator;
import pl.gda.pg.eti.kernelhive.gui.validator.ValidationResult;
import pl.gda.pg.eti.kernelhive.gui.validator.ValidationResult.ValidationResultType;

public class ResourcePathValidatorTest {
	
	private static String filepath = "./test.file";
	private static String dirpath = "./testdir/";
	
	private String filepathValid;
	private String dirpathValid;
	
	private String filepathInvalid;
	private String dirpathInvalid;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File f = new File(filepath);
		boolean isCreated = f.createNewFile();
		if(!isCreated){
			throw new Exception("no file created");
		}
		File d = new File(dirpath);
		isCreated = d.mkdir();
		if(!isCreated){
			throw new Exception("no directory created");
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File f = new File(filepath);
		boolean isDeleted = f.delete();
		if(!isDeleted){
			throw new Exception("file not deleted");
		}
		File d = new File(dirpath);
		isDeleted = d.delete();
		if(!isDeleted){
			throw new Exception("directory not deleted");
		}
	}

	@Before
	public void setUp() throws Exception {
		filepathValid = filepath;
		filepathInvalid = "./invalid.file";
		
		dirpathValid = dirpath;
		dirpathInvalid = "./invaliddir/";
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testValidateDirectoryValidPath() {
		ValidationResult vr = ResourcePathValidator.validateDirectory(dirpathValid);
		assertEquals(ValidationResultType.VALID, vr.getType());
	}

	@Test
	public void testValidateFileValidPath() {
		ValidationResult vr = ResourcePathValidator.validateFile(filepathValid);
		assertEquals(ValidationResultType.VALID, vr.getType());
	}
	
	@Test
	public void testValidateDirectoryInvalidPath(){
		ValidationResult vr = ResourcePathValidator.validateDirectory(dirpathInvalid);
		assertEquals(ValidationResultType.INVALID, vr.getType());
	}
	
	@Test
	public void testValidateFileInvalidPath(){
		ValidationResult vr = ResourcePathValidator.validateFile(filepathInvalid);
		assertEquals(ValidationResultType.INVALID, vr.getType());
	}

}
