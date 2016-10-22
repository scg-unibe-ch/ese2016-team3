package ch.unibe.ese.team3.model;
/**
 * Enumerates all possible types a house
 */
public enum Type {
	APARTMENT("Appartment"), 
	VILLA("Villa"), 
	HOUSE("House"), 
	STUDIO ("Studio"), 
	LOFT("Loft");
	
	private String name;
	
	private Type(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}
