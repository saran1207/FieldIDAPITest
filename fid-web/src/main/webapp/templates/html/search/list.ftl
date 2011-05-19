<title><@s.text name="title.assetsearch"/> <@s.text name="title.results"/></title>
<head>
	<@n4.includeStyle href="pageStyles/search"/>
</head>
<#assign listPage=true/>

<#if validPage >
	<#if hasResults >
		<#include '_form.ftl' >
		<#assign postRowHeaderTemplate="../search/_postRowHeader.ftl" />
		<#assign postRowTemplate="../search/_postRow.ftl" />
		<#include '../customizableSearch/table.ftl'>
		
		<div class="adminLink">	
			<span class="total"><@s.text name="label.totalassets"/> ${totalResults}</span> (<span id="numSelectedItems">${numSelectedItems}</span> selected)
		</div>
		<div class="adminLink alternateActions">
			<#if securityGuard.manufacturerCertificateEnabled>
				<a href="<@s.url action="searchPrintAllCerts" namespace="/aHtml" searchId="${searchId}"/>" class='lightview printAllPDFs' rel='ajax' title=' :: :: scrolling:true, autosize: true' ><img src="<@s.url value="/images/pdf_small.gif"/>" /> <@s.text name="label.printselectedmanufacturercertificate"/></a>
				| 		
			</#if>
			<a href='<@s.url action="searchResults" namespace="/aHtml" searchId="${searchId}"/>'  class='lightview exportToExcel' rel='ajax' title=' :: :: scrolling:true, autosize: true' ><@s.text name="label.exporttoexcel" /></a> 
			<#if sessionUser.hasAccess('tag') >
				| <a href="<@s.url action="massUpdateAssets" searchId="${searchId}" currentPage="${currentPage!}"/>" class="massUpdate"><@s.text name="label.massupdate" /></a>
			</#if>
			<#if sessionUser.hasAccess('createevent') >
				| <a href="#multiEvent" name="multiEvent" id="multiEvent" class="multiEvent"><@s.text name="label.preform_mass_event"/></a>
			</#if>
		</div>
	<#else>
		<div class="initialMessage" >
			<div class="textContainer">
				<h2><@s.text name="label.no_assets" /></h2>
				<p><@s.text name="message.no_assets" /></p>
			</div>
			<div class="identifyActions">
				<input type="button" onClick="location.href='<@s.url action="assetAdd"/>'" value="<@s.text name='label.identify_first_asset' />" />
				<@s.text name="label.or" />
				<a href="<@s.url action="assetMultiAdd"/>"><@s.text name="label.mass_identify_first_asset" /></a>
				<@s.text name="label.or" />
				<a href="<@s.url value="w/setup/import"/>"><@s.text name="label.import_from_excel" /></a>
			</div>
		</div>
	</#if>
<#else>
	<#include '_form.ftl' >
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url includeParams="none" action="searchResults" searchId="${searchId!0}"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>
<#include "../customizableSearch/_massActionRestriction.ftl"/>

<@s.form id="performMultiEvent" action="selectEventType" namespace="/multiEvent">
    <@s.hidden name="searchContainerKey" value="${searchContainerKey}"/>
    <@s.hidden name="searchId" value="${searchId}"/>
</@s.form>

<@n4.includeScript>
    onDocumentLoad(function() {
        $$('#multiEvent').each(function(element) {
            element.observe('click', function(event) {
                if (numSelectedItems() > 0 && numSelectedItems() <= maxSizeForMultiEvent) {
                    event.stop();
                    $('performMultiEvent').submit();
                }
            });
        });
    });
</@n4.includeScript>

