package net.thartm.treeutil.bulk.nodecreation.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.namespace.QName;

public class NodePattern {

	private String name;
	private Map<QName, Serializable> properties = new HashMap<QName, Serializable>();
	private ContentData contentData;
	private Set<QName> aspects = new HashSet<QName>();
	private QName type;
	
	public NodePattern(){	
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	
	public void addProperty(QName key, Serializable value){
		properties.put(key, value);
	}
	
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the properties
	 */
	public Map<QName, Serializable> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Map<QName, Serializable> properties) {
		this.properties = properties;
	}

	public void setContentData(ContentData contentData) {
		this.contentData = contentData;
	}

	public ContentData getContentData() {
		return contentData;
	}

	public void setAspects(Set<QName> aspects) {
		this.aspects = aspects;
	}

	public Set<QName> getAspects() {
		return aspects;
	}

	public void setType(QName type) {
		this.type = type;
	}

	public QName getType() {
		return type;
	}
}