package org.socraticgrid.uml;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents a simple UML package. UML packages
 * can be used to group related classes.
 * 
 * @author cnanjo
 *
 */
public class UmlPackage extends UmlComponent implements Identifiable {
	
	private List<UmlPackage> packages = new ArrayList<UmlPackage>();
	private List<UmlClass> classes = new ArrayList<UmlClass>();

	public UmlPackage(String name) {
		setName(name);
	}
	
	public void addPackage(UmlPackage umlPackage) {
		packages.add(umlPackage);
	}
	
	public List<UmlPackage> getPackages() {
		return packages;
	}
	
	public void addClass(UmlClass umlClass) {
		classes.add(umlClass);
	}
	
	public List<UmlClass> getClasses() {
		return classes;
	}
	
	public void initializePackage(UmlModel model) {
		for(UmlClass umlClass : getClasses()) {
			umlClass.findClassForId(model.getIdMap());
		}
		for(UmlPackage umlPackage : packages) {
			umlPackage.initializePackage(model);
		}
	}

	public String toString() {
		return "Package(" + getName() + ") " + packages + "," + classes;
	}
}
