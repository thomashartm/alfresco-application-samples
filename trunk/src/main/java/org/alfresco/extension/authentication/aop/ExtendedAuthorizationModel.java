package org.alfresco.extension.authentication.aop;

import org.alfresco.service.namespace.QName;

public interface ExtendedAuthorizationModel {
	
	public static final String EX_AUTH_NAMESPACE = "http://www.alfresco.com/model/extdended/auth/1.0";
	public static final QName ASPECT_PASSWORD_EXTENSION = QName.createQName(EX_AUTH_NAMESPACE, "passwordExtension");
	public static final QName PROPERTY_PASSWORD_HISTORY = QName.createQName(EX_AUTH_NAMESPACE, "passwordHistory");
	public static final QName PROPERTY_LAST_MOD = QName.createQName(EX_AUTH_NAMESPACE, "lastModification");
}
