<title>
	<@s.text name="title.report" />
</title>
<head>
	<style type="text/css">
		#savedReportList {
			border-spacing:0;
			border-bottom: none;
		}
		#savedReportList td {
			padding: 10px 5px;
			width:auto;
			vertical-align: top;
		}
	</style>
</head>

<div class="box rightBox">
	<h2>
		<@s.text name="label.savedreports"/> <a href="<@s.url action="savedReports"/>"><@s.text name="label.more"/></a>
	</h2>
	<div class="boxContent" id="savedReports" >
		<table id="savedReportList" class="list">
		<#if !savedReports.empty>
			<#list savedReports as report>
				<tr class="savedReport" id="savedReport_${report.id}">
					<td><a href="<@s.url action="savedReportLoad" uniqueID="${report.id}"/>"><@s.text name="label.run"/></a></td>
					<td>${report.name?html}</td>
				</tr>
			</#list>
		<#else>
			<tr>
				<td>
					<@s.text name="label.emptylistsavedreports"/>
				</td>
			</tr>
		</#if>
		</table>
	</div>
</div>
<#include "_form.ftl"/>
