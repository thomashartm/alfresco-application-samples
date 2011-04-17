package org.alfresco.extension.authentication.aop;


import junit.framework.Assert;

import net.sf.acegisecurity.providers.encoding.PasswordEncoder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

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
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		when(encoder.encodePassword("password1", null)).thenReturn("password1");
		when(encoder.encodePassword("password2", null)).thenReturn("password2");
		
		Credentials credentials = new Credentials(encoder, args);
		Assert.assertTrue(credentials.containsDifferentPasswords());
	}
	
	@Test
	public void containsEqualPasswords(){
		Object[] args = new Object[]{"user","password1".toCharArray(),"password1".toCharArray()};
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		when(encoder.encodePassword(anyString(), anyObject())).thenReturn("password1");
		
		Credentials credentials = new Credentials(encoder, args);
		Assert.assertFalse(credentials.containsDifferentPasswords());
	}
	
}
