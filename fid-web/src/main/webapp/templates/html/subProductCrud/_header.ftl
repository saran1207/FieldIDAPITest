
<div class="definition" id="subProductDefinition_${subProduct.product.id}">
	<div class="identifier">
		<div class="drag" style="display:none;" ><img src="<@s.url value="/images/drag.gif"/>" alt="<@s.text name="label.drag"/>"/></div>
		<div class="productType">${(subProduct.product.type.name?html)!}</div>  
		<div class="subProductLabel ">${(subProduct.label?html)!} <a href="#change" class="notAllowedDuringOrdering" onclick="$('subProductDefinition_${subProduct.product.id}').hide(); $('subProductLabelForm_${subProduct.product.id}').show(); return false;"><@s.text name="label.change_label"/></a></div>
	</div>
	<div class="subProductActions">
		<#if inInspection?exists>
			<div id="subInspection_${subProduct.product.id}" class="subProductAction inspectionTypes inspectionTypesMiddle simpleContainer" onmouseover="positionDropdown(this);">
				<a class="exitLink" href="javascript:void(0);"  ><@s.text name="label.start_event"/></a>
				<ul id="subInspection_${subProduct.product.id}_list" >
					<#assign inspectionTypes=subProduct.product.type.inspectionTypes  />
					<#assign subInspection=true/>
					<#include "/templates/html/inspectionGroup/_inspectionSelect.ftl" />
				</ul>
			</div>
		</#if>
	
		<div class="subProductAction removeSubProduct">
			<a href="#removeSubProduct" id="removeSubProduct_${subProduct.product.id}" onclick="removeSubProduct( '${subProduct.product.id}' ); return false;"><img src="<@s.url value="/images/x.gif"/>" alt="<@s.text name="label.remove"/>"/></a>
		</div>
	</div>
</div>


<div class="labelForm" style="display:none" id="subProductLabelForm_${subProduct.product.id}">
	<@s.form action="${updateSubProductAction!'updateSubProduct'}" id="subProductForm_${subProduct.product.id}" namespace="/ajax" theme="fieldid">
		<@s.hidden name="subProduct.product.type.name" value="${subProduct.product.type.name!}" />
		<@s.hidden name="subProduct.product.iD" value="${subProduct.product.id}"/>
		<@s.hidden name="subProduct.product.serialNumber" value="${subProduct.product.serialNumber!}"/>
		<@s.hidden name="uniqueID" value="${uniqueID}"/>
		<@s.hidden name="token"/>
		<label for="label"><@s.text name="label.label"/></label>  
		<@s.textfield name="subProduct.label" value="${subProduct.label!}"/>
		<@s.submit key="label.save"/>
		<a href="#" onclick="$('subProductDefinition_${subProduct.product.id}').show(); $('subProductLabelForm_${subProduct.product.id}').hide(); $('subProductForm_${subProduct.product.id}').reset(); return false;"><@s.text name="label.cancel"/></a>
	</@s.form>
	
</div>


<script type="text/javascript">
	$('subProductForm_${subProduct.product.id}').observe('submit', function(event) { event.stop(); $('subProductForm_${subProduct.product.id}').request(getStandardCallbacks()); } );
</script>	

