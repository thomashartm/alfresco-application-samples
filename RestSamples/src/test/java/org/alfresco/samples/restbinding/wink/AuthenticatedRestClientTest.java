package org.alfresco.samples.restbinding.wink;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.alfresco.custom.rest.util.RemoteClientTestUtil;
import org.alfresco.custom.rest.winkconnector.AuthenticatedRestClient;
import org.alfresco.custom.rest.winkconnector.PostRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthenticatedRestClientTest {

	private AuthenticatedRestClient client;

	@Before
	public void setUp() throws Exception {
		client = new AuthenticatedRestClient("admin", "admin");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetByUrl() {
		fail("Not yet implemented");
	}

	@Test
	public void testPostToUrl() throws JSONException {
		String shortName = RemoteClientTestUtil.generateRandomShortName(7);
		JSONObject result = client.postToUrl(createPostRequest("http://localhost:8080/alfresco/service/api/sites",shortName));
		Assert.assertNotNull(result);
	}
	
	private PostRequest createPostRequest(String uri, String shortname){
		PostRequest request = new PostRequest(uri);
		request.addArgument("shortName", shortname);
		request.addArgument("sitePreset", "site-dashboard");
		request.addArgument("title", "Title " + shortname);
		request.addArgument("description", "Description " + shortname);
		
		return request;
	}

}
