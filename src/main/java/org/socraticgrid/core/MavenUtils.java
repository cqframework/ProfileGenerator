package org.socraticgrid.core;

import java.net.URL;

public class MavenUtils {
	
	/**
	 * If resource is in resource/sample.txt
	 * path is /sample.txt.
	 * @param pathFromResource
	 * @return
	 */
	public static URL getResourceAsUrl(String pathFromResource) {
		return MavenUtils.class.getResource(pathFromResource);
	}

}
