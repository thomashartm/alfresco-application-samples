package org.alfresco.extension.authentication.constraint;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.alfresco.extension.authentication.aop.Credentials;
import org.alfresco.extension.authentication.constraint.PasswordSimilarityConstraint;
import org.alfresco.repo.security.authentication.AuthenticationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PasswordSimilarityConstraintTest {

	private PasswordSimilarityConstraint approver;

	@Before
	public void setUp() throws Exception {
		approver = new PasswordSimilarityConstraint();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void processDifferentPWSuccessfully() {
		approver.setAllowEqualPasswords(false);
		Object[] args = new Object[]{"user", "oldpass".toCharArray(), "newpass".toCharArray()};
		
		try{
			Credentials credentials = new Credentials(args);
			approver.process(credentials);
		}catch(Exception e){
			fail("Did not approve different pws as expected.");
		}
	}

	@Test
	public void processEqualPWsAndFail() {
		approver.setAllowEqualPasswords(false);
		Object[] args = new Object[]{"user", "oldpass".toCharArray(), "oldpass".toCharArray()};
		
		try{
			Credentials credentials = new Credentials(args);
			approver.process(credentials);
			fail("Did not throw AuthenticationException");
		}catch(Exception e){
			Assert.assertTrue(e instanceof AuthenticationException);
		}
	}
	
	@Test
	public void processEqualSuccessfully() {
		approver.setAllowEqualPasswords(true);
		Object[] args = new Object[]{"user", "oldpass".toCharArray(), "oldpass".toCharArray()};
		
		try{
			Credentials credentials = new Credentials(args);
			approver.process(credentials);
		}catch(Exception e){
			fail("Did not approve different pws as expected.");
		}
	}
}