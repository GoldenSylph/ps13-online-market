package com.bedrin.models.pojo;

public class Bug implements java.io.Serializable {

	private static final long serialVersionUID = -8698456375721171616L;

	private String email;
	private String logFile;
	private String description;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getLogFile() {
		return logFile;
	}
	
	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
