package org.alfresco.custom.rest.util;

public enum ContentType {

	JSON("application/json"), OCTECT_STREAM("application/octet-stream");

	private String contentType;

	ContentType(String contentType) {
		this.contentType = contentType;
	}

	public String value() {
		return contentType;
	}
}
