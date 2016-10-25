package ch.unibe.ese.team3.model;

public enum Gender {
	
	MALE("Male"), FEMALE("Female"), ADMIN("Admin"), OTHER("Other");
	
	private String genderName;
	
	private Gender(String genderName){
		this.genderName = genderName;
	}
	
	public static Gender[] valuesForDisplay(){
		return new Gender[] { MALE, FEMALE, OTHER };
	}
	
	public String getGenderName(){
		return this.genderName;
	}
}
