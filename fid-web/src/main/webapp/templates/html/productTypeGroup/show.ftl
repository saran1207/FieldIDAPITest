${action.setPageType('product_type_group', 'show')!}
<div class="crudForm bigForm pageSection">
	<h2><@s.text name="label.groupdetails"/> <a href="<@s.url action="productTypeGroupEdit" uniqueID="${group.id}"/>"><@s.text name="label.littleedit"/></a></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label for="name"><@s.text name="label.name"/></label>
			<span>${(group.name?html) !}</span>
		</div>
	</div>
</div>

<div class="pageSection">
	<h2><@s.text name="label.producttypes"/></h2>
	<div class="crudForm bigForm sectionContent">
		
		<#list assetTypes as assetType>
			<div class="infoSet">
				<label class="line" ><a href="<@s.url action="productType" uniqueID="${assetType.id}"/>">${(assetType.name?html) !}</a></label>
			</div>
		</#list>
		<#if assetTypes.empty >
			<div class="infoSet">
				<label class="line"><@s.text name="label.noproducttypesundergroup"/></label>
			</div>
		</#if>	
		<div class="infoSet">
			<label class="line"><a href="<@s.url action="productTypeEdit" group="${group.id}"/>"><@s.text name="label.addnewproducttype"/></a></label>
		</div>
		 
	</div>
</div>

