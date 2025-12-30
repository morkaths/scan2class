package com.morkath.scan2class.util;

import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class HashUtils {
	private static final String SALT = "S2C_SECRET_";

	public static String encodeId(Long id) {
		if (id == null)
			return null;
		String rawString = SALT + id;
		return Base64.getUrlEncoder().withoutPadding().encodeToString(rawString.getBytes(StandardCharsets.UTF_8));
	}

	public static Long decodeId(String encodedId) {
		if (encodedId == null || encodedId.isEmpty())
			return null;

		try {
			byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedId);
			String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
			if (decodedString.startsWith(SALT)) {
				String idPart = decodedString.substring(SALT.length());
				return Long.parseLong(idPart);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}