package ch.unibe.ese.team3.model.enums;

public enum PageMode {
	BUY("buy"), RENT("rent"), NONE("none");
	
	private String parameter;
	
	private PageMode(String parameter){
		this.parameter = parameter;
	}
	
	public String getParameter(){
		return parameter;
	}
}