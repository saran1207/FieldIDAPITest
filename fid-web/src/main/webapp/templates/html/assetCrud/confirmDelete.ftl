${action.setPageType('asset', 'edit')!}
<#if !removalSummary.validToDelete() >
	<div class="formErrors error" >
		<@s.text name="error.assetusedonamasterevent">
			<@s.param>${removalSummary.assetUsedInMasterEvent}</@s.param>
		</@s.text>
	</div>
</#if>

<div class="instructions">
	<@s.text name="instruction.deleteasset"><@s.param >${asset.identifier?html}</@s.param></@s.text>
</div>

<div class="crudForm largeForm bigForm pageSection">
	<h2><@s.text name="label.removaldetails"/></h2>
	<div class="sectionContent">
		
		<div class="infoSet">
			<label for="">${removalSummary.eventsToDelete}</label>
			<span><@s.text name="label.eventsbeingdeleted"/></span>
		</div>
		
		<div class="infoSet">
			<label for="">${removalSummary.schedulesToDelete}</label>
			<span><@s.text name="label.schedulesbeingdeleted"/></span>
		</div>
		
		<#if removalSummary.detachFromMaster >
			<div class="infoSet">
				<label for=""></label>
				<span><@s.text name="label.detachfrommaster"/></span>
			</div>
		</#if>
		<div class="infoSet">
			<label for="">${removalSummary.subAssetsToDetach}</label>
			<span><@s.text name="label.detachsubassets"/></span>
		</div>
		
		
		<#if securityGuard.projectsEnabled>
			<div class="infoSet">
				<label for="">${removalSummary.projectToDetachFrom}</label>
				<span><@s.text name="label.assetsbeingdetachedfromproject"/></span>
			</div>
		</#if>

    <#if asset.type.hasProcedures()>
        <div class="infoSet">
            <label>${removalSummary.procedureDefinitionsToDelete}</label>
            <span><@s.text name="label.procedure_definitions_to_be_deleted"/></span>
        </div>
        <div class="infoSet">
            <label>${removalSummary.proceduresToDelete}</label>
            <span><@s.text name="label.procedures_to_be_deleted"/></span>
        </div>
    </#if>
		
	</div>
	<div class="formAction">
		<@s.url id="deleteUrl" action="assetDelete" uniqueID="${uniqueID}"/>
		
		<@s.submit key="label.delete" onclick="return redirect('${deleteUrl}');" theme="fieldid" >
			<#if !removalSummary.validToDelete() >
				<@s.param name="disabled" value="true"/>
			</#if>
		</@s.submit>	
		
		<@s.text name="label.or"/>
		
		<a href="#" onclick="return redirect('/fieldid/w/identify?id=${uniqueID}');"><@s.text name="label.cancel"/></a>
	</div>
</div>


