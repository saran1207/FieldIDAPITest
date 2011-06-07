<#if eventSchedule.project?exists>
	<a href="<@s.url action="job" namespace="/" uniqueID="${eventSchedule.project.id}" />"><@s.text name="${eventSchedule.project.name}" /></a>
<#else>
	&nbsp;
</#if>