<head>
	<#include "/templates/html/common/_calendar.ftl"/>
	<script type="text/javascript" src="<@s.url value="/javascript/customerUpdate.js"  />"></script>
	
	<#if securityGuard.jobSitesEnabled>
		<script type="text/javascript" src="<@s.url value="/javascript/changeJobSite.js" />"></script>
	</#if>

	<script type="text/javascript">
		customerChangeUrl = "<@s.url action="divisionList" namespace="/ajax" />";
		jobSiteChangeUrl = '<@s.url action="jobSite" namespace="/ajax" />';
	</script>
	<link type="text/css" rel="stylesheet" href="<@s.url value="/style/pageStyles/projects.css"/>"/>
</head>
<#include "/templates/html/common/_formErrors.ftl" />

<div class="twoColumn" >
	<div class="infoSet">
		<label for="projectID"><@s.text name="indicator.required"/> <@s.text name="label.projectid"/></label>
		<@s.textfield name="projectID" required="true"/>
	</div>
	
	<div class="infoSet">
		<label for="name"><@s.text name="indicator.required"/> <@s.text name="label.title"/></label>
		<@s.textfield name="name" required="true"/>
	</div>
	
	<#if securityGuard.jobSitesEnabled>
		<div class="infoSet">
			<label for="jobSite"><@s.text name="indicator.required"/> <@s.text name="label.jobsite"/> </label>
			<@s.select name="jobSite" id="jobSite" list="jobSites" listKey="id" listValue="name" onchange="jobSiteChange(this)" />
		</div>
		<div class="infoSet">
			<label for="customer"><@s.text name="label.customer"/> </label>
			<span id="customerName">
				${(project.owner.customerOrg.name?html) !}
				
			</span>
			<@s.hidden name="customer" id="customer" />
		</div>
		
		<div class="infoSet">
			<label for="division"><@s.text name="label.division"/> </label>
			<span id="divisionName">
				${(project.owner.divisionOrg.name?html)!}
			</span>
			<@s.hidden name="division" id="division" />
		</div>
	<#else>
		<div class="infoSet">
			<label for="customer"><@s.text name="label.customer"/> </label>
			<@s.select name="customer" id="customer" list="customers" listKey="id" listValue="name" emptyOption="true" onchange="customerChanged(this)" />
		</div>
		
		<div class="infoSet">
			<label for="division"><@s.text name="label.division"/> </label>
			<@s.select name="division" id="division" list="divisions" listKey="id" listValue="name" emptyOption="true" />
		</div>
	</#if>
	
	<div class="infoSet">
		<label for="status"><@s.text name="label.status"/></label>
		<@s.textfield name="status"/>
	</div>
	
	<div class="infoSet">
		<label for="open"><@s.text name="label.open" /></label>
		<@s.checkbox name="open" />
	</div>		

	<div class="infoSet">
		<label for="description"><@s.text name="label.description" /></label>
		<@s.textarea name="description" />
	</div>
	
	
</div>
<div class="twoColumn" >
	<div class="infoSet">
		<label for="started"><@s.text name="label.datecreated"/></label>
		<span>${action.formatDateTime(project.created)}</span>
	</div>
	<div class="infoSet">
		<label for="started"><@s.text name="label.datestarted"/></label>
		<@s.datetimepicker name="started" type="dateTime"/>
	</div>
	
	<div class="infoSet">
		<label for="estimatedCompletion"><@s.text name="label.estimatedcompletion"/> </label>
		<@s.datetimepicker name="estimatedCompletion" type="dateTime"/>
	</div>
	
	<div class="infoSet">
		<label for="acutalCompletion"><@s.text name="label.actualcompletion"/> </label>
		<@s.datetimepicker name="actualCompletion" type="dateTime"/>
	</div>
		
	<div class="infoSet">
		<label for="duration"><@s.text name="label.duration"/></label>
		<@s.textfield name="duration"/>
	</div>

	<div class="infoSet">
		<label for="poNumber"><@s.text name="label.ponumber"/></label>
		<@s.textfield name="poNumber" />
	</div>

	<div class="infoSet">
		<label for="workPerformed"><@s.text name="label.workperformed" /></label>
		<@s.textarea name="workPerformed" />
	</div>	
</div>