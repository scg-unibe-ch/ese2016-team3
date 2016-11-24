package ch.unibe.ese.team3.model.enums;

public enum AuctionStatus {
	NotYetRunning("Not yet running"),
	Running("Running"),
	Stopped("Stopped"),
	Expired("Expired"),
	Completed("Completed"),
	NoAuction("No auction");
	
	private String name;
	
	private AuctionStatus(String name){
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
