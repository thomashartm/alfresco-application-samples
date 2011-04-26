package org.alfresco.extension.authentication.constraint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alfresco.extension.authentication.aop.Credentials;
import org.alfresco.repo.security.authentication.AuthenticationException;

public class PasswordRegexConstraint implements CredentialConstraint{

	private Pattern allowPattern;
	
	@Override
	public void process(Credentials credentials) {
		if(allowPattern != null){
			String password = credentials.getNewPassword();
			Matcher m = allowPattern.matcher(password);
			if(!m.matches()){
				System.out.println(m.toString());
				throw new AuthenticationException("Password does not match the regex requirements.");
			}
		}
	}

	public void setAllowPattern(String pattern) {
		this.allowPattern = (pattern != null && pattern.length() > 3) ? Pattern.compile(pattern) : null;
	}

}
