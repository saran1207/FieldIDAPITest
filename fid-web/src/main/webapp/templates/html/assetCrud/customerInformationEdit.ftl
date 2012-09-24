${action.setPageType('asset', 'customer_edit')!}

<head>
    <script type="text/javascript">
        getLocationPickerUrl = '<@s.url namespace="ajax" action="updateCustomerInformationLocation" uniqueID="${asset.id}"/>';
    </script>

</head>

<@s.form action="customerInformationUpdate" cssClass="crudForm" theme="fieldid">
	<#include "/templates/html/common/_formErrors.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
	<#include "/templates/html/common/_orgPicker.ftl"/>

	<@s.hidden name="uniqueID"/>
	<h2>
		<#if securityGuard.jobSitesEnabled>
			<@s.text name="label.siteinformation"/>
		<#else>
			<@s.text name="label.customerinformation"/>
		</#if>
	</h2>
	
	<#if !subAsset>
		<div class="infoSet">
			<label><#include "../common/_requiredMarker.ftl"/><@s.text name="label.owner"/></label>
			<@n4.orgPicker id="ownerId" name="owner"/>
		</div>
		<div class="infoSet">
			<label><@s.text name="label.location"/></label>
			<div class="fieldHolder">
                <span class="locationTree">
				    <@n4.location name="assetWebModel.location" id="location" nodesList=predefinedLocationTree fullName="${helper.getFullNameOfLocation(assetWebModel.location)}"  theme="fieldidSimple"/>
                </span>
			</div>
	
		</div>
	</#if>
	<div class="infoSet">
		<label><@s.text name="label.referencenumber"/></label>
		<@s.textfield name="customerRefNumber"/>
	</div>
	<div class="infoSet">
		<label><@s.text name="label.purchaseorder"/></label>
		<@s.textfield name="purchaseOrder"/>
	</div>
	<div class="formAction">
		<@s.submit key="label.save"/> 
		<@s.text name="label.or"/> 
		<a href="<@s.url value="w/assetSummary?uniqueID=${uniqueID}"/>"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>