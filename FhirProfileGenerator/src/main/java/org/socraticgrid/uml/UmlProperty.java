package org.socraticgrid.uml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a UML class attribute/property.
 * Typically, a property has a name, a type, and a cardinality.
 * 
 * @author cnanjo
 *
 */
public class UmlProperty extends UmlComponent implements Cloneable {

	private String documentation;
	private CardinalityRange cardinality;
	private List<UmlClass> types;
	private String typeId;
	private UmlClass source;
	private List<UmlProperty> mappings;
	private Map<String,String> aliases;
	private Map<String, CardinalityRange> cardinalityConstraints;
	
	public UmlProperty() {
		cardinality = new CardinalityRange();
		mappings = new ArrayList<UmlProperty>();
		aliases = new HashMap<String,String>();
		cardinalityConstraints = new HashMap<String,CardinalityRange>();
		types = new ArrayList<UmlClass>();
	}
	
	public UmlProperty(String name) {
		this();
		setName(name);
	}
	
	public UmlProperty(String name, Integer low, Integer high) {
		this(name);
		this.cardinality.setLow(low);
		this.cardinality.setHigh(high);
	}
	
	public Integer getLow() {
		return cardinality.getLow();
	}

	public void setLow(Integer low) {
		this.cardinality.setLow(low);
	}

	public Integer getHigh() {
		return cardinality.getHigh();
	}

	public void setHigh(Integer high) {
		this.cardinality.setHigh(high);
	}
	
	private void setCardinality(CardinalityRange range) {
		this.cardinality = range;
	}
	
	public List<UmlClass> getTypes() {
		return types;
	}

	public void setTypes(List<UmlClass> types) {
		this.types = types;
	}
	
	public UmlClass getFirstType() {
		UmlClass type = null;
		if(types != null && types.size() > 0) {
			type = types.get(0);
		}
		return type;
	}
	
	public void addType(UmlClass type) {
		types.add(type);
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	
	public UmlClass getSource() {
		return source;
	}

	public void setSource(UmlClass source) {
		this.source = source;
	}
	
	public List<UmlProperty> getMappings() {
		return mappings;
	}

	public void setMappings(List<UmlProperty> mappings) {
		this.mappings = mappings;
	}
	
	public void addMapping(UmlProperty mapping) {
		this.mappings.add(mapping);
	}
	
	public Map<String,String> getAliases() {
		return aliases;
	}
	
	public void setAliases(Map<String,String> aliases) {
		this.aliases = aliases;
	}
	
	public void addAliasForScope(String scope, String alias) {
		aliases.put(scope, alias);
	}
	
	public void removeAliasForScope(String scope) {
		aliases.remove(scope);
	}
	
	public String getAliasForScope(String scope) {
		return aliases.get(scope);
	}
	
	public void clearAliases() {
		aliases.clear();
	}
	
	public String getNameForScope(String scope) {
		String alias = getAliasForScope(scope);
		if(alias != null) {
			return alias;
		} else {
			return getName();
		}
	}
	
	public Map<String, CardinalityRange> getCardinalityConstraints() {
		return cardinalityConstraints;
	}
	
	public void setCardinalityConstraints(HashMap<String,CardinalityRange> cardinalityConstraints) {
		this.cardinalityConstraints = cardinalityConstraints;
	}
	
	public void addCardinalityConstraint(String scope, CardinalityRange cardinalityConstraint) {
		validate(cardinalityConstraint);
		cardinalityConstraints.put(scope, cardinalityConstraint);
	}
	
	public void removeCardinalityConstraintForScope(String scope) {
		cardinalityConstraints.remove(scope);
	}
	
	public void clearCardinalityConstraints() {
		cardinalityConstraints.clear();
	}
	
	public Integer getAdjustedLow(String scope) {
		CardinalityRange constraint = cardinalityConstraints.get(scope);
		Integer adjustedLow = getLow();
		if(constraint != null) {
			if(constraint.getLow() != null) {
				adjustedLow = constraint.getLow();
			}
		}
		return adjustedLow;
	}
	
	public Integer getAdjustedHigh(String scope) {
		CardinalityRange constraint = cardinalityConstraints.get(scope);
		Integer adjustedHigh = getHigh();
		if(constraint != null) {
			if(constraint.getHigh() != null) {
				adjustedHigh = constraint.getHigh();
			}
		}
		return adjustedHigh;
	}
	
	public void validate(CardinalityRange cardinalityConstraint) {
		this.cardinality.validate(cardinalityConstraint);
	}

	public UmlProperty clone() {
		UmlProperty clone = (UmlProperty)super.clone();
		clone.setName(this.getName());
		clone.setId(this.getId());
		clone.setLow(this.getLow());
		clone.setHigh(this.getHigh());
		clone.setDocumentation(this.getDocumentation());
		clone.setTypes(new ArrayList<UmlClass>());
		clone.getTypes().addAll(this.getTypes());
		clone.setSource(this.getSource());
		clone.setMappings(new ArrayList<UmlProperty>());
		clone.getMappings().addAll(this.getMappings());
		clone.setAliases(new HashMap<String,String>());
		clone.getAliases().putAll(this.getAliases());
		clone.setCardinality(this.cardinality.clone());
		clone.setCardinalityConstraints(new HashMap<String, CardinalityRange>());
		clone.getCardinalityConstraints().putAll(this.getCardinalityConstraints());
		return clone;
	}
	
	public void findTypeClassForTypeId(Map<String, Identifiable> classIdToClassMap) {
		UmlClass typeClass = (UmlClass)classIdToClassMap.get(getTypeId());
		if(typeClass != null) {
			addType(typeClass);
		} else {
			throw new RuntimeException("Unknown class ID: " + getTypeId());
		}
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if(getSource()!= null) {
			builder.append(getSource().getName());
		} else {
			builder.append("UNKNOWN_SOURCE");
		}
		builder.append(".").append(getName());
		if(cardinality != null) {
			builder.append("(").append(cardinality.getLow()).append(",").append(cardinality.getHigh()).append(")");
		}
		return builder.toString();
	}
}
