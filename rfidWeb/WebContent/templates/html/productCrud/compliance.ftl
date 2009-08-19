<head>
	<@n4.includeStyle type="page" href="product"/>
</head>
	
${action.setPageType('product', 'compliancecheck')!}
<h2><@s.text name="label.compliance.productcheck"/></h2>

<#if complianceCheck?exists >
	<#if complianceCheck >
		<div id="compliancePassed" class="viewSection complianceMessage">
			<p>
				<label><@s.text name="label.compliance.productcheck"/></label>
				<span ><strong class="pass"><@s.text name="label.passed"/></strong>: <@s.text name="message.compliance.productpassed"/></span>
			</p>
			<p>
				<label><@s.text name="label.riskofnoncompliance"/></label>
				<span >${riskPercent?string.percent}</span>
			</p>
			<p>
				<label><@s.text name="label.daystoincompliance"/></label>
				<span >${risk.schedule.daysToDue}</span>
			</p>
			<p>
				<label><@s.text name="label.nextscheduledinspection"/></label>
				<span ><a href="<@s.url action="inspectionScheduleList" includeParams="none" productId="${uniqueID}"/>">${risk.schedule.inspectionType.name}</a> <@s.text name="label.on" /> ${action.formatDate(risk.schedule.nextDate, false)}</span>
			</p>
		</div>
	<#else>
		<div id="complianceFailed" class="viewSection complianceMessage">
			<p>
				<label><@s.text name="label.compliance.productcheck"/></label>
				<span ><strong class="fail"><@s.text name="label.failed"/></strong>: <@s.text name="message.compliance.productfailed"/></span>
			</p>
			<#list availableSchedules as schedule >
				<#if schedule.pastDue >
					<p>
						<label><@s.text name="label.compliance.overdueevent"/></label>
						<span ><a href="<@s.url action="inspectionScheduleList" includeParams="none" productId="${uniqueID}"/>">${schedule.inspectionType.name}</a> <@s.text name="label.wasdue"/> ${action.formatDate(schedule.nextDate, false)}</span> 
					</p>
				</#if>
			</#list>
			<p>
				<label><@s.text name="label.daysincompliant"/></label>
				<span >${risk.schedule.daysPastDue}</span> 
			</p>
		</div>
	</#if>
<#else>
	<div id="noCompliance" class="viewSection complianceMessage">
		<p>
			<@s.text name="message.compliance.productunknown"/>
			<@s.text name="label.click"/>
			<a href="<@s.url action="inspectionScheduleList" includeParams="none" productId="${product.uniqueID}"/>"><@s.text name="label.here"/></a>
			<@s.text name="label.toaddinspectionschedules"/>
		</p>
	</div>
</#if>



<#if complianceCheck?exists >
	<h2><@s.text name="label.riskofnoncompliance"/></h2>
	<div id="complianceGraph">
		<OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase=https://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" width="800" height="300" id="Column3D" >
		     <param name="movie" value="/fieldid/flash/charts/FCF_MSArea2D.swf" />
		     <param name="FlashVars" value="&dataURL=xml/productComplianceRisk.action%3funiqueID%3d${uniqueID}&chartWidth=800&chartHeight=300">
		     <param name="quality" value="high" />
		     <embed src="/fieldid/flash/charts/FCF_MSArea2D.swf" flashVars="&dataURL=xml/productComplianceRisk.action%3funiqueID%3d${uniqueID}&chartWidth=800&chartHeight=300" quality="high" width="800" height="300" name="Column3D" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
		  </object>
	
	</div>
	<div id="complianceLegend">
		
		<table class="legend">
			<tr>
				<td colspan="2"><h3><@s.text name="label.legend"/></h3></td>
			</tr>
			<tr>	
				<th><@s.text name="label.past"/></th>
				<td style="background-color: #333333; opacity:0.5; filter: alpha(opacity=50);"></td>
			</tr>
			<tr>	
				<th><@s.text name="label.today"/></th>
				<td style="background-color: #99FF99; opracity:0.5; filter: alpha(opacity=50);"></td>
			</tr>
			<tr>	
				<th><@s.text name="label.compliant"/></th>
				<td style="background-color: #00DD00; opacity:0.5; filter: alpha(opacity=50);"></td>
			</tr>
			<tr>	
				<th><@s.text name="label.nextinspectiondate"/></th>
				<td style="background-color: #DDDD00; opacity:0.5; filter: alpha(opacity=50);"></td>
			</tr>
			<tr>	
				<th><@s.text name="label.incompliant"/></th>
				<td style="background-color: #DD0000; opacity:0.5; filter: alpha(opacity=50);"></td>
			</tr>
		</table>
	</div>
</#if>






