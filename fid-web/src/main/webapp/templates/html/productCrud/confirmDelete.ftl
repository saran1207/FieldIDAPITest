${action.setPageType('product', 'edit')!}
<#if !removalSummary.validToDelete() >
	<div class="formErrors error" >
		<@s.text name="error.productusedonamasterinspection">
			<@s.param>${removalSummary.productUsedInMasterInpsection}</@s.param>
		</@s.text>
	</div>
</#if>

<div class="instructions">
	<@s.text name="instruction.deleteproduct"><@s.param >${asset.serialNumber?html}</@s.param></@s.text>
</div>

<div class="crudForm largeForm bigForm pageSection">
	<h2><@s.text name="label.removaldetails"/></h2>
	<div class="sectionContent">
		
		<div class="infoSet">
			<label for="">${removalSummary.inspectionsToDelete}</label>
			<span><@s.text name="label.inspectionsbeingdeleted"/></span>
		</div>
		
		<div class="infoSet">
			<label for="">${removalSummary.schedulesToDelete}</label>
			<span><@s.text name="label.schedulesbeingdeleted"/></span>
		</div>
		
		<#if removalSummary.detatachFromMaster >
			<div class="infoSet">
				<label for=""></label>
				<span><@s.text name="label.detachfrommaster"/></span>
			</div>
		</#if>
		<div class="infoSet">
			<label for="">${removalSummary.subProductsToDettach}</label>
			<span><@s.text name="label.detachsubproducts"/></span>
		</div>
		
		
		<#if securityGuard.projectsEnabled>
			<div class="infoSet">
				<label for="">${removalSummary.projectToDetachFrom}</label>
				<span><@s.text name="label.productsbeingdetachedfromproject"/></span>
			</div>
		</#if>
		
	</div>
	<div class="formAction">
		<@s.url id="cancelUrl" action="productEdit" uniqueID="${uniqueID}"/>
		<@s.reset key="label.cancel" onclick="return redirect('${cancelUrl}');" theme="fieldid"/>
		
		<@s.url id="deleteUrl" action="productDelete" uniqueID="${uniqueID}"/>
		<@s.submit key="label.delete" onclick="return redirect('${deleteUrl}');" theme="fieldid" >
			<#if !removalSummary.validToDelete() >
				<@s.param name="disabled" value="true"/>
			</#if>
		</@s.submit>	
	</div>
</div>


