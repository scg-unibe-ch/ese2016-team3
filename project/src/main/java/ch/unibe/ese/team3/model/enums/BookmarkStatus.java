package ch.unibe.ese.team3.model.enums;

public enum BookmarkStatus {
	
	NotLoggedIn(0), NoUserFound(1), NotBookmarked(2), Bookmarked(3), OwnAd(4);
	
	private int statusCode;
	
	private BookmarkStatus(int statusCode){
		this.statusCode = statusCode;
	}
	
	public int getStatusCode(){
		return this.statusCode;
	}
}