package ch.unibe.ese.team3.model.enums;

public enum Distance {
	OneHundred(100, "100 m"), 
	TwoHundred(200, "200 m"), 
	ThreeHundred(300, "300 m"), 
	FourHundred(400, "400 m"), 
	FiveHundred(500, "500 m"), 
	SixHundred(600, "600 m"), 
	SevenHundred(700, "700 m"), 
	EightHundred(800, "800 m"), 
	NineHundred(900, "900 m"), 
	Thousand(1000, "1 km"), 
	MoreThanThousand(2000, "More than 1 km");

	private int distance;
	private String name;

	private Distance(int distance, String name) {
		this.distance = distance;
		this.name = name;
	}

	public int getDistance() {
		return distance;
	}

	public String getName() {
		return name;
	}

	public static Distance fromInt(int distance) {
		switch (distance) {
		case 100:
			return Distance.OneHundred;
		case 200:
			return Distance.TwoHundred;
		case 300:
			return Distance.ThreeHundred;
		case 400:
			return Distance.FourHundred;
		case 500:
			return Distance.FiveHundred;
		case 600:
			return Distance.SixHundred;
		case 700:
			return Distance.SevenHundred;
		case 800:
			return Distance.EightHundred;
		case 900:
			return Distance.NineHundred;
		case 1000:
			return Distance.Thousand;
		default:
			return Distance.MoreThanThousand;
		}
	}
}
