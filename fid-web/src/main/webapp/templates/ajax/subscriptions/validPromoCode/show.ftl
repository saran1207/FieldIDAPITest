var promoCode = $$('#promoCode').first();
<#if validPromoCode>
	promoCode.removeClassName('inputError');
	promoCode.title="";
<#else>
	promoCode.addClassName('inputError');
	promoCode.title="<@s.text name="error.promo_code_not_valid"/>";
</#if>