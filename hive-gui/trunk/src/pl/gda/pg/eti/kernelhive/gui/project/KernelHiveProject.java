package pl.gda.pg.eti.kernelhive.gui.project;

import java.io.File;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class KernelHiveProject implements Serializable {

	private static final long serialVersionUID = -4797108604024696381L;

	private String name;
	
	private File projectFile;
	private Set<File> files = new TreeSet<File>();
	
	public KernelHiveProject(File projectFile){
		this.projectFile = projectFile;
	}
	
	private void updateProjectName(String name){
		//TODO update project name in catalog structure and .project file
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
		updateProjectName(name);
	}
	
	public Set<File> getFiles(){
		return files;
	}
	
	public void setFiles(Set<File> files){
		this.files = files;
	}
	
	public boolean addFile(File f){
		if(!files.contains(f)){
			return files.add(f);
		} else{
			return false;
		}
	}
	
	public boolean removeFile(File f, boolean removeFromDisc){
		boolean result = false;
		if(files.contains(f)){
			result = files.remove(f);
			if(removeFromDisc){
				result &= f.delete();
			}
		}
		return result;
	}
}
