package org.alfresco.extension.authentication.constraint;

import org.alfresco.extension.authentication.aop.Credentials;

public interface CredentialConstraint {
	void process(Credentials credentials);
}