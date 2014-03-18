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

import org.junit.Before;
import org.junit.Test;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class ValidationResultTest {

	ValidationResult result;
	String message;
	ValidationResultType type;
	
	@Before
	public void setUp() throws Exception {
		message = "test";
		type = ValidationResultType.VALID;
		result = new ValidationResult(message, type);
	}

	@Test
	public void testGetMesssage() {
		assertEquals(message, result.getMesssage());
	}

	@Test
	public void testGetType() {
		assertEquals(type, result.getType());
	}

}
