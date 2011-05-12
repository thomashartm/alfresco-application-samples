package org.alfresco.extension.authentication.constraint;

import org.alfresco.extension.authentication.aop.Credentials;
import org.alfresco.repo.security.authentication.AuthenticationException;

public class PasswordSimilarityConstraint implements CredentialConstraint {

	private boolean allowEqualPasswords;

	/* (non-Javadoc)
	 * @see org.alfresco.extension.authentication.assertion.CredentialApprover#process(org.alfresco.extension.authentication.aop.Credentials)
	 */
	@Override
	public void process(Credentials credentials){
		if(allowEqualPasswords){
			 if(credentials.containsDifferentPasswords()){
				 throw new AuthenticationException("The password does not match the validation constraints.");
			 }
		}else{
			if(!credentials.containsDifferentPasswords()){
				throw new AuthenticationException("The password does not match the validation constraints.");
			}
		}
	}

	public void setAllowEqualPasswords(boolean allowEqualPasswords) {
		this.allowEqualPasswords = allowEqualPasswords;
	}
}
