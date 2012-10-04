<head>
    <@n4.includeStyle href="pageStyles/projectNotes.css"/>
</head>
${action.setPageType('job', 'notes')!}


<#assign secondaryNavAction="list"/>
<#include "_secondaryNav.ftl"/>

<#if page.hasResults() && page.validPage() >
	<#assign currentAction="jobNotes" >
	<#include '../common/_pagination.ftl' >
	<div class="pageSection">
		<h2 class="decoratedHeader">
			<@s.text name="label.notes"/>
		</h2>
		<div class="sectionContent">
			<#list page.list as note > 
				<#include "../projects/_attachedNote.ftl"/>	
			</#list>
		</div>
	</div>
	<#include '../common/_pagination.ftl' >
<#elseif  page.totalResults == 0  >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptynoteslist"/><#if sessionUser.hasAccess( "managejobs" )><@s.text name="label.emptynoteslistinstruction"/></#if>
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2 class="decoratedHeader"><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url action="jobNotes" projectId="${projectId}"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>




