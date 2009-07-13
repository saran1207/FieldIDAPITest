<#if securityGuard.jobSitesEnabled>
	<div class="infoSet">
		<label for="criteria.jobSite"><@s.text name="label.jobsite"/></label>
		<@s.select name="criteria.jobSite" list="jobSites" listKey="id" listValue="name" emptyOption="true"  />
	</div>
</#if>
<div class="infoSet">
	<label for="criteria.customer"><@s.text name="label.customer"/></label>
	<#if sessionUser.anEndUser>
		<span>${customers[0].name?html}</span>
	<#else>
		<@s.select  name="criteria.customer" list="customers" listKey="id" listValue="name" emptyOption="true"  onchange="customerChanged(this);"/>
	</#if>
</div>
<div class="infoSet">
	<label for="criteria.division"><@s.text name="label.division"/></label>
	<#if sessionUser.inDivision>
		<span>${divisions[0].name?html}</span>
	<#else>
		<@s.select id="division" name="criteria.division" list="divisions" listKey="id" listValue="name" emptyOption="true" />
	</#if>
</div>