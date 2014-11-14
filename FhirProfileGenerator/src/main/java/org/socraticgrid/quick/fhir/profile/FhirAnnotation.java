package org.socraticgrid.quick.fhir.profile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FhirAnnotation {

	private List<String> annotation;
	private FhirAnnotationParser parser;
	
	
	public FhirAnnotation() {
		annotation = new ArrayList<String>();
		parser = new FhirAnnotationParser();
	}
	
	public FhirAnnotation(String tag) {
		this();
		annotation.addAll(parser.tokenize(tag));
	}
	
	public FhirAnnotation(List<String> tag) {
		this();
		annotation.addAll(tag);
	}
	
	public FhirAnnotation(String tag, List<String> tokenizedTag) {
		this();
		annotation.addAll(tokenizedTag);
	}
	
	public void addToken(String token) {
		annotation.add(token);
	}
	
	public void addToken(int index, String token) {
		annotation.add(index, token);
	}
	
	public int size() {
		return annotation.size();
	}
	
	public String get(int index) {
		return annotation.get(index);
	}
	
	public boolean contains(String token) {
		return annotation.contains(token);
	}
	
	public FhirAnnotation remove(int...tokenIndices) {
		List<String> tokens = new ArrayList<String>();
		tokens.addAll(annotation);
		for(int tokenIndex:tokenIndices) {
			tokens.set(tokenIndex, null);
		}
		Iterator<String> itr = tokens.iterator();
		while(itr.hasNext()) {
			String token = itr.next();
			if(token == null) {
				itr.remove();
			}
		}
		return new FhirAnnotation(tokens);
	}
	
	public FhirAnnotation getSuffix(int size) {
		FhirAnnotation tail = new FhirAnnotation();
		int start = annotation.size() - size;
		for(int index = start; index < annotation.size(); index++) {
			tail.addToken(index - start, get(index));
		}
		return tail;
	}
	
	public FhirAnnotation getPrefix(int numberOfTokens) {
		FhirAnnotation prefix = new FhirAnnotation();
		for(int index = 0; index < numberOfTokens; index++) {
			prefix.addToken(get(index));
		}
		return prefix;
	}
	
	public String getPrefixAsString(int numberOfTokens) {
		return getPrefix(numberOfTokens).toString();
	}
	
	public boolean isPrefix(String prefix) {
		FhirAnnotation annotation = new FhirAnnotation(prefix);
		return isPrefix(annotation);
	}
	
	public boolean isPrefix(FhirAnnotation annotation) {
		boolean isPrefix = true;
		for(int index = 0; index < annotation.size(); index++) {
			if(!annotation.get(index).equals(this.get(index))) {
				isPrefix = false;
				break;
			}
		}
		return isPrefix;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((annotation == null) ? 0 : annotation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FhirAnnotation other = (FhirAnnotation) obj;
		if (annotation == null) {
			if (other.annotation != null)
				return false;
		} else if (!annotation.equals(other.annotation))
			return false;
		return true;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int index = 0; index < annotation.size(); index++) {
			builder.append(annotation.get(index));
			if(index < annotation.size() - 1) {
				builder.append(".");
			}
		}
		return builder.toString();
	}
}
