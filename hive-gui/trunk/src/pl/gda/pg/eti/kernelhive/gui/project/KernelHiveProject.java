package pl.gda.pg.eti.kernelhive.gui.project;

import java.io.Serializable;

public class KernelHiveProject implements Serializable {

	private static final long serialVersionUID = -4797108604024696381L;

	private String name;
	
	public KernelHiveProject(){
		
	}
	
	private void updateProjectName(String name){
		
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
		updateProjectName(name);
	}
}
