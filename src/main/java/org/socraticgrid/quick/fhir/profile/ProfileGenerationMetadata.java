package org.socraticgrid.quick.fhir.profile;

public class ProfileGenerationMetadata {
	
	private String name;
	private String source;
	private String target;
	private boolean generateResourceDefinition;
	private String resourceName;

	public ProfileGenerationMetadata() {}
	
	public ProfileGenerationMetadata(String name, String source, String target) {
		super();
		this.name = name;
		this.source = source;
		this.target = target;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	public boolean isGenerateResourceDefinition() {
		return generateResourceDefinition;
	}

	public void setGenerateResourceDefinition(boolean generateResourceDefinition) {
		this.generateResourceDefinition = generateResourceDefinition;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@Override
	public String toString() {
		return "ProfileGenerationMetadata [name=" + name + ", source=" + source
				+ ", target=" + target + "]";
	}
	
}
