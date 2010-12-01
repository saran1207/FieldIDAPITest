<title><@s.text name="title.massupdateassets" /></title>

<div class="instructions">
	<@s.text name="instruction.delete_multiple_assets"><@s.param >${removalSummaries.size()}</@s.param></@s.text>
</div>

<#if !aggregateRemovalSummary.validToDelete() >
	<div class="formErrors error" >
		<@s.text name="error.mass_delete_asset_used_on_master_event">
			<@s.param>${aggregateRemovalSummary.assetUsedInMasterEvent}</@s.param>
		</@s.text>
		<ul class="borderLess">
			<li>&nbsp;</li>
			<#list removalSummaries as removalSummary>
				<#if !removalSummary.validToDelete()>
					<li>${removalSummary.asset.serialNumber}</li>
				</#if>
			</#list>
		</ul>
	</div>
</#if>


<div class="crudForm largeForm bigForm pageSection">
	<h2><@s.text name="label.removaldetails"/></h2>
	<div class="sectionContent">
		
		<div class="infoSet">
			<label for="">${aggregateRemovalSummary.eventsToDelete}</label>
			<span><@s.text name="label.eventsbeingdeleted"/></span>
		</div>
		
		<div class="infoSet">
			<label for="">${aggregateRemovalSummary.schedulesToDelete}</label>
			<span><@s.text name="label.schedulesbeingdeleted"/></span>
		</div>
		
		<#if aggregateRemovalSummary.detachFromMaster >
			<div class="infoSet">
				<label for=""></label>
				<span><@s.text name="label.detachfrommaster"/></span>
			</div>
		</#if>
		<div class="infoSet">
			<label for="">${aggregateRemovalSummary.subAssetsToDetach}</label>
			<span><@s.text name="label.detachsubassets"/></span>
		</div>
		
		
		<#if securityGuard.projectsEnabled>
			<div class="infoSet">
				<label for="">${aggregateRemovalSummary.projectToDetachFrom}</label>
				<span><@s.text name="label.assetsbeingdetachedfromproject"/></span>
			</div>
		</#if>
		
	</div>
	<div class="formAction">
		<@s.url id="cancelUrl" action="massUpdateAssets" searchId="${searchId}" currentPage="${currentPage}"/>
		<@s.url id="deleteUrl" action="massAssetDelete" searchId="${searchId}"/>
		
		<@s.submit key="label.delete" onclick="return redirect('${deleteUrl}');" theme="fieldid" >
			<#if !aggregateRemovalSummary.validToDelete() >
				<@s.param name="disabled" value="true"/>
			</#if>
		</@s.submit>	
		
		<@s.text name="label.or"/>
		
		<a href="#" onclick="return redirect('${cancelUrl}');"><@s.text name="label.cancel"/></a>
	</div>
</div>


