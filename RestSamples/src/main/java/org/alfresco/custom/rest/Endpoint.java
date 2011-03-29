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

package org.alfresco.custom.rest;

/**
 * A remote endpoint i.e. an Alfresco application.
 * A remote endpoint has at least an URL and some authentication information.
 * 
 * @author Thomas Hartmann
 *
 */
public class Endpoint {

	private static final String DEFAULT_HTTP_PORT = "80";
	private static final String HTTP_PREFIX = "http://";
	private static final String HTTPS_PREFIX = "http://";
	private String host;
	private String port;
	private String username;
	private String password;
	
	public Endpoint(String host, String port, String username, String password){
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * Return a fully qualified endpoint address.
	 * 
	 * @return
	 */
	public String getEndpointAddress(boolean secure){
		StringBuilder sb = new StringBuilder(secure ? HTTPS_PREFIX : HTTP_PREFIX);
		sb.append(host);
		if(isPortDeclarationRequired()){
			sb.append(":");
			sb.append(port);
		}
		return sb.toString();
	}

	/**
	 * Checks if the port exists and does not equal the DEFAULt HTTP Port 80
	 * @return
	 */
	private boolean isPortDeclarationRequired() {
		return port != null && !port.equals(DEFAULT_HTTP_PORT);
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
