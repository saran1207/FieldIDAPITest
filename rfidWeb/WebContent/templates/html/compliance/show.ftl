<title><@s.text name="title.compliancecheck"/></title>

<h2><@s.text name="label.compliance.sitecheck"/></h2>



<#if complianceCheck.hasComplianceValue() >
	<#if complianceCheck.fullyCompliant >
		<div id="compliancePassed" class="viewSection complianceMessage">
			<p>
				<label><@s.text name="label.compliance.sitecheck"/></label>
				<span ><strong class="pass"><@s.text name="label.passed"/></strong>: <@s.text name="message.compliance.sitepassmessage"/></span>
			</p>
			<p>
				<label><@s.text name="label.compliance.result"/></label>
				<span ><@s.text name="message.compliance.score"><@s.param name="value">${complianceCheck.percentageCompliant()?string.percent}</@s.param></@s.text></span> 
			</p>
			<p>
				<label><@s.text name="label.compliance.information"/></label>
				<span ><@s.text name="message.compliance.assets" ><@s.param name="value">${complianceCheck.totalAssets}</@s.param></@s.text></span >
			</p>
		</div>
	<#else>
		<div id="complianceFailed" class="viewSection complianceMessage">
			<p>
				<label><@s.text name="label.compliance.sitecheck"/></label>
				<span ><strong class="fail"><@s.text name="label.failed"/></strong>: <@s.text name="message.compliance.sitefailmessage"/></span>
			</p>
			<p>
				<label><@s.text name="label.compliance.result"/></label>
				<span ><@s.text name="message.compliance.score"><@s.param name="value">${complianceCheck.percentageCompliant()?string.percent}</@s.param></@s.text></span> 
			</p>
			<p>
				<label><@s.text name="label.compliance.reason"/></label>
				<span >
					<@s.text name="message.compliance.passeddueevents"><@s.param name="value">${complianceCheck.outOfCompliance}</@s.param></@s.text>
					<@s.text name="label.click"/> 
					<a href="schedule!createSearch.action?toDate=${action.formatDate(yesterday, true)}"><@s.text name="label.here"/></a>
					<@s.text name="label.toview" /> 
				</span> 
			</p>
			<p>
				<label><@s.text name="label.compliance.information"/></label>
				<span ><@s.text name="message.compliance.assets" ><@s.param name="value">${complianceCheck.totalAssets}</@s.param></@s.text></span >
			</p>
		</div>
	</#if>
<#else>
	<div id="noCompliance" class="viewSection complianceMessage">
		<p>
			<@s.text name="message.compliance.siteunknown"/>
		</p>
	</div>
</#if>
			



