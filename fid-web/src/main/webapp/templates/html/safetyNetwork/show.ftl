<#include '_safetyNetworkLayout.ftl'>

<div id="safetyNetworkSplash">
	<h1><@s.text name="label.safety_network"/></h1>
	<p id="safetyNetworkDescription">
		<@s.text name="label.safety_network_info.2"/>
	</p>
	
	<p class="videoLinks">
		<a id="help_link" href="${helpUrl}" target="_blank"><@s.text name="label.safety_network_info.help"/></a>
		<a id="video_link" href="${videoUrl}" target="_blank"><@s.text name="label.safety_network_info.video"/></a>
	</p>
	<@s.form action="findConnections" theme="fieldid" cssClass="fullForm">
		<p>
			<span class="splashHeading"><@s.text name="label.find_a_company"/></span><br/>
			<@s.textfield id="companySearchBox"  name="searchText" cssClass="inputAlign"/>
			<@s.submit id="searchForCompanyButton"  key="hbutton.search" cssClass="saveButton save inputAlign"/>
		</p>
	</@s.form>
	<p id="inviteCompany">
		<span class="safetyHeading"><a href="<@s.url action="invite"/>"><@s.text name="label.invite_a_company"/></a></span>
		<a href="<@s.url action="invite"/>"><@s.text name="label.invite_a_company.full"/></a>
	</p>
</div>

