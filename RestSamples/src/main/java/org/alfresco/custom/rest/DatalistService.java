package org.alfresco.custom.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.custom.rest.httpconnector.RemoteClient;
import org.alfresco.custom.rest.httpconnector.RemoteClientImpl;
import org.apache.commons.httpclient.HttpException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The datalist service uses an internal API and is designed for testing purposes and as a demo.
 * Please use an own webscript to query your specific datalists.#
 * 
 * @author thomas
 *
 */
public class DatalistService {

	private static final String DATALISTS_CONTAINER = "/dataLists";
	private static final String DATALISTS_OVERVIEW_URI = "/alfresco/s/slingshot/datalists/lists/site/";
	private static final String DATALISTS_ITEMS_URI = "/alfresco/service/slingshot/datalists/data/site/";
	private RemoteClient remote;
	private static final String DATALISTS = "datalists";

	public DatalistService() {
		Endpoint endpoint = new Endpoint("localhost", "8080", "admin", "admin");
		remote = new RemoteClientImpl(endpoint);
	}

	public List<JSONObject> getAllDataLists(String site) throws HttpException,
			URISyntaxException, IOException, JSONException {
		List<JSONObject> results = new ArrayList<JSONObject>();

		String uri = getDatalistsUri(DATALISTS_OVERVIEW_URI, site);
		Response response = remote.executeGetCall(uri);
		JSONObject jsonResult = new JSONObject(response.getBodyAsString());
		
		JSONArray lists = jsonResult.getJSONArray(DATALISTS);
		
		for (int i = 0; i < lists.length(); i++) {
			JSONObject obj = lists.getJSONObject(i);
			results.add(obj);
		}

		return results;
	}

	private String getDatalistsUri(String webScriptUri, String site, String...segments) {
		StringBuilder sb = new StringBuilder(webScriptUri);
		sb.append(site);
		sb.append(DATALISTS_CONTAINER);
		for(String segment : segments){
			sb.append("/");
			sb.append(segment);
		}
		return sb.toString();
	}

	public List<JSONObject> getDataListbyName(String site, String dataListName) throws HttpException, IOException, URISyntaxException {
		String uri = getDatalistsUri(DATALISTS_ITEMS_URI, site, dataListName);
		JSONObject jsonObject =  createQueryJSONObject(new String[0]);
		Response response = remote.executePostCall(uri, jsonObject);
		System.out.println(response.getBodyAsString());
		return null;
	}
	
	
	
	
	/**
	 * Creates a JSON Object like the following example: 
	 * {"fields":["cm_title","dl_locationAddress1"],"filter":{"filterData":"","filterId":"all"}}
	 * 
	 */
	private JSONObject createQueryJSONObject(String[] fields){
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("fields", fields);
		
		Map<String,String> filterQuery = new HashMap<String, String>();
		filterQuery.put("filterData","");
		filterQuery.put("filterId","all");
		queryMap.put("filter", filterQuery);

		return new JSONObject(queryMap);
	}
}
