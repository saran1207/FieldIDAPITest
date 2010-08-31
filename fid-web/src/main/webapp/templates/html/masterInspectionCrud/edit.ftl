<head>
	<script type="text/javascript" src="<@s.url value="/javascript/subProduct.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/inspection.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/masterInspection.js"/>"></script>
	<@n4.includeStyle type="page" href="subProduct" />
	<@n4.includeStyle type="page" href="inspection" />
	<@n4.includeStyle type="page" href="masterInspection" />
</head>


<#assign form_action="EDIT" /> 
${action.setPageType('inspection', 'edit')!}

<div id="masterInspection" >
	<#include "/templates/html/common/_formErrors.ftl" />
	
	<div class="masterProduct done">
		<div class="definition"><div class="identifier"><span>${product.type.name!}</span></div></div>
		<div class="performedInspection">
			<span>${(inspectionType.name)!}</span> 
			<span>
				<a class="exitLink" href="<@s.url action="subInspectionEdit"  uniqueID="0" productId="${product.id}" type="${type}" parentProductId="${product.id}" token="${token}"/>">
					<@s.text name="label.edit_this_event"/>
				</a>
			</span>
		</div>
	</div>
	
	
	<div id="productComponents">
		<#list availableSubProducts as subProduct>
			<#include "_subInspection.ftl" />
		</#list>
	</div>
	
	<@s.form action="masterInspectionUpdate" id="subProductForm" cssClass="crudForm" theme="fieldid">
		<@s.hidden name="uniqueID" id="uniqueID"/>
		<@s.hidden name="token"/>
		<@s.hidden name="type"/>
		<@s.hidden name="inspectionGroupId"/>
		<@s.hidden name="productId" id="productId"/>
		<div class="formAction">
			<@s.url id="cancelUrl" action="inspectionGroups" uniqueID="${product.id}"/>
			<@s.submit key="label.cancel" onclick="return redirect( '${cancelUrl}' );"/>
			<button onclick="return redirect('<@s.url action="masterInspectionDelete" uniqueID="${uniqueID}" productId="${productId}" /> ');"><@s.text name="label.delete"/></button>
			<@s.submit key="label.save" />
		</div>
	</@s.form>
</div>


