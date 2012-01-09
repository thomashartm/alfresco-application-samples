package net.thartm.alfresco.cleanup.webscript;

public enum Action {

	CANCEL("cancel");
	
	String name;
	
	Action(String name){
		this.name = name;
	}
	
	public boolean equals(String value){
		return name.equalsIgnoreCase(value);
	}
}
