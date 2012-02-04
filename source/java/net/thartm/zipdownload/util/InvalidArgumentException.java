package net.thartm.zipdownload.util;

public class InvalidArgumentException extends RuntimeException{

	private static final long serialVersionUID = 291472375173536658L;

	public InvalidArgumentException(String message){
		 super(message);
	 }
	 
	 public InvalidArgumentException(String message, Throwable t){
		 super(message, t);
	 }
}
