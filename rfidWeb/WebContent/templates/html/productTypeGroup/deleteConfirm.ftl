<title><@s.text name="title.confirmproducttypegroupdelete" /> - ${group.name?html}</title>
${action.setPageType('product_type_group', 'edit')!}
<div class="instructions">
	<@s.text name="instruction.deleteproducttypegroup"><@s.param >${group.name?html}</@s.param></@s.text>
</div>

<div class="crudForm largeForm bigForm pageSection">
	<h2><@s.text name="label.removaldetails"/></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label for="">${removalSummary.productTypesConnected}</label>
			<span><@s.text name="label.producttypesbeingdetached"/></span>
		</div>
	</div>
	<div class="formAction">
		<@s.url id="cancelUrl" action="productTypeGroup" uniqueID="${uniqueID}"/>
		<@s.submit key="label.cancel" onclick="return redirect('${cancelUrl}');" theme="fieldid"/>
		
		<@s.url id="deleteUrl" action="productTypeGroupDelete" uniqueID="${uniqueID}"/>
		<@s.submit key="label.delete" onclick="return redirect('${deleteUrl}');" theme="fieldid" />
	</div>
</div>

