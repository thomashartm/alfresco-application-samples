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

public class SlingshotApiSortOrderTest {

	private AuthenticatedRestClient client;
	
	@Before
	public void setUp() throws Exception {
		client = new AuthenticatedRestClient("admin", "admin");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSortedDoclistContents() {
		fail("Not yet implemented");
	}
}
