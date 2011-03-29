package org.alfresco.custom.rest.util;

import java.util.Random;

public class RemoteClientTestUtil {

	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";

	public static String generateRandomShortName(String characters, int length) {

		Random rnd = new Random();

		char[] text = new char[length];

		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rnd.nextInt(characters.length()));
		}
		return new String(text);

	}

	public static String generateRandomShortName(int length) {
		return generateRandomShortName(CHARACTERS, length);
	}
}
