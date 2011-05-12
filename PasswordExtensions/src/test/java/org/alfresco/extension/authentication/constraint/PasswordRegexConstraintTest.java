package org.alfresco.extension.authentication.constraint;


import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.extension.authentication.aop.Credentials;
import org.alfresco.extension.authentication.constraint.PasswordRegexConstraint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PasswordRegexConstraintTest {

	private PasswordRegexConstraint constraint;

	@Before
	public void setUp() throws Exception {
		this.constraint = new PasswordRegexConstraint();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void process(){
		String allowPattern = "[a-zA-Z]*";
		List<String> patterns = new ArrayList<String>();
		patterns.add(allowPattern);
		
		constraint.setAllowPatterns(patterns);
		
		Credentials credentials = createCredentials("user", "validPassword", "oldPassword");
		constraint.process(credentials);
		
		try{
			credentials = createCredentials("user", "invalid12", "oldPassword");
			constraint.process(credentials);
			fail("Authentication exception expected");
		}catch (Exception e) {

		}
	}
	
	private Credentials createCredentials(String user, String newPw, String oldPw){
		Object[] arguments = new Object[]{user, oldPw.toCharArray(), newPw.toCharArray()};
		return new Credentials(arguments);
	}
}
