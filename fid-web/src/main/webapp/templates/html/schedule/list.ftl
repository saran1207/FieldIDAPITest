<title><@s.text name="title.schedulesearch"/> <@s.text name="title.results"/></title>
<head>
	<@n4.includeStyle href="pageStyles/schedules"/>
    <script type="text/javascript">

        jQuery(document).ready(function(){
            jQuery('.eventLightbox').colorbox({maxHeight: '600px', width: '600px'});
            jQuery('.exportToExcelLightbox').colorbox({scrolling: true});
        });

    </script>
</head>

<#assign listPage=true/>
<#include '_form.ftl' >

<#if validPage >
	<#if hasResults >
		<#assign postRowHeaderTemplate="../schedule/_postRowHeader.ftl" />
		<#assign postRowTemplate="../schedule/_postRow.ftl" />
		<#include '../customizableSearch/table.ftl'>

	
		<div class="adminLink">	
			<span class="total"><@s.text name="label.totalschedules"/> ${totalResults}</span> (<span id="numSelectedItems">${numSelectedItems}</span> selected)
		</div>
		<div class="adminLink alternateActions">
			<a href='<@s.url action="scheduleResults.action" namespace="/aHtml" searchId="${searchId}"/>' class="exportToExcelLightbox"> <@s.text name="label.exporttoexcel" /></a>
			<#if sessionUser.hasAccess('createevent') >
				| <a href="<@s.url action="massUpdateEventSchedule"  searchId="${searchId}" currentPage="${currentPage!}"/>" class="massUpdate"><@s.text name="label.massupdate" /></a>
			</#if>
			<#if securityGuard.projectsEnabled && sessionUser.hasAccess('createevent') >
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
