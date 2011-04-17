package org.alfresco.extension.authentication.approver;

import org.alfresco.extension.authentication.aop.Credentials;

public interface CredentialApprover {
	void process(Credentials credentials);
}