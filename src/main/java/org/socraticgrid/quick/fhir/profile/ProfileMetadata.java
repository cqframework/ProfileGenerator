package org.socraticgrid.quick.fhir.profile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProfileMetadata {
	
	private Map<String,String> sharedProfileProperties;
	private Map<String,Map<String,String>> profileSpecificConfigurations;

	public ProfileMetadata() {
		sharedProfileProperties = new HashMap<String,String>();
		profileSpecificConfigurations = new HashMap<String, Map<String,String>>();
	}
	
	public void setSharedProperties(Map<String,String> sharedProfileProperties) {
		this.sharedProfileProperties = sharedProfileProperties;
	}
	
	public void setSharedProperty(String property, String value) {
		sharedProfileProperties.put(property, value);
	}
	
	public String getSharedProperty(String property) {
		return sharedProfileProperties.get(property);
	}
	
	public Set<String> getSharedProperties() {
		return sharedProfileProperties.keySet();
	}
	
	public void setProfileSpecificProperties(String profilePath, Map<String,String> profileSpecificProperties) {
		this.profileSpecificConfigurations.put(profilePath, profileSpecificProperties);
	}
	
	public Map<String,String> getProfileSpecificProperties(String profilePath) {
		return profileSpecificConfigurations.get(profilePath);
	}
	
	public void setPropertyForProfile(String profilePath, String property, String value) {
		Map<String,String> profileConfiguration = profileSpecificConfigurations.get(profilePath);
		if(profileConfiguration == null) {
			profileConfiguration = new HashMap<String,String>();
			profileSpecificConfigurations.put(profilePath, profileConfiguration);
		}
		profileConfiguration.put(property, value);
	}
	
	public String getPropertyForProfile(String profilePath, String property) {
		Map<String,String> profileConfig = profileSpecificConfigurations.get(profilePath);
		if(profileConfig == null) {
			return null;
		} else {
			return profileConfig.get(property);
		}
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(sharedProfileProperties);
		buffer.append("\n");
		buffer.append(profileSpecificConfigurations);
		return buffer.toString();
	}
}
