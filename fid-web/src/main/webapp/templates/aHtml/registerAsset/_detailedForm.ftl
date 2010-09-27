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
				<label for="identified" class="label"><@s.text name="label.identifieddate"/></label>
				<@s.datetimepicker id="identified" name="identified" type="dateTime"/>
			</div>
			
			<div class="infoSet">
				<label for="productStatus" class="label"><@s.text name="label.productstatus"/></label>
					<#if !parentProduct?exists >
						<@s.select name="productStatus" list="productStatuses" listKey="uniqueID" listValue="name" emptyOption="true"  />
					<#else>
						<span class="fieldHolder" id="productStatus">${(product.productStatus.name?html)!}</span>
					</#if>		
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
					<label for="assignedUser" class="label"><@s.text name="label.assignedto"/></label>
					<#if !parentProduct?exists >
						<@s.select  name="assignedUser" list="employees" listKey="id" listValue="displayName" headerKey="0" headerValue="${action.getText('label.unassigned')}" />
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
