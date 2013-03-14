${action.setPageType('asset_type_group', 'show')!}
<div class="crudForm bigForm pageSection">
	<h2><@s.text name="label.groupdetails"/> <a href="<@s.url action="assetTypeGroupEdit" uniqueID="${group.id}"/>"><@s.text name="label.littleedit"/></a></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label for="name"><@s.text name="label.name"/></label>
			<span>${(group.name?html) !}</span>
		</div>
        <#if securityGuard.lotoProceduresEnabled>
            <div class="infoSet">
                <label><@s.text name="label.loto_device"/></label>
                <span class="fieldValue">${group.lotoDevice?string("Yes", "No")}</span>
            </div>
        </#if>
    </div>
</div>

<div class="pageSection">
	<h2 class="decoratedHeader"><@s.text name="label.assettypes"/></h2>
	<div class="crudForm bigForm sectionContent">
		
		<#list assetTypes as assetType>
			<div class="infoSet">
				<label class="line" ><a href="<@s.url action="assetType" uniqueID="${assetType.id}"/>">${(assetType.name?html) !}</a></label>
			</div>
		</#list>
		<#if assetTypes.empty >
			<div class="infoSet">
				<label class="line"><@s.text name="label.noassettypesundergroup"/></label>
			</div>
		</#if>	
		<div class="infoSet">
			<label class="line"><a href="<@s.url action="assetTypeEdit" group="${group.id}"/>"><@s.text name="label.addnewassettype"/></a></label>
		</div>
		 
	</div>
</div>

