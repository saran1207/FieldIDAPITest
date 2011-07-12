<title><@s.text name="label.start_event"/>
<head>
	<@n4.includeStyle type="page" href="events"/>
</head>
<script>
	document.observe("dom:loaded", function(){
		$('assetSearchForm_search').focus();
	});
</script>
<#assign multiEventaction= '<@s.url action="assetSelection"/>'/>
<div class="eventRow">
	<div class="eventOption">
		<div class="optionContent">		
			<div class="heading singleEvent"><h2><@s.text name="label.perform_event_single_asset"/></h2></div>
			<p><@s.text name="label.perform_event_single_asset.full"/></p>
		</div>
		<div class="searchAction">
			<#include "../eventGroup/_eventSearchForm.ftl"/>
		</div>
	</div>
	
	<div class="eventOption">
		<div class="optionContent">
			<div class="heading multipleEvents"><h2><@s.text name="label.multi_event"/></h2></div>
			<p>
				<@s.text name="label.multi_event.full"><@s.param>${maxAssetsFromMassEvent!250}</@s.param></@s.text>
			</p>
		</div>
		<div class="eventAction">	
			<button onclick="redirect('<@s.url action="assetSelection"/>')" ><@s.text name="label.select_your_assets_now"/></button>
		</div>
	</div>
</div>
<div class="eventRow">
	<div class="eventOption">
		<div class="optionContent">
			<div class="heading import"><h2><@s.text name="label.import"/></h2></div>
			<p>
				<@s.text name="message.import_events"/>
			</p>			
		</div>
		<div class="eventAction">	
			<button onclick="redirect('<@s.url action="eventImportExport"/>')" ><@s.text name="label.start_now"/></button>
		</div>
	</div>

	<#if securityGuard.proofTestIntegrationEnabled>
		<div class="eventOption">
			<div class="optionContent">
				<div class="heading multipleProofTests"><h2><@s.text name="label.multi_proof_test"/></h2></div>
				<p>
					<@s.text name="label.multi_proof_test.full"><@s.param>${maxAssetsFromMassEvent!250}</@s.param></@s.text>
				</p>
			</div>
			<div class="eventAction">	
				<button onclick="redirect('<@s.url action="multiProofTest"/>')"><@s.text name="label.start_now"/></button>
			</div>
		</div>
	</#if>
</div>
