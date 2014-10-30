package org.socraticgrid.quick.fhir.profile;

public class ProfileGenerationMetadata {
	
	private String name;
	private String source;
	private String target;

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

	@Override
	public String toString() {
		return "ProfileGenerationMetadata [name=" + name + ", source=" + source
				+ ", target=" + target + "]";
	}
	
}
