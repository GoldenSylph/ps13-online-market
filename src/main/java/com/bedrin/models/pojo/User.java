package com.bedrin.models.pojo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User implements java.io.Serializable {

	private static final long serialVersionUID = 794534936335587472L;

	public static final String EMPTY_UUID = "0";

	private String email;
	private String password;
	private boolean bought;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPasswordRaw(String newPassword) {
		this.password = newPassword;
	}
	
	public void setPassword(String password) throws NoSuchAlgorithmException {
		this.password = encodePassword(password);
	}

	public boolean isBought() {
		return bought;
	}

	public void setBought(boolean bought) {
		this.bought = bought;
	}
	
	@Override
	public String toString() {
		return getEmail() + " : " + getPassword() + " : " + isBought();
	}
	
	public static String encodePassword(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte byteData[] = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

}
