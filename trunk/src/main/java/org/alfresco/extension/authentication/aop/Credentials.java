package org.alfresco.extension.authentication.aop;

import net.sf.acegisecurity.providers.encoding.PasswordEncoder;

public class Credentials {
	
	private String userName;
	private String oldPassword;
	private String newPassword;
	
	public Credentials(PasswordEncoder encoder, Object args[]){
		if(args.length == 3){
			if(args[0] instanceof String){
				this.userName = (String) args[0];
			}
			
			if(args[1] instanceof char[]){
				this.oldPassword = encoder.encodePassword(new String((char[]) args[1]), null);
			}
			
			if(args[2] instanceof char[]){
				this.newPassword = encoder.encodePassword(new String((char[]) args[2]), null);
			}
		}
	}

	public String getUserName() {
		return userName;
	}

	public String getOldPasswordHash() {
		return oldPassword;
	}

	public String getNewPasswordHash() {
		return newPassword;
	}
	
	public boolean containsDifferentPasswords(){
		return !oldPassword.equals(newPassword);
	}
}
