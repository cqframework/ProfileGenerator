package org.socraticgrid.uml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a UML Class
 * 
 * @author cnanjo
 *
 */
public class UmlClass extends UmlComponent implements Identifiable, Cloneable {

	private String description;
	private List<UmlClass> generalizations = new ArrayList<UmlClass>();
	private List<String> generalizationIds = new ArrayList<String>();
	private List<UmlProperty> properties = new ArrayList<UmlProperty>();
	private UmlModel model;
	
	public UmlClass() {}
	
	public UmlClass(String name) {
		this();
		setName(name);
	}

	public List<UmlProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<UmlProperty> attributes) {
		this.properties = attributes;
	}

	public void addProperty(UmlProperty attribute) {
		properties.add(attribute);
	}
	
	public List<UmlClass> getGeneralizations() {
		return generalizations;
	}

	public void setGeneralizations(List<UmlClass> generalizations) {
		this.generalizations = generalizations;
	}

	public void addGeneralization(UmlClass generalization) {
		generalizations.add(generalization);
	}
	
	public List<String> getGeneralizationIds() {
		return generalizationIds;
	}

	public void setGeneralizationIds(List<String> generalizationIds) {
		this.generalizationIds = generalizationIds;
	}

	public void addGeneralizationId(String generalizationId) {
		generalizationIds.add(generalizationId);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public UmlModel getModel() {
		return model;
	}

	public void setModel(UmlModel model) {
		this.model = model;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UML Classname: ").append(getName());
		
		if(properties.size() > 0) {
			builder.append("\n").append("Properties:");
			for(UmlProperty property : properties) {
				builder.append("\n\t").append(property);
			}
		}
		
		if(getTags().size() > 0) {
			builder.append("\n").append("Tags:");
			for(TaggedValue tag : getTags()) {
				builder.append("\n\t").append(tag);
			}
		}
		
		if(generalizations.size() > 0) {
			builder.append("\n").append("Parents:");
			for(UmlClass parent : generalizations) {
				builder.append("\n\t").append(parent.getName());
			}
		}
		builder.append("\n");
		return builder.toString();
	}
	
	public void findClassForId(Map<String, Identifiable> idToClassMap) {
		for(String id: generalizationIds) {
			UmlClass umlClass = (UmlClass)idToClassMap.get(id);
			if(umlClass != null) {
				generalizations.add(umlClass);
			} else {
				throw new RuntimeException("Class not found for ID: " + id);
			}
		}
		
		for(UmlProperty property : properties) {
			property.findTypeClassForTypeId(idToClassMap);
		}
	}
	
	public UmlClass clone() {
		UmlClass clone = (UmlClass)super.clone();
		clone.setDescription(this.description);
		clone.setGeneralizations(new ArrayList<UmlClass>());
		clone.getGeneralizations().addAll(this.generalizations);
		clone.setGeneralizationIds(new ArrayList<String>());
		clone.getGeneralizationIds().addAll(this.getGeneralizationIds());
		clone.setProperties(new ArrayList<UmlProperty>());
		clone.getProperties().addAll(this.getProperties());
		return clone;
	}
}
