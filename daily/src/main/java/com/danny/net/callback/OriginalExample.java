package com.danny.net.callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OriginalExample {
	public static void main(String[] args) {
		for (String arg : args) {
			File f = new File(arg);
			System.out.println(calculate(f));
		}
	}

	private static String calculate(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			MessageDigest sha = MessageDigest.getInstance("SHA");
			DigestInputStream dis = new DigestInputStream(fis, sha);
			while (dis.read() != -1)
				;
			dis.close();
			byte[] digest = sha.digest();
			StringBuffer sb = new StringBuffer(file.toString());
			sb.append(": ");
			for (byte dig : digest) {
				sb.append(dig + " ");
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
