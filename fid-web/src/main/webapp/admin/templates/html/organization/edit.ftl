<head>
	<@n4.includeStyle href="admin/editOrg"/>	
	<@n4.includeScript src="organizationEdit.js" />		
	<script type="text/javascript">
		editNoteUrl = '<@s.url action="editNote" namespace="/adminAjax"/>'
		cancelNoteUrl = '<@s.url action="cancelNote" namespace="/adminAjax"/>'
		editPlanUrl = '<@s.url action="editPlan" namespace="/adminAjax"/>'
		cancelPlanUrl = '<@s.url action="cancelPlan" namespace="/adminAjax"/>'
	</script>
</head>

<div id="orgInfo">
	<#assign tenant = primaryOrg.tenant>
    <img class="logo" alt="${(tenant.displayName?html)!}" src='<@s.url action="downloadTenantLogo" namespace="/file" uniqueID="${tenant.id}" />'/>
	<h3>${primaryOrg.displayName}</h3>	
	<a href='${action.getLoginUrlForTenant(tenant)}' target='_blank' >${tenant.name}.fieldid.com</a>
	
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
	
	<div id="orgStatus">
		<#include "_status.ftl"/>
	</div>
</div>

<div id="orgPlan">
	<div id="orgLimits">
		<#include "_planDisplay.ftl"/>
	</div>
	<div id="activity">
		<h3>Activity</h3>
		<div class="infoSet"><label>Total Assets:</label><span>${limits.assetsUsed?string.number}</span></div>
		<div class="infoSet"><label>Assets Last 30 Days:</label><span>${action.getTotal30DayAssets(id)?string.number}</span></div>
		<div class="infoSet"><label>Total Events</label> <span>${action.getTotalEvents(id)?string.number}</span></div>
		<div class="infoSet"><label>Events Last 30 Days:</label><span>${action.getTotal30DayEvents(id)?string.number}</span></div>
		<#if action.getLastActiveSession(tenant.id)?exists && action.getLastActiveSession(tenant.id).user.userID != 'n4systems'>
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
	<#include "_extendedFeatures.ftl"/>
</div>