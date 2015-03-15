package com.danny.net.callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ThreadImpl extends Thread {

	File file;

	ThreadImpl(File file) {
		this.file = file;
	}

	public void run() {
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
			System.out.println(sb.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		for (String arg : args) {
			File f = new File(arg);
			ThreadImpl ti = new ThreadImpl(f);
			ti.start();
		}
	}
}
