package com.bedrin.models.dto;

public class ProfileEmailChangeDTO {
	
	private String email;
	
	private boolean isFindOldEmailError;
	private boolean isSetNewEmailError;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isFindOldEmailError() {
		return isFindOldEmailError;
	}

	public void setFindOldEmailError(boolean isFindOldEmailError) {
		this.isFindOldEmailError = isFindOldEmailError;
	}

	public boolean isSetNewEmailError() {
		return isSetNewEmailError;
	}

	public void setSetNewEmailError(boolean isSetNewEmailError) {
		this.isSetNewEmailError = isSetNewEmailError;
	}
	
}
