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
package pl.gda.pg.eti.kernelhive.common.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
		String basePath = "/etc";
		String pathToTranslate = "/etc/init.d";
		String result = FileUtils.translateAbsoluteToRelativePath(basePath, pathToTranslate);
		assertEquals("init.d", result);
	}
}
