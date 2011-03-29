package org.alfresco.custom.rest.httpconnector;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
	
	public String getShortName() {
		return shortName;
	}

	public String getSitePreset() {
		return sitePreset;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getVisibility() {
		return visibility;
	}
	
	public JSONObject toJson() throws JSONException{
		Map<String, String> params = new HashMap<String, String>();
		params.put("shortName", shortName);
		params.put("sitePreset", sitePreset);
		params.put("title", title);
		params.put("description", description);
		params.put("visibility", "PUBLIC");
		
		JSONObject json = new JSONObject(params);
		return json;
	}
}
