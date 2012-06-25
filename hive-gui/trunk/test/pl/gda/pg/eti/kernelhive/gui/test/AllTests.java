package pl.gda.pg.eti.kernelhive.gui.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ResourcePathValidatorTest.class, 
	FileUtilsTest.class, 
	GraphValidatorTest.class})
public class AllTests {
	
	public AllTests(){	}

}