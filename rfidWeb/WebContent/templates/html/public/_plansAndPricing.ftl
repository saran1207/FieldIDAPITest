<h2><@s.text name="label.new_to_fieldid"/></h2>
<p class="titleSummary"><@s.text name="label.get_a_company_account.full"/></p>
<div class="actionButton">
	<#if tenant?exists>
		<@s.url id="signUpPackageUrl" namespace="public" action="signUpPackages"/>
	<#else>
		<#assign signUpPackageUrl=action.getBaseBrandedUrl("fieldid") + "public/signUpPackages.action"/>
	</#if>
	<div id="plansPricingButton" class="imageButton"><a href="${signUpPackageUrl}"><span><@s.text name="label.plans_and_pricing"/></span></a></div>
</div>
