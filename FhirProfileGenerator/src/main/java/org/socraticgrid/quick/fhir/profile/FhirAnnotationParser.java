package org.socraticgrid.quick.fhir.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FhirAnnotationParser {

	public FhirAnnotationParser() {}
	
	public FhirAnnotation parse(String tag) {
		return new FhirAnnotation(tag, tokenize(tag));
	}
	
	public List<String> tokenize(String tag)  {
		List<String> tokenizedTag = new ArrayList<String>();
		if(tag != null && tag.trim().length() > 0) {
			tokenizedTag.addAll(Arrays.asList(tag.split("\\.")));
		}
		return tokenizedTag;
	}
}
