package ch.unibe.ese.team3.util;

import java.util.Comparator;

import ch.unibe.ese.team3.model.Ad;

/**
 * Compare two ads. Ads of premium users are more valuable
 * than those from normal users.
 *
 */
public class PremiumAdComparator implements Comparator<Ad> {

	@Override
	public int compare(Ad first, Ad second) {
		boolean firstAdIsPremium = first.isPremiumAd();
		boolean secondAdIsPremium = second.isPremiumAd();
		
		if (firstAdIsPremium && !secondAdIsPremium){
			return -1;
		}
		else if (!firstAdIsPremium && secondAdIsPremium){
			return 1;
		}
		
		return 0;
	}
	
}