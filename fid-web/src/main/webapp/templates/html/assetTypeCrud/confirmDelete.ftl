<title><@s.text name="title.confirmassettypedelete" /> - ${assetType.name?html}</title>
${action.setPageType('asset_type', 'edit')!}
<#if !removalSummary.validToDelete() >
	<div class="formErrors error" >
		<@s.text name="error.assetsusedonamasterevent">
			<@s.param>${assetType.name?html}</@s.param>
			<@s.param>${removalSummary.assetsUsedInMasterEvent}</@s.param>
		</@s.text>
	</div>
</#if>

<div class="instructions">
	<@s.text name="instruction.deleteassettype"><@s.param >${assetType.name?html}</@s.param></@s.text>
</div>

<div class="crudForm largeForm bigForm pageSection">
	<h2><@s.text name="label.removaldetails"/></h2>
	<div class="sectionContent">
		
		<div class="infoSet">
			<label for="">${removalSummary.assetsToDelete}</label>
			<span><@s.text name="label.assetsbeingdeleted"/></span>
		</div>
		
		<div class="infoSet">
			<label for="">${removalSummary.eventsToDelete}</label>
			<span><@s.text name="label.eventsbeingdeleted"/></span>
		</div>
		
		<div class="infoSet">
			<label for="">${removalSummary.schedulesToDelete}</label>
			<span><@s.text name="label.schedulesbeingdeleted"/></span>
		</div>
		<#if securityGuard.integrationEnabled>
			<div class="infoSet">
				<label for="">${removalSummary.assetCodeMappingsToDelete}</label>
				<span><@s.text name="label.assetcodemappingsbeingdeleted"/></span>
			</div>
		</#if>
	
		<div class="infoSet">
			<label for="">${removalSummary.assetTypesToDetachFrom}</label>
			<span><@s.text name="label.detachedfromassettypes"/></span>
		</div>
		
		<div class="infoSet">
			<label for="">${removalSummary.masterAssetsToDetach}</label>
			<span><@s.text name="label.assetsbeingdetachedfrommaster"/></span>
		</div>
		
		<div class="infoSet">
			<label for="">${removalSummary.subAssetsToDetach}</label>
			<span><@s.text name="label.subassetsbeingdetached"/></span>
		</div>
		
		
		
		<#if securityGuard.projectsEnabled>
			<div class="infoSet">
				<label for="">${removalSummary.assetsToDetachFromProjects}</label>
				<span><@s.text name="label.assetsbeingdetachedfromproject"/></span>
			</div>
		</#if>
		
	</div>
	<div class="formAction">
		<@s.url id="cancelUrl" action="assetTypeEdit" uniqueID="${uniqueID}"/>
		<@s.submit key="label.cancel" onclick="return redirect('${cancelUrl}');" theme="fieldid"/>
		
		<@s.url id="deleteUrl" action="assetTypeDelete" uniqueID="${uniqueID}"/>
		<@s.submit key="label.delete" onclick="return redirect('${deleteUrl}');" theme="fieldid" >
			<#if !removalSummary.validToDelete() >
				<@s.param name="disabled" value="true"/>
			</#if>
		</@s.submit>	
	</div>
</div>


