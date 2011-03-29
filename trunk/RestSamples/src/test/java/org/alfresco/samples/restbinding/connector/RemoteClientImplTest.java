/**
 * Copyright (C) 2005-2009 Alfresco Software Limited.
 *
 * This file is part of the Spring Surf Extension project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 * This code is considered to be a demonstration and sample of how to use the Alfresco REST/Webscript API.
 * Alfresco will not assume any liability for this code.
 * Please assure to quality check the code before usage.
 */

package org.alfresco.samples.restbinding.connector;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import junit.framework.Assert;
import org.alfresco.custom.rest.Endpoint;
import org.alfresco.custom.rest.httpconnector.HttpClientResponseImpl;
import org.alfresco.custom.rest.httpconnector.RemoteClient;
import org.alfresco.custom.rest.httpconnector.RemoteClientImpl;
import org.alfresco.custom.rest.httpconnector.Site;
import org.alfresco.custom.rest.util.RemoteClientTestUtil;
import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RemoteClientImplTest {

	private RemoteClient remoteClient;

	@Before
	public void setUp() throws Exception {
		remoteClient = new RemoteClientImpl(createEndpoint());
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Simple GET request to obtain site related information. Positive test
	 * expected as long as the site test exists.
	 * 
	 * @throws URISyntaxException
	 * @throws HttpException
	 * @throws IOException
	 */
	@Test
	public void testCallWebscriptByHttpGet() throws URISyntaxException,
			HttpException, IOException {
		String webscriptUri = "/alfresco/service/api/sites/test";
		HttpClientResponseImpl reponse = remoteClient.executeGetCall(webscriptUri);
		Assert.assertEquals(200, reponse.getClientResponseCode());
	}

	/**
	 * Simple GET request to obtain site related information. Negative test
	 * expected because the URI is invalid {illegal char inside}.
	 * 
	 * @throws URISyntaxException
	 * @throws HttpException
	 * @throws IOException
	 */
	@Test
	public void testCallInvalidWebscriptByHttpGet() throws HttpException,
			IOException {
		try {
			String invalidWebscriptUri = "/alfresco/service/api/sites/{test";
			remoteClient.executeGetCall(invalidWebscriptUri);
			fail("Request was expected to fail because of an illegal char!!!");
		} catch (URISyntaxException uriEx) {
			// expected
		}
	}

	@Test
	public void testCreateSiteWebscript() throws URISyntaxException,
			HttpException, IOException, JSONException {
		
		String webscriptUri = "/alfresco/service/api/sites";
		String shortName = RemoteClientTestUtil.generateRandomShortName(5);
		
		Site siteConfiguration = 
				createSiteCreationParameters(shortName);

		System.out.println(siteConfiguration.toJson().toString());
		HttpClientResponseImpl reponse = remoteClient.executePostCall(webscriptUri, siteConfiguration.toJson());
		System.out.println(reponse.getBodyAsString());

		Assert.assertEquals(200, reponse.getClientResponseCode());

	}

	// TODO use a configurable site object as argument
	private Site createSiteCreationParameters(String shortName) {
		// A new site requires the name, preset id (share-webapp/presets/preset.xml, shortname, description and the visibility)
		return new Site("Demo"+shortName, "site-dashboard", shortName + " title", shortName + " description", "PUBLIC");
	}

	/**
	 * Expects a running Alfresco installation
	 * 
	 * @return
	 */
	private Endpoint createEndpoint() {
		return new Endpoint("localhost", "8080", "admin", "admin");
	}

}
