<td <#if primaryOrg.plansAndPricingAvailable> class='onIcon'<#else> class='offIcon'</#if>></td>
<td>
	<@s.form id="plansAndPricingForm" action="updatePlansAndPricing" namespace="/adminAjax" theme="fieldidSimple">
		<@s.hidden name="id"/>
		<#if primaryOrg.plansAndPricingAvailable> 
			<@s.hidden name="primaryOrg.plansAndPricingAvailable" value="false" />
		<#else> 
			<@s.hidden name="primaryOrg.plansAndPricingAvailable" value="true" />
		</#if>

		<a href="javascript:void(0);" onClick="savePlansAndPricing();">
			<#if primaryOrg.plansAndPricingAvailable>on<#else>off</#if>
		</a>
	</@s.form>
</td>
<td class="featureLabel"><@s.text name="feature.plans_and_pricing"/></td>
<td> <img id="loading_plansAndPricing" class="loading" src="<@s.url value="../images/loading-small.gif"/>" /><td>
