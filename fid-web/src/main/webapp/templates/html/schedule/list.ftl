<title><@s.text name="title.schedulesearch"/> <@s.text name="title.results"/></title>
<head>
	<@n4.includeStyle href="pageStyles/schedules"/>
</head>

<#assign listPage=true/>

<#if validPage >
	<#if hasResults >
		<#include '_form.ftl' >
		<#assign postRowHeaderTemplate="../schedule/_postRowHeader.ftl" />
		<#assign postRowTemplate="../schedule/_postRow.ftl" />
		<#include '../customizableSearch/table.ftl'>

	
		<div class="adminLink">	
			<span class="total"><@s.text name="label.totalschedules"/> ${totalResults}</span> (<span id="numSelectedItems">${numSelectedItems}</span> selected)
		</div>
		<div class="adminLink alternateActions">
			<a href='<@s.url action="scheduleResults.action" namespace="/aHtml" searchId="${searchId}"/>'  class='lightview exportToExcel' rel='ajax' title=' :: :: scrolling:true, autosize: true' ><@s.text name="label.exporttoexcel" /></a>  
			<#if sessionUser.hasAccess('createevent') >
				| <a href="<@s.url action="massUpdateEventSchedule"  searchId="${searchId}" currentPage="${currentPage!}"/>" class="massUpdate"><@s.text name="label.massupdate" /></a>
			</#if>
			<#if securityGuard.projectsEnabled && sessionUser.hasAccess('createevent') >
				| <a href="<@s.url action="selectJobToAssignTo"  searchId="${searchId}" currentPage="${currentPage!}"/>" class="massUpdate"><@s.text name="label.assigntojob" /></a>
			</#if>
		</div>
	<#else>
		<div class="initialMessage" >
			<div class="textContainer">
				<h2><@s.text name="label.no_schedules" /></h2>
				<p><@s.text name="message.no_schedules" /></p>
			</div>
			<div class="identifyActions">
				<input type="button" onClick="location.href='<@s.url action="assetAdd"/>'" value="<@s.text name='label.identify_an_asset' />" />
			</div>
		</div>
	</#if>
<#else>
	<#include '_form.ftl' >
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url action="scheduleResults" searchId="${searchId!0}"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>

<#include "../customizableSearch/_massActionRestriction.ftl"/>
