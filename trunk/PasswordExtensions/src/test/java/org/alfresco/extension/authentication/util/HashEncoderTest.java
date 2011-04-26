package org.alfresco.extension.authentication.util;


import java.security.NoSuchAlgorithmException;

import junit.framework.Assert;

import org.alfresco.extension.authentication.util.HashEncoder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HashEncoderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createUniqueHashesFromString() throws NoSuchAlgorithmException{
		HashEncoder encoder = HashEncoder.getInstance();
		
		String hash = encoder.createHash("testvalue");
		String equalHash = encoder.createHash("testvalue");
		String differentHash = encoder.createHash("differentvalue");
		
		Assert.assertEquals(hash, equalHash);
		Assert.assertFalse(hash.equals(differentHash));
	}
	
	@Test
	public void createUniqueHashesFromDifferentEncoders() throws NoSuchAlgorithmException{
		HashEncoder encoder = HashEncoder.getInstance();
		HashEncoder secondEncoder = HashEncoder.getInstance();
		
		String hash = encoder.createHash("testvalue");
		String hashFromSecondEncoder = secondEncoder.createHash("testvalue");;
		
		Assert.assertEquals(hash, hashFromSecondEncoder);
	}
	
	@Test
	public void isHashed() throws NoSuchAlgorithmException{
		HashEncoder encoder = HashEncoder.getInstance();
		String base = "testvalue";
		String hash = encoder.createHash(base);

		Assert.assertFalse(hash.equals(base));
	}
}
