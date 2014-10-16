package org.socraticgrid.quick.fhir.profile

import org.socraticgrid.core.xmlreader.BaseReader

/**
 * <profileRootPath>a</profileRootPath>
	    <profileBaseVersion>a</profileBaseVersion>
	    <profilePublisher>a</profilePublisher>
	    <profileEmailContact>a</profileEmailContact>
	    <profileFhirVersion>a</profileFhirVersion>
 * @author cnanjo
 *
 */
public class ProfileConfigurationLoader extends BaseReader {

	public ProfileConfigurationLoader() {}
	
	public ProfileMetadata loadProfileConfigurations() {
		Node profileMetadataRoot = loadFromStream("/configuration/ProfileMetadata.xml");
		ProfileMetadata profileMetadata = new ProfileMetadata();
		loadSharedMetadata(profileMetadata, profileMetadataRoot.sharedMetadata[0]);
		loadProfiles(profileMetadata, profileMetadataRoot.profiles[0])
		return profileMetadata;
	}
	
	public void loadSharedMetadata(ProfileMetadata profileMetadata, Node sharedMetadataNode) {
		if(sharedMetadataNode == null) {
			return;
		} else {
			Map<String,String> sharedMetadata = new HashMap<String,String>()
			sharedMetadataNode.children().each { it -> sharedMetadata.put(it.name(),it.text())}
			profileMetadata.setSharedProperties(sharedMetadata)
		}
	}
	
	public void loadProfiles(ProfileMetadata profileMetadata, Node profilesNode) {
		profilesNode.children().each{ it -> 
			loadProfile(profileMetadata, it);
		}
	}
	
	public void loadProfile(ProfileMetadata profileMetadata, Node profile) {
		String profileName = profile.@name
		profile.children().each { it -> 
			profileMetadata.setPropertyForProfile(profileName, it.name(), it.text())
		}
	}

}
