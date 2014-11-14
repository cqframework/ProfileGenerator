package org.socraticgrid.uml;

/**
 * Interface implemented by an identifiable resource. That is,
 * a resource that has both a name and an ID.
 * 
 * 
 * @author cnanjo
 *
 */
public interface Identifiable {
	public String getName();
	public void setName(String name);
	public String getId();
	public void setId(String id);
}
