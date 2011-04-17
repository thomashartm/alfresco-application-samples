package org.alfresco.extension.authentication.approver;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.Assert;

import net.sf.acegisecurity.providers.encoding.PasswordEncoder;

import org.alfresco.extension.authentication.aop.Credentials;
import org.alfresco.repo.security.authentication.AuthenticationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PasswordConstraintApproverTest {

	private PasswordConstraintApprover approver;

	@Before
	public void setUp() throws Exception {
		approver = new PasswordConstraintApprover();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void processDifferentPWSuccessfully() {
		approver.setAllowEqualPasswords(false);
		Object[] args = new Object[]{"user", "oldpass".toCharArray(), "newpass".toCharArray()};
		
		try{
			PasswordEncoder encoder = mock(PasswordEncoder.class);
			when(encoder.encodePassword("newpass", null)).thenReturn("newpass");
			when(encoder.encodePassword("oldpass", null)).thenReturn("oldpass");
			
			Credentials credentials = new Credentials(encoder, args);
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
			PasswordEncoder encoder = mock(PasswordEncoder.class);
			when(encoder.encodePassword(anyString(), anyObject())).thenReturn("oldpass");
			
			Credentials credentials = new Credentials(encoder, args);
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
			PasswordEncoder encoder = mock(PasswordEncoder.class);
			when(encoder.encodePassword(anyString(), anyObject())).thenReturn("oldpass");
			
			Credentials credentials = new Credentials(encoder, args);
			approver.process(credentials);
		}catch(Exception e){
			fail("Did not approve different pws as expected.");
		}
	}
}
