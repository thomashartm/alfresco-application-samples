package org.alfresco.custom.rest.winkconnector;

public enum Header {
	JSON_ACCEPT_HEADER("application/json;q=1.0, application/xml;q=0.8");

	private String header;

	Header(String header) {
		this.setHeader(header);
	}

	private void setHeader(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}
	
	public String toString(){
		return this.header;
	}
	
	
}
