<head>
	<link type="text/css" rel="stylesheet" href="<@s.url value="/style/pageStyles/projectAssets.css"/>"/>
</head>
${action.setPageType('job','assets')!}

<#assign secondaryNavAction="list"/>
<#include "_secondaryNav.ftl"/>

<#if page.hasResults() && page.validPage() >
	<#assign currentAction="jobAssets" >
	<#include '../common/_pagination.ftl' >
	<div class="pageSection">
		<h2>
			<@s.text name="label.assetsonproject"/>
		</h2>
		<div class="sectionContent">
			<#list page.list as asset > 
				<#include "../projects/_attachedAsset.ftl"/>
			</#list>
		</div>
	</div>
	<#include '../common/_pagination.ftl' >
<#elseif  page.totalResults == 0  >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyprojectassetslist"/><#if sessionUser.hasAccess( "managejobs" )> <@s.text name="label.emptyprojectassetslistinstruction"/></#if>
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="jobAssets" projectId="${projectId}"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>


