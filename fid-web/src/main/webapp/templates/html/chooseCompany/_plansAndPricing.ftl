

<div class="actionButton">
	<#if (securityGuard.tenant)?exists>
		<@s.url id="signUpPackageUrl" namespace="public" action="signUpPackages"/>
	<#else>
		<#assign signUpPackageUrl=action.getBaseBrandedUrl(action.houseAccountName) + "public/signUpPackages.action"/>
	</#if>
	<div id="plansPricingButtonChooseCompany" class="imageButton"><a href="${signUpPackageUrl}"><span><@s.text name="label.plans_and_pricing"/></span></a></div>
</div>
