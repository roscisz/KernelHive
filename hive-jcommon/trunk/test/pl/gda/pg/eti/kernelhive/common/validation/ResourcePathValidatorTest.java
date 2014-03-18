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
package pl.gda.pg.eti.kernelhive.common.validation;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

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
	
	@Test
	public void testValidateFileNotExistsValidPath(){
		ValidationResult vr = ResourcePathValidator.validateFileNotExists(filepathInvalid);
		assertEquals(ValidationResultType.VALID, vr.getType());
	}
	
	@Test
	public void testValidateFileNotExistsInvalidPath(){
		ValidationResult vr = ResourcePathValidator.validateFileNotExists(filepathValid);
		assertEquals(ValidationResultType.INVALID, vr.getType());
	}

}
