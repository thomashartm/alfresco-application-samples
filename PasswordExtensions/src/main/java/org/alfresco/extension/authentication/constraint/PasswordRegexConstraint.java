package org.alfresco.extension.authentication.constraint;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alfresco.extension.authentication.aop.Credentials;
import org.alfresco.repo.security.authentication.AuthenticationException;

public class PasswordRegexConstraint implements CredentialConstraint{

	private List<Pattern> allowPatterns = new ArrayList<Pattern>();
	
	@Override
	public void process(Credentials credentials) {
		if(allowPatterns != null){
			String password = credentials.getNewPassword();
			for(Pattern pattern : allowPatterns.toArray(new Pattern[allowPatterns.size()])){
				Matcher m = pattern.matcher(password);
				if(!m.matches()){
					throw new AuthenticationException("Password does not match the regex requirements for " + pattern.pattern());
				}
			}
		}
	}

	public void setAllowPatterns(List<String> patterns) {
		Pattern pattern = null;
		for(String p : patterns){
			pattern = (p != null && p.length() > 3) ? Pattern.compile(p) : null;
			this.allowPatterns.add(pattern);
		}
	}

}
