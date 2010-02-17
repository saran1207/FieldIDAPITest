<title><@s.text name="title.assetsearch"/> <@s.text name="title.results"/></title>

<#assign listPage=true/>
<#include '_form.ftl' >

<#if validPage >
	<#if hasResults >
		<#assign postRowHeaderTemplate="../search/_postRowHeader.ftl" />
		<#assign postRowTemplate="../search/_postRow.ftl" />
		<#include '../customizableSearch/table.ftl'>
		
		<div class="adminLink">	
			<span class="total"><@s.text name="label.totalproducts"/> ${totalResults}</span>
		</div>
		<div class="adminLink alternateActions">
			<a href="<@s.url action="searchPrintAllCerts" namespace="/aHtml" searchId="${searchId}"/>" class='lightview printAllPDFs' rel='ajax' title=' :: :: scrolling:true, autosize: true' ><img src="<@s.url value="/images/pdf_small.gif"/>" /> <@s.text name="label.printallmanufacturercertificate"/></a>
			| 		
			<a href='<@s.url action="searchResults" namespace="/aHtml" searchId="${searchId}"/>'  class='lightview exportToExcel' rel='ajax' title=' :: :: scrolling:true, autosize: true' ><@s.text name="label.exporttoexcel" /></a> 
			<#if Session.sessionUser.hasAccess('tag') >
				| <a href="<@s.url action="massUpdateProducts" searchId="${searchId}" currentPage="${currentPage!}"/>" class="massUpdate"><@s.text name="label.massupdate" /></a>
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
			<a href="<@s.url includeParams="none" action="searchResults" searchId="${searchId!0}"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>
<#include "../customizableSearch/_massActionRestriction.ftl"/>
