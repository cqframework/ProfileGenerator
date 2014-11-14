package org.socraticgrid.uml;

import java.util.List;

/**
 * Contract supporting the tagging of resources.
 * A tag is a name-value pair that can be used to annotate
 * a resource.
 * 
 * @author cnanjo
 *
 */
public interface Taggable {
	
	public List<TaggedValue> getTags();
	public void setTags(List<TaggedValue> tags);
	public void addTag(TaggedValue tag);
	public void buildTaggedValueIndex();
	public TaggedValue getTaggedValueByKey(String key);
	public boolean getTaggedValueAsBoolean(String key);
	public String getTaggedValueAsString(String key);
	public boolean hasTags();
	public boolean hasNoTags();
}
