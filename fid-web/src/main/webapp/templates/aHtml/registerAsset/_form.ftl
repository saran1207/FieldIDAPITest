<div class="formBody">
	<@s.form action="save" namespace="/aHtml/iframe" theme="fieldid" cssClass="fullForm fluidSets" method="post"> 
		<div id="basicDetails" class="basicForm">
			<div class="formTitle">
				<h3><@s.text name="nav.asset_information" /></h3>
			</div>
			<div class="infoSet">
				<label for="product.serialNumber" class="label"><@s.text name="label.serialnumber"/></label>
				<@s.textfield name="product.serialNumber"/>
			</div>
			<div class="infoSet">
				<label for="product.rfidNumber" class="label"><@s.text name="label.rfidnumber"/></label>
				<@s.textfield name="product.rfidNumber"/>
			</div>
			<div class="infoSet">
				<label for="product.customerRefNumber" class="label"><@s.text name="label.referencenumber"/></label>
				<@s.textfield name="product.customerRefNumber"/>
			</div>
	
			<div class="infoSet">
				<label for="product.productTypeId" class="label"><@s.text name="label.producttype"/></label>
				<@s.select id="productType" name="product.productTypeId" onchange="updateProductType(this)">
					<#include "/templates/html/common/_productTypeOptions.ftl"/>
				</@s.select>
				<span class="fieldHolder updating" id="productTypeIndicator">
					<img src="<@s.url value="/images/indicator_mozilla_blu.gif" />" alt="<@s.text name="updating"/>" />
				</span>	
			</div>
	
			<div class="infoSet">
				<label for="product.advancedLocation.freeformLocation" class="label"><@s.text name="label.location"/></label>
				<@s.textfield name="product.advancedLocation.freeformLocation"/>
			</div>	
		</div>
		
		<div class="expand">
			<p>
				<a id="expand_details" onclick="openSection('extraDetails', 'expand_details', 'collapse_details');return false" href="javascript:void(0);" ><@s.text name="label.addmoredetails"/></a>
				<a id="collapse_details" onclick="closeSection('extraDetails', 'collapse_details', 'expand_details');return false" href="javascript:void(0);" style="display:none"><@s.text name="label.addlessdetails"/></a>
			</p>
		</div>
		
		<div id="extraDetails" class="detailedForm" style="display:none">
			<div class="formTitle">
				<h3><@s.text name="label.indentifiers" /></h3>
			</div>
			<div class="infoSet">
				<label for="product.identified" class="label"><@s.text name="label.identifieddate"/></label>
				<@s.textfield name="product.identified"/>
			</div>
			<div class="infoSet">
				<label for="product.productStatus" class="label"><@s.text name="label.productstatus"/></label>
				<@s.select name="product.productStatus" list="productStatuses" listKey="uniqueID" listValue="name" emptyOption="true"  />		
			</div>
			<div class="formTitle">
				<h3><@s.text name="label.ownership" /></h3>
			</div>
			<div class="infoSet">
				<label for="owner" class="label"><@s.text name="label.owner"/></label>
				<#if !parentProduct?exists >
					<@n4.orgPicker name="owner" theme="fieldid"/>
				<#else>
					<span class="fieldHolder" id="owner">${(product.owner.name?html)!}</span>
				</#if>
			</div>
			<#if securityGuard.assignedToEnabled >
				<div class="infoSet">
					<label for="product.assigneduser" class="label"><@s.text name="label.assignedto"/></label>
					<#if !parentProduct?exists >
						<@s.select  name="product.assignedUser" list="employees" listKey="id" listValue="displayName" headerKey="0" headerValue="${action.getText('label.unassigned')}" />
					<#else>
						<span class="fieldHolder" id="assignedUser">${(product.assignedUser.userLabel)!}</span>
					</#if>
				</div>
			</#if>
			<div class="formTitle">
				<h3><@s.text name="label.orderdetails" /></h3>
			</div>				
			<div class="infoSet">
				<label for="product.purchaseOrder" class="label"><@s.text name="label.purchaseorder"/></label>
				<@s.textfield  name="product.purchaseOrder" />	
			</div>
			<#if !securityGuard.integrationEnabled>
				<div class="infoSet">
					<label for="nonIntegrationOrderNumber" class="label"><@s.text name="label.ordernumber"/></label>
					<@s.textfield id="nonIntegrationOrderNumber" name="nonIntegrationOrderNumber" />	
				</div>
			</#if>
			<div class="formTitle">
				<h3><@s.text name="label.attributes"/></h3>
			</div>
			<#include "_infoOptions.ftl">
			
			<div class="infoSet">
				<label for="comments" class="label"><@s.text name="label.comments"/></label>
				<span class="fieldHolder">
					<@s.select id="commentTemplateSelection" name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)" theme="fieldidSimple"/><br/>
					<@s.textarea id="comments"  name="comments" theme="fieldidSimple"/>
				</span>
			</div>
		</div>
		


		
		<@s.submit id="saveButton" name="save" cssClass="save" key="hbutton.save"/>
		<@s.text name="label.or"/>
		<a href="" onclick="Lightview.hide();"><@s.text name="label.cancel"/></a>
	</@s.form>
</div>
