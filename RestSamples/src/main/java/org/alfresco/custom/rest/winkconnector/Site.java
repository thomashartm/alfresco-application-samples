package org.alfresco.custom.rest.winkconnector;

import org.json.JSONObject;

/**
 * Site construct that represents the 
 * presets/required parameters for a site creation/modification request. 
 * 
 * @author thomas
 *
 */
public class Site {
	
	private String shortName;
	private String sitePreset;
	private String title;
	private String description;
	private String visibility;
	
	public Site(String shortName, String sitePreset, String title,
			String description, String visibility) {
		super();
		this.shortName = shortName;
		this.sitePreset = sitePreset;
		this.title = title;
		this.description = description;
		this.visibility = visibility;
	}

	public JSONObject toJson(){
		return new JSONObject(this);
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public String getSitePreset() {
		return sitePreset;
	}
	
	public void setSitePreset(String sitePreset) {
		this.sitePreset = sitePreset;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getVisibility() {
		return visibility;
	}
	
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
}
