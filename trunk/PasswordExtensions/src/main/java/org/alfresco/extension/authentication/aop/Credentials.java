package org.alfresco.extension.authentication.aop;

import java.security.NoSuchAlgorithmException;

import org.alfresco.extension.authentication.util.HashEncoder;

public class Credentials {
	
	private String userName;
	private String oldPasswordHash;
	private String newPasswordHash;
	private String newPassword;

	public Credentials(Object args[]){
		if(args.length == 3){
			HashEncoder encoder = HashEncoder.getInstance();
			
			if(args[0] instanceof String){
				this.userName = (String) args[0];
			}
			
			try {
				if(args[1] instanceof char[]){
					this.oldPasswordHash = encoder.createHash(new String((char[]) args[1]));
				}
				
				if(args[2] instanceof char[]){
					this.newPassword = new String((char[]) args[2]);
					this.newPasswordHash = encoder.createHash(this.newPassword);
					
				}
			} catch (NoSuchAlgorithmException e) {
				new RuntimeException("Did not find the required hashing algorithm.", e);
			}
		}
	}
		
	public String getUserName() {
		return userName;
	}

	public String getOldPasswordHash() {
		return oldPasswordHash;
	}

	public String getNewPasswordHash() {
		return newPasswordHash;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	
	public boolean containsDifferentPasswords(){
		return !oldPasswordHash.equals(newPasswordHash);
	}
}