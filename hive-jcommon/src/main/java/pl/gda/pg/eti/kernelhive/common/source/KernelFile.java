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
package pl.gda.pg.eti.kernelhive.common.source;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class KernelFile extends KernelSource implements IKernelFile, Serializable {

	private static final long serialVersionUID = 192995314148788181L;
	protected File file;
	
	public KernelFile(File file, String id){
		super(id, null);
		this.file = file;
	}
	
	public KernelFile(File file, String id, Map<String, Object> properties){
		super(id, properties);
		this.file = file;
	}
	
	@Override
	public File getFile() {
		return file;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String toString(){
		return this.file.getAbsolutePath();
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public List<ValidationResult> validate() {
		List<ValidationResult> result = new ArrayList<ValidationResult>();
		if(getGlobalSize()==null){
			result.add(new ValidationResult("Kernel with id: "+id+" has no global size defined", ValidationResultType.INVALID));
		}
		if(getLocalSize()==null){
			result.add(new ValidationResult("Kernel with id: "+id+" has no local size defined", ValidationResultType.INVALID));
		}
		if(getOffset()==null){
			result.add(new ValidationResult("Kernel with id: "+id+" has no offset defined", ValidationResultType.INVALID));
		}
		if(getOutputSize()==-1){
			result.add(new ValidationResult("Kernel with id: "+id+" has no output size defined", ValidationResultType.INVALID));
		}
		if(!file.exists()||!file.canRead()||!file.isFile()){
			result.add(new ValidationResult("Kernel with id: "+id+" has no valid file attached", ValidationResultType.INVALID));
		}
		if(result.size()==0){//everything ok
			result.add(new ValidationResult("Kernel with id: "+id+" validated correctly", ValidationResultType.VALID));
		}
		return result;	
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
