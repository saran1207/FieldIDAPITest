<div class="jobResource " id="resource_${user.id}">
	<span class="jobResourceName">${user.userLabel?html}&nbsp;</span>
	<span class="jobResourcePosition">${(user.position?html)!}&nbsp;</span>
	<#if sessionUser.hasAccess("managejobs")>
		<span class="jobResourceRemove"><a href="javascript:void(0);" onclick="getResponse('<@s.url action="jobResourceDelete" namespace="/ajax" jobId="${project.id}" uniqueID="${user.id}"/>'); return false;" class="removeUserLink"><img alt="x" src="<@s.url value="/images/x.gif"/>"/></a></span>
	</#if>
</div>