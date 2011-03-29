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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.alfresco.custom.rest.Endpoint;
import org.alfresco.custom.rest.util.Base64;
import org.alfresco.custom.rest.util.ContentType;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.json.JSONObject;


public class RemoteClientImpl implements RemoteClient {

	private static final String UTF_8_ENCODING = "UTF-8";
	private Endpoint endpoint;
	private String ticketName;
	private Object ticket;

	public RemoteClientImpl(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	/* (non-Javadoc)
	 * @see org.alfresco.samples.restbinding.connector.RemoteClient#executeGetCall(java.lang.String)
	 */
	@Override
	public HttpClientResponseImpl executeGetCall(String uri) throws URISyntaxException,
			HttpException, IOException {
		return service(HttpMethod.GET, buildURL(uri), ContentType.JSON, null);
	}

	/* (non-Javadoc)
	 * @see org.alfresco.samples.restbinding.connector.RemoteClient#executePostCall(java.lang.String, org.json.JSONObject)
	 */
	@Override
	public HttpClientResponseImpl executePostCall(String uri, JSONObject json)
			throws HttpException, IOException, URISyntaxException {
		
		String body = (json != null) ? json.toString() : "";
		byte[] bytes = body.getBytes(UTF_8_ENCODING);

		return service(HttpMethod.POST, buildURL(uri), ContentType.JSON, new ByteArrayInputStream(bytes));
	}

	private HttpClientResponseImpl service(HttpMethod httpMethod, URI uri, ContentType contentType, InputStream in)
			throws HttpException, IOException {

		HttpClient client = new HttpClient();
		
		// HttpMethod configuration
		org.apache.commons.httpclient.HttpMethod method = createRequestMethod(httpMethod, uri);
		applyBasicAuthentication(method);

		// prepare the POST/PUT entity data if input supplied
		if (in != null) {
			preparePostPutRequest(contentType, in, method);
		}

		// execute the method to get the response code
		int responseCode = client.executeMethod(method);
		return new HttpClientResponseImpl(method, responseCode);
	}

	private void preparePostPutRequest(ContentType contentType, InputStream in, org.apache.commons.httpclient.HttpMethod method) {
		method.setRequestHeader("Content-Type", contentType.value());

		// apply content-length here if known (i.e. from proxied req)
		// if this is not set, then the content will be buffered in memory
		int contentLength = InputStreamRequestEntity.CONTENT_LENGTH_AUTO;

		((EntityEnclosingMethod) method)
				.setRequestEntity(new InputStreamRequestEntity(in, contentLength));
	}

	/**
	 * Applies HTTP basic authentication support to the provided method.
	 * 
	 * @param method
	 */
	private void applyBasicAuthentication(
			org.apache.commons.httpclient.HttpMethod method) {
		if (endpoint.getUsername() != null && endpoint.getPassword() != null) {
			String auth = endpoint.getUsername() + ':' + endpoint.getPassword();
			method.addRequestHeader("Authorization",
					"Basic " + Base64.encodeBytes(auth.getBytes()));
		}
	}

	private org.apache.commons.httpclient.HttpMethod createRequestMethod(
			HttpMethod requestMethod, URI redirectURL) {
		org.apache.commons.httpclient.HttpMethod method = null;

		switch (requestMethod) {
		default:
		case GET:
			method = new GetMethod(redirectURL.toString());
			break;
		case PUT:
			method = new PutMethod(redirectURL.toString());
			break;
		case POST:
			method = new PostMethod(redirectURL.toString());
			break;
		case DELETE:
			method = new DeleteMethod(redirectURL.toString());
			break;
		case HEAD:
			method = new HeadMethod(redirectURL.toString());
			break;
		}

		// Switch off automatic redirect handling as we want to process them
		// ourselves and maintain cookies
		method.setFollowRedirects(false);

		return method;
	}

	/**
	 * Build the URL object based on the supplied uri and configured endpoint.
	 * Ticket will be applied as an argument if available. (Tickets are currently not used inside this sample)
	 * 
	 * @param uri URI to build URL against
	 * 
	 * @return the URL object representing the call.
	 * @throws URISyntaxException
	 * 
	 */
	private URI buildURL(String uri) throws URISyntaxException {
		URI url;
		String endpointAddress = endpoint.getEndpointAddress(false);
		String resolvedUri = uri.startsWith(endpointAddress) ? uri : endpointAddress + uri;

		if (getTicket() == null) {
			url = new URI(resolvedUri);
		} else {
			url = new URI(resolvedUri
					+ (uri.lastIndexOf('?') == -1 ? ("?" + getTicketName()
							+ "=" + getTicket())
							: ("&" + getTicketName() + "=" + getTicket())));
		}
		return url;
	}

	private String getTicketName() {
		return this.ticketName;
	}

	private Object getTicket() {
		return this.ticket;
	}

}
