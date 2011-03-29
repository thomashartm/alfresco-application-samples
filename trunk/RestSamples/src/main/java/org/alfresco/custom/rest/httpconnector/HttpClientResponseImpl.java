package org.alfresco.custom.rest.httpconnector;

import java.io.IOException;

import org.alfresco.custom.rest.Response;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.URI;

public class HttpClientResponseImpl implements Response {
	private String body;
	private int statusCode;
	private int clientResponseCode;
	private Header[] headers;
	private URI usedUri;

	public HttpClientResponseImpl(org.apache.commons.httpclient.HttpMethod method, int clientResponseCode)
			throws IOException {
		assertRequestWasExecuted(method);

		this.clientResponseCode = clientResponseCode;
		this.body = method.getResponseBodyAsString();
		this.statusCode = method.getStatusCode();
		this.headers = method.getResponseHeaders();
		this.usedUri = method.getURI();
	}

	private void assertRequestWasExecuted(
			org.apache.commons.httpclient.HttpMethod method) {

		if (!method.hasBeenUsed()) {
			throw new RuntimeException(
					"The HttpMethod has never been used. "
							+ "Please assure that the request has already been executed.");
		}
	}

	/* (non-Javadoc)
	 * @see org.alfresco.custom.rest.httpconnector.Response#getBodyAsString()
	 */
	@Override
	public String getBodyAsString() {
		return body;
	}

	/* (non-Javadoc)
	 * @see org.alfresco.custom.rest.httpconnector.Response#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		return statusCode;
	}

	/* (non-Javadoc)
	 * @see org.alfresco.custom.rest.httpconnector.Response#getHeaders()
	 */
	@Override
	public Header[] getHeaders() {
		return headers;
	}

	public URI getUsedUri() {
		return usedUri;
	}

	public int getClientResponseCode() {
		return clientResponseCode;
	}
	
	public String toString() {
		StringBuilder headerPrintLine = new StringBuilder();
		for (Header header : headers) {
			headerPrintLine.append(header.getName());
			headerPrintLine.append(":");
			headerPrintLine.append(header.getValue());
			headerPrintLine.append(",");
		}

		headerPrintLine.deleteCharAt(headerPrintLine.lastIndexOf(","));

		return "StatusCode: " + statusCode + "; URI: " + usedUri + " Headers: "
				+ headerPrintLine.toString();
	}
}
