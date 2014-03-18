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

import java.io.Serializable;

public class ValidationResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5866015140843352858L;

	public static enum ValidationResultType{
		VALID, INVALID
	};
	
	private final String messsage;
	private final ValidationResultType type;
	
	public ValidationResult(String message, ValidationResultType type){
		this.messsage = message;
		this.type = type;
	}

	public String getMesssage() {
		return messsage;
	}

	public ValidationResultType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((messsage == null) ? 0 : messsage.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidationResult other = (ValidationResult) obj;
		if (messsage == null) {
			if (other.messsage != null)
				return false;
		} else if (!messsage.equals(other.messsage))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
