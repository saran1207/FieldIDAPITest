${action.setPageType('safety_network_connections', 'import')!}

<#assign loaderDiv>
	<div class="loading"><img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/></div>
</#assign>
<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/publishedCatalog.css"/>" />
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/steps.css"/>" />
	<script type="text/javascript" src="<@s.url value="/javascript/steps.js"/>"></script>
	<script type="text/javascript">
		var loading_holder = '${loaderDiv?js_string}';
	</script>
</head>

<div id="steps">
	<div class="step">
		<h2>1. <@s.text name="label.catalog_import"/> <#assign tenant=linkedTenant/><#include "../common/_displayTenantLogo.ftl"/></h2>
		<div class="stepContent"  id="step1">
			<@s.form action="importCatalogSelection" namespace="/ajax" theme="fieldid" id="step1Form">
				<@s.hidden name="uniqueID"/>
				<@s.hidden name="usingPackage" id="usingPackage"/>
				<p>
					<@s.text name="label.how_to_import"><@s.param>${linkedTenant.displayName}</@s.param></@s.text>
				</p>
				<div class="stepAction">
					<@s.submit key="label.select_items_to_import" onclick="$('usingPackage').value = 'true'; toStep(2, 'step2Loading');  $('step1Form').request(getStandardCallbacks()); return false;"/>
				</div>
				
				<p class="alternativeOption">
					<@s.text name="label.alt_import_description"/> - <a href="javascript:void(0);"  onclick="$('usingPackage').value = 'false'; toStep(2, 'step2Loading'); $('step1Form').request(getStandardCallbacks()); return false;"><@s.text name="label.customize_import_selection"/></a>
				</p>
			</@s.form>
		</div>
	</div>
	
	<div class="step stepClosed">
		<h2>2. <@s.text name="label.what_do_you_want_to_import"/></h2>
		<div class="stepContent"  id="step2" style="display:none">
		</div>
		<div class="stepContent loader" id="step2Loading" style="display:none">
			${loaderDiv}
			<p>
				<@s.text name="label.retrieving_catalog_details" />
			</p>
		</div>
	</div>
	
	<div class="step stepClosed">
		<h2>3. <@s.text name="label.ready_to_import"/></h2>
		<div class="stepContent" id="step3" style="display:none">
			
		</div>
		<div class="stepContent loader" id="step3Loading" style="display:none">
			${loaderDiv}
			<p>
				<@s.text name="label.confirming_your_catalog_import" />
			</p>
		</div>
	</div>
	<div class="step stepClosed">
		<h2>4. <@s.text name="label.you_are_done"/></h2>
		<div class="stepContent" id="step4" style="display:none">
		</div>
		<div class="stepContent loader" id="step4Loading" style="display:none">
			${loaderDiv}
			
		</div>
	</div>
	
	<@s.url id="cancelUrl" action="safetyNetworkList" />
	
	<div id="cancelButton" class="stepAction" >
		<@s.submit key="label.cancel_import" id="cancel" onclick="return redirect('${cancelUrl}');"/>
	</div>
</div>

<div class="helpfulHints">
	<h3><@s.text name="label.where_do_i_start"/></h3>
	<p><@s.text name="label.where_do_i_start.full"/></p>

	<h3><@s.text name="label.what_will_be_imported"/></h3>
	<p><@s.text name="label.what_will_be_imported.full"/></p>

	<h3><@s.text name="label.will_this_overwrite_my_existing_setup"/></h3>
	<p><@s.text name="label.will_this_overwrite_my_existing_setup.full"/></p>

	<h3><@s.text name="label.how_do_i_know_if_the_import_was_successful"/></h3>
	<p><@s.text name="label.how_do_i_know_if_the_import_was_successful.full"/></p>
</div>




