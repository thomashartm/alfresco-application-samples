package org.alfresco.custom.rest.winkconnector;

import javax.ws.rs.core.MediaType;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.client.handlers.BasicAuthSecurityHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticatedRestClient {

	private static final String ACCEPT = "Accept";
	private ClientConfig config;
	private RestClient client;

	public AuthenticatedRestClient(String user, String password) {
		config = new ClientConfig();

		BasicAuthSecurityHandler basicAuthHandler = new BasicAuthSecurityHandler();
		basicAuthHandler.setUserName(user);
		basicAuthHandler.setPassword(password);
		config.handlers(basicAuthHandler);

		this.client = new RestClient(this.config);
	}

	/**
	 * Sends a GET Request to a REST (Representational State Transfer) service. 
	 * 
	 * @param url
	 *            Uses a String representation because the java.net.Url class is buggy.
	 * @return A JSONObject.
	 * @throws JSONException
	 */
	public JSONObject getByUrl(String url) throws JSONException {
		Resource resource = client.resource(url);
		resource.header(ACCEPT, Header.JSON_ACCEPT_HEADER.toString());
		ClientResponse response = resource.get();
		
		return new JSONObject(response.getEntity(String.class));
	}

	/**
	 * Sends a POST Request to a REST (Representational State Transfer) service. 
	 * 
	 * @param request A PostRequest that contains the url and the required arguments
	 * @return JSONObject
	 * @throws JSONException
	 */
	public JSONObject postToUrl(PostRequest request) throws JSONException {
		final String url = request.getUrl();

		Resource alfrescoSite = client.resource(url);
		alfrescoSite.contentType(MediaType.APPLICATION_JSON);
		alfrescoSite.accept(MediaType.APPLICATION_JSON_TYPE);
		
		JSONObject payload = request.getArgumentsAsJSON();
		ClientResponse response = alfrescoSite.post(payload.toString());
		
		return new JSONObject(response.getEntity(String.class));
	}


}
