package ch.unibe.ese.team3.model;
/**
 * Enumerates all possible types a house
 */
public enum Type {
	APARTMENT {
		public String getName(){
			return "Appartment";
		}
	},
	
	VILLA {
		public String getName(){
			return "Villa";
		}
	},
	
	HOUSE {
		public String getName(){
			return "House";
		}
	},
	
	STUDIO {
		public String getName(){
			return "Studio";
		}
	},
	
	LOFT {
		public String getName(){
			return "Loft";
		}
	}

}
