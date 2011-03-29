package org.alfresco.custom.rest.winkconnector;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class PostRequest {

	private String url;
	private Map<String, Object> arguments = new HashMap<String, Object>();
	private Site site;
	
	public PostRequest(String url){
		this.setUrl(url);
	}
	
	public Map<String, Object> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, Object> arguments) {
		this.arguments = arguments;
	}

	public void addArgument(String name, String value){
		arguments.put(name, value);
	}
	
	public boolean removeArgumentByName(String name){
		if(arguments.containsKey(name)){
			arguments.remove(name);
			return true;
		}
		return false;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
	public JSONObject getArgumentsAsJSON(){
		return new JSONObject(arguments);
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Site getSite() {
		return site;
	}
}
