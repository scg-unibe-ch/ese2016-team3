package ch.unibe.ese.team3.model;

/**
 * Enumerates all possible types Internet / TV infrastructure
 */
public enum InfrastructureType {
	SATELLITE("Satellite"), CABLE("Cable"), FOC("Fiber optic cable");

	private String name;

	private InfrastructureType(String name){
			this.name = name;
		}

	public String getName() {
		return name;
	}
}
