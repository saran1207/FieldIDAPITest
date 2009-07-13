<title><@s.text name="title.inspectionschedule"/> <@s.text name="title.results"/></title>

<#assign listPage=true/>
<#include '_form.ftl' >

<#if validPage >
	<#if hasResults >
		<#assign postRowHeaderTemplate="../schedule/_postRowHeader.ftl" />
		<#assign postRowTemplate="../schedule/_postRow.ftl" />
		<#include '../customizableSearch/table.ftl'>

	
		<div class="adminLink">	
			<span class="total"><@s.text name="label.totalschedules"/> ${totalResults}</span>
		</div>
		<div class="adminLink alternateActions">
			<a href='<@s.url action="scheduleResults.action" namespace="/aHtml" searchId="${searchId}"/>'  class='lightview exportToExcel' rel='ajax' title=' :: :: scrolling:true, autosize: true' ><@s.text name="label.exporttoexcel" /></a>  
			<#if Session.sessionUser.hasAccess('createinspection') >
				| <a href="<@s.url action="massUpdateInspectionSchedule"  searchId="${searchId}" currentPage="${currentPage!}"/>" class="massUpdate"><@s.text name="label.massupdate" /></a>
			</#if>
			<#if securityGuard.projectsEnabled && sessionUser.hasAccess('createinspection') >
				| <a href="<@s.url action="selectJobToAssignTo"  searchId="${searchId}" currentPage="${currentPage!}"/>" class="massUpdate"><@s.text name="label.assigntojob" /></a>
			</#if>
		</div>
	<#else>
		<div class="emptyList" >
			<h2><@s.text name="label.noresults" /></h2>
			<p><@s.text name="message.emptysearch" /></p>
		</div>
	</#if>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url action="scheduleResults" searchId="${searchId!0}"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>

<#include "../customizableSearch/_massActionRestriction.ftl"/>
