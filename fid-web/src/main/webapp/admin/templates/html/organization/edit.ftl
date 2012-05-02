<head>
	<@n4.includeStyle href="admin/editOrg"/>	
	<@n4.includeScript src="organizationEdit.js" />		
	<script type="text/javascript">
		editNoteUrl = '<@s.url action="editNote" namespace="/adminAjax"/>';
		cancelNoteUrl = '<@s.url action="cancelNote" namespace="/adminAjax"/>';
		editPlanUrl = '<@s.url action="editPlan" namespace="/adminAjax"/>';
		cancelPlanUrl = '<@s.url action="cancelPlan" namespace="/adminAjax"/>';
		editNameUrl = '<@s.url action="editTenantName" namespace="/adminAjax"/>';
		cancelNameUrl = '<@s.url action="cancelTenantName" namespace="/adminAjax"/>';
        editSignUpUrl = '<@s.url action="editSignUp" namespace="/adminAjax"/>';
        cancelSignUpUrl = '<@s.url action="cancelSignUp" namespace="/adminAjax"/>';
	</script>
</head>

<div id="orgInfo">
	<#assign tenant = primaryOrg.tenant>
    <img class="logo" alt="${(tenant.displayName?html)!}" src='<@s.url action="downloadTenantLogo" namespace="/file" uniqueID="${tenant.id}" />'/>
	<h3>${primaryOrg.displayName}</h3>	
	<div id="loginUrl">
		<#include "_loginUrl.ftl"/>	
	</div>
	
	<div id="address">
		<#if primaryOrg.addressInfo??>
		    <label>${primaryOrg.addressInfo.streetAddress!""}</label>
		    <label>
		    	<#if primaryOrg.addressInfo.city??>${primaryOrg.addressInfo.city}</#if><#if primaryOrg.addressInfo.state??>, ${primaryOrg.addressInfo.state}</#if><#if primaryOrg.addressInfo.country??>, ${primaryOrg.addressInfo.country}</#if>
		    </label>
		    <label>${primaryOrg.addressInfo.zip!""}</label>
		    <label>${primaryOrg.addressInfo.phone1!""}</label>
		</#if>	
	</div>
	
	<div id="createDate">
		<label class="bold">Customer Since: </label>${primaryOrg.created?date}
	</div>
	
	<div id="tenantName">
		<#include "_nameDisplay.ftl"/>
	</div>
	
	<div id="orgStatus">
		<#include "_status.ftl"/>
	</div>

    <div id="signUpDetails" style="padding-top:20px;">
        <#include "_signUpDetails.ftl"/>
    </div>
</div>

<div id="orgPlan">
	<div id="orgLimits">
		<#include "_planDisplay.ftl"/>
	</div>
	<div id="activity">
		<h3>Activity</h3>
		<div class="infoSet"><label>Total Assets:</label><span>${action.getTotalAssets(id)?string.number}</span></div>
		<div class="infoSet"><label>Assets Last 30 Days:</label><span>${action.getTotal30DayAssets(id)?string.number}</span></div>
		<div class="infoSet"><label>Total Events</label> <span>${action.getTotalEvents(id)?string.number}</span></div>
		<div class="infoSet"><label>Events Last 30 Days:</label><span>${action.getTotal30DayEvents(id)?string.number}</span></div>
		<#if action.getLastActiveSession(tenant.id)?exists && action.getLastActiveSession(tenant.id).user.userID?exists && action.getLastActiveSession(tenant.id).user.userID != 'n4systems'>
			<div class="infoSet"><label>Last Login Date:</label><span>${action.getLastActiveSession(tenant.id).lastTouched?datetime}</span></div>
			<div class="infoSet"><label>Last Login User:</label><span>${action.getLastActiveSession(tenant.id).user.userID}</span></div>
		<#else>
			<div class="infoSet"><label>Last Login Date:</label><span>--</span></div>
			<div class="infoSet"><label>Last Login User:</label><span>--</span></div>
		</#if>	
	</div>
	<div id="notes">
		<#include "_noteDisplay.ftl"/>
	</div>
</div>

<div id="extendedFeatures">
<h3>Extended Features</h3>
<table id="extendedFeaturesList">
	<#list availableExtendedFeatures as feature>
		<#if feature == "JobSites">
			<tr><td colspan = "4">&nbsp;</td></td>
		</#if> 		
		<tr id="extendedFeatureRow_${feature}">
			<#assign featureLabel="${feature.label}">
			<#include "_extendedFeatures.ftl"/>
		</tr>
	</#list>	
	<tr><td colspan = "4">&nbsp;</td></td>	
	<tr id="plansAndPricingRow">
		<#include "_plansAndPricing.ftl"/>		
	</tr>
	<tr id="secondaryOrgsRow">
		<#include "_secondaryOrgsButton.ftl"/>		
	</tr>
<table>	</div>