<title><@s.text name="title.report"/> <@s.text name="title.results"/>  
	<#if container.fromSavedReport>
		<@s.text name="label.for"/> - ${savedReportName?html} 
		<#if savedReportModified>
			 (<@s.text name="label.modified"/>)
		</#if>
	</#if>
	 
</title>
<head>
	<link rel="stylesheet" type="text/css" href="style/pageStyles/reporting.css" />
</head>
<#assign reportActions>
	<ul class="listOfLinks">
		<li class="first">
			<a href="<@s.url action="report"/>"><@s.text name="label.newreport" /></a>
		</li>
		<#if container.fromSavedReport>
			<@s.url id="saveUrl" action="savedReportEdit" searchId="${searchId}" uniqueID="${container.savedReportId}"/>
		<#else>
			<@s.url id="saveUrl" action="savedReportAdd" searchId="${searchId}"/>
		</#if>
		<li>
			<a href="${saveUrl}" class="saveLink" onclick="if(formChanged) {return false;}"><@s.text name="label.savereport" /></a>
		</li>
		<#if container.fromSavedReport>
			<li>
				<a href="<@s.url action="savedReportAdd" searchId="${searchId}"/>" class="saveLink" onclick="if(formChange) {return false;}"><@s.text name="label.savereportas" /></a>
			</li>
		</#if>
	</ul>
</#assign>	
${reportActions}
<#assign listPage=true/>
<#include "_form.ftl"/>

<#if validPage >
	<#if hasResults >
		<#assign postRowHeaderTemplate="../report/_postRowHeader.ftl" />
		<#assign postRowTemplate="../report/_postRow.ftl" />
		<#include '../customizableSearch/table.ftl'>
	
		<div class="adminLink">	
			<span class="total"><@s.text name="label.totalinspections"/> ${totalResults}</span>
		</div>
		<div class="adminLink alternateActions">
			<#-- The following displays the drop down menu for the print report, all inspections and all observations link -->
			<@s.url id="printReport" action="printReport.action" namespace="/aHtml" searchId="${searchId}"/>
			<@s.url id="printAllInspectionUrl" action="reportPrintAllCerts" namespace="/aHtml" reportType="INSPECTION_CERT" searchId="${searchId}" />
			<@s.url id="printAllobservationUrl" action="reportPrintAllCerts" namespace="/aHtml" reportType="OBSERVATION_CERT" searchId="${searchId}" />
	
			<div id="print_link" class="print printRelative" style="display: inline;" onmouseover="repositionPrintList('print_list', 'print_link');" >
				<ul id="print_list">
					<li><a href='${printReport}'			class='lightview summaryReport' rel='ajax' title=' :: :: scrolling: false, autosize: true' ><@s.text name="label.printreport" /></a></li>
					<li><a href="${printAllInspectionUrl}"	class='lightview printAllPDFs' rel='ajax' title=' :: :: scrolling: false, autosize: true' ><@s.text name="label.printallpdfreports"/></a></li>
					<li><a href="${printAllobservationUrl}"	class='lightview printAllPDFs' rel='ajax' title=' :: :: scrolling: false, autosize: true' ><@s.text name="label.printallobservationcertificate"/></a></li>
				</ul>
				
				<a href="javascript:void(0);" class="pdfPrinting" ><img src="<@s.url value="/images/pdf_small.gif"/>" /> <@s.text name="label.print"/></a>
			</div>
			|
			<a href='<@s.url action="reportResults" namespace="/aHtml" searchId="${searchId}" />' class='lightview exportToExcel' rel='ajax' title=' :: :: scrolling:true, autosize: true' ><@s.text name="label.exporttoexcel" /></a>
			
			<#if Session.sessionUser.hasAccess('editinspection') && !criteria.includeNetworkResults>
				| <a href="<@s.url action="massUpdateInspections"  searchId="${searchId}" currentPage="${currentPage!}"/>" class="massUpdate"><@s.text name="label.massupdate" /></a>
			</#if>
	   		|
			<a id="warning_summaryReport_button" href="<@s.url action="summaryReport" searchId="${searchId}" currentPage="${currentPage!}"/>" class="summaryReport"><@s.text name="label.summaryreport" /></a>
			<#if securityGuard.projectsEnabled && sessionUser.hasAccess('createinspection') && !criteria.includeNetworkResults>
				| <a href="<@s.url action="selectJobToAssignInspectionsTo"  searchId="${searchId}" currentPage="${currentPage!}"/>" class="assignInspectionsToJob"><@s.text name="label.assigntojob" /></a>
			</#if>
			
		</div>
	<#else>
		<div class="emptyList" >
			<h2><@s.text name="label.noresults"/></h2>
			<p>
				<@s.text name="message.emptyreport" />
			</p>
		</div>
	</#if>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage"/></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url action="reportResults" searchId="${searchId!0}"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>
${reportActions}

<script type="text/javascript">
function repositionPrintList(list, link) {
	var list =  $(list);
	translate(list, $(link), -list.getHeight(), 0);
}
	
</script>

<#include "../customizableSearch/_massActionRestriction.ftl"/>
