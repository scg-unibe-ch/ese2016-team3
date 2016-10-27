package ch.unibe.ese.team3.model;

public enum AccountType {
	BASIC("Basic"), PREMIUM("Premium");
	
	private String accountTypeName;
	
	private AccountType(String accountTypeName){
		this.accountTypeName = accountTypeName;
	}
	
	private String getAccountTypeName(){
		return this.accountTypeName;
	}
}
