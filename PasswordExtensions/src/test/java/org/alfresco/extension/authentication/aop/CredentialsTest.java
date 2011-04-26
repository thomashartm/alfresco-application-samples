package org.alfresco.extension.authentication.aop;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CredentialsTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void containsDifferentPasswords(){
		Object[] args = new Object[]{"user", "password1".toCharArray(), "password2".toCharArray()};
		
		Credentials credentials = new Credentials(args);
		Assert.assertTrue(credentials.containsDifferentPasswords());
	}
	
	@Test
	public void containsEqualPasswords(){
		Object[] args = new Object[]{"user","password1".toCharArray(),"password1".toCharArray()};
		
		Credentials credentials = new Credentials(args);
		Assert.assertFalse(credentials.containsDifferentPasswords());
	}
	
}
