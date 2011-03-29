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

package org.alfresco.custom.rest.httpconnector;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONObject;

/**
 * 
 * @author thomas.hartmann@alfresco.com
 *
 */
public interface RemoteClient {

	/**
	 * Executes an HTTP GET request to the defined resource that is identified by the URI parameter.
	 * 
	 * @param uri An URI according to the following scheme /alfresco/service/uriresource/of/the/webscript.
	 * @return A {@ Response} object that contains the results of the previously fired HttpRequest.
	 * @throws URISyntaxException
	 * @throws HttpException
	 * @throws IOException
	 */
	public abstract HttpClientResponseImpl executeGetCall(String uri)
			throws URISyntaxException, HttpException, IOException;

	/**
	 * Executes an HTTP POST request to the defined resource that is identified by the URI parameter.
	 * It expects an endpoint to be set and a JSOnObject which contains all required parameters.
	 * 
	 * @param uri An URI according to the following scheme /alfresco/service/uriresource/of/the/webscript.
	 * @param json The post parameters packt into a {@link JSONObjects}. 
	 * @return A {@ Response} object that contains the results of the previously fired HttpRequest.
	 * 
	 * @throws HttpException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public abstract HttpClientResponseImpl executePostCall(String uri, JSONObject json)
			throws HttpException, IOException, URISyntaxException;

}