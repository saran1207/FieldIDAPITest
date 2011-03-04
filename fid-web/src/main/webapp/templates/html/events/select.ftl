<title><@s.text name="label.start_event"/>
<head>
	<@n4.includeStyle type="page" href="events"/>
</head>
<#assign multiEventaction= '<@s.url action="assetSelection"/>'/>
<div class="eventList">
	<fieldset>
		<legend id="singleEvent">
			<span class="heading"><@s.text name="label.perform_event_single_asset"/></span>
		</legend>
		<p><@s.text name="label.perform_event_single_asset.full"/></p>
		<div class="searchAction">
			<#include "../eventGroup/_eventSearchForm.ftl"/>
		</div>
	</fieldset>
	
	<#if sessionUser.hasAccess("createevent") == true >
		<fieldset>
			<legend id="multipleEvents"><span class="heading"><@s.text name="label.multi_event"/></span></legend>
			<p>
				<@s.text name="label.multi_event.full"><@s.param>${maxAssetsFromMassEvent!250}</@s.param></@s.text><br/><br/>
				<button onclick="redirect('<@s.url action="assetSelection"/>')" ><@s.text name="label.select_your_assets_now"/></button>
			</p>
		</fieldset>
		<#if securityGuard.proofTestIntegrationEnabled>
			<fieldset>
				<legend id="multipleProofTests"><span class="heading"><@s.text name="label.multi_proof_test"/></span></legend>
				<p>
					<@s.text name="label.multi_proof_test.full"><@s.param>${maxAssetsFromMassEvent!250}</@s.param></@s.text><br/><br/>
					<button onclick="redirect('<@s.url action="multiProofTest"/>')"><@s.text name="label.start_now"/></button>
				</p>
			</fieldset>
		</#if>
	</#if>
</div>
