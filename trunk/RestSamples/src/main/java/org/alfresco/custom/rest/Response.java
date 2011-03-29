package org.alfresco.custom.rest;

import org.apache.commons.httpclient.Header;

public interface Response {

	public abstract String getBodyAsString();

	public abstract int getStatusCode();

	public abstract Header[] getHeaders();

}