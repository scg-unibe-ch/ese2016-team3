package ch.unibe.ese.team3.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import ch.unibe.ese.team3.model.Ad;

public class PremiumAdComparatorTest {
	@Test
	public void firstPremiumLastNotPremium(){
		Ad first = mock(Ad.class);
		Ad second = mock(Ad.class);
		
		when(first.isPremiumAd()).thenReturn(true);
		when(second.isPremiumAd()).thenReturn(false);
		
		PremiumAdComparator comp = new PremiumAdComparator();
		
		assertEquals(-1, comp.compare(first, second));		
	}
	
	@Test
	public void firstNotPremiumLastPremium(){
		Ad first = mock(Ad.class);
		Ad second = mock(Ad.class);
		
		when(first.isPremiumAd()).thenReturn(false);
		when(second.isPremiumAd()).thenReturn(true);
		
		PremiumAdComparator comp = new PremiumAdComparator();
		
		assertEquals(1, comp.compare(first, second));		
	}
	
	@Test
	public void bothPremium(){
		Ad first = mock(Ad.class);
		Ad second = mock(Ad.class);
		
		when(first.isPremiumAd()).thenReturn(true);
		when(second.isPremiumAd()).thenReturn(true);
		
		PremiumAdComparator comp = new PremiumAdComparator();
		
		assertEquals(0, comp.compare(first, second));		
	}
	
	@Test
	public void bothNotPremium(){
		Ad first = mock(Ad.class);
		Ad second = mock(Ad.class);
		
		when(first.isPremiumAd()).thenReturn(false);
		when(second.isPremiumAd()).thenReturn(false);
		
		PremiumAdComparator comp = new PremiumAdComparator();
		
		assertEquals(0, comp.compare(first, second));		
	}
}
