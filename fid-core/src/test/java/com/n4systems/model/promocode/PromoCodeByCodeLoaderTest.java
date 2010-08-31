package com.n4systems.model.promocode;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import javax.persistence.EntityManager;

import org.junit.Test;


public class PromoCodeByCodeLoaderTest {

	
	@Test
	public void should_return_null_for_null_promo_code() {
		test_empty_promo_code(null);
	}
	
	@Test
	public void should_return_null_for_blank_string_promo_code() {
		test_empty_promo_code("");
	}
	
	@Test
	public void should_return_null_for_only_spaces_promo_code() {
		test_empty_promo_code("     ");
	}

	private void test_empty_promo_code(String promoCode) {
		// fixture setup
		EntityManager mockEM = createMock(EntityManager.class);
		replay(mockEM);
		
		PromoCodeByCodeLoader sut = new PromoCodeByCodeLoader();
		sut.setCode(promoCode);
		
		
		// exercise
		PromoCode actualPromoCode = sut.load(mockEM);

		// verify
		assertNull(actualPromoCode);
	}
	
}
