package org.socraticgrid.uml;

public class OneToOnePropertyMapping {
	
	private UmlProperty source;
	private UmlProperty destination;
	private String sourcePath;
	private String destinationPath;
	private String rationale;
	private TerminologyBinding binding;
	private String destinationProfileName;
	private boolean extension;
	
	public OneToOnePropertyMapping() {}

	public OneToOnePropertyMapping(UmlProperty source, UmlProperty destination) {
		this.source = source;
		this.destination = destination;
	}

	public UmlProperty getSource() {
		return source;
	}

	public void setSource(UmlProperty source) {
		this.source = source;
	}

	public UmlProperty getDestination() {
		return destination;
	}

	public void setDestination(UmlProperty destination) {
		this.destination = destination;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getDestinationPath() {
		return destinationPath;
	}

	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}
	
	public String getDestinationPathPrefix() {
		String prefix = null;
		if(destinationPath != null && destinationPath.indexOf('.') > 0) {
			prefix = destinationPath.substring(0, destinationPath.lastIndexOf('.'));
		}
		return prefix;
	}
	
	public String getRationale() {
		return rationale;
	}

	public void setRationale(String rationale) {
		this.rationale = rationale;
	}

	public boolean isMapped() {
		return destination != null;
	}
	
	public boolean isUnmapped() {
		return !isMapped();
	}

	public boolean isExtension() {
		return extension;
	}

	public void setExtension(boolean extension) {
		this.extension = extension;
	}
	
	public TerminologyBinding getBinding() {
		return binding;
	}

	public void setBinding(TerminologyBinding binding) {
		this.binding = binding;
	}
	
	public String getDestinationProfileName() {
		return destinationProfileName;
	}

	public void setDestinationProfileName(String destinationProfileName) {
		this.destinationProfileName = destinationProfileName;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(source).append("-->").append(destination).append("[Is extension: ").append(extension).append("]");
		return builder.toString();
	}
	
	public static class TerminologyBinding {
		private String name;
		private boolean isExtensible;
		private String conformance;
		private String valueSetUri; //TODO Change to URI later
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isExtensible() {
			return isExtensible;
		}
		public void setExtensible(boolean isExtensible) {
			this.isExtensible = isExtensible;
		}
		public String getConformance() {
			return conformance;
		}
		public void setConformance(String conformance) {
			this.conformance = conformance;
		}
		public String getValueSetUri() {
			return valueSetUri;
		}
		public void setValueSetUri(String valueSetUri) {
			this.valueSetUri = valueSetUri;
		}
		
		public String toString() {
			return conformance + "," + valueSetUri;
		}
	}
}
