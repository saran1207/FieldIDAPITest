<head>
	<title>
		<#if uniqueID?exists  >
			${action.setPageType('customer','edit')!}
		<#else>
			${action.setPageType('customer','add')!}
		</#if>
	</title>
	<@n4.includeStyle href="user" type="page"/>
</head>
<@s.form action="customerEdit!save" cssClass="fullForm fluidSets" theme="fieldid">
	<#include "../common/_formErrors.ftl"/>
	<@s.hidden name="uniqueID" />
	<@s.hidden name="currentPage" />
	<#if customer.linkedOrg?exists>
		<p class="instructions">
			<@s.text name="instructions.linked_customer_edit"/>
		</p>
	</#if>
	<div class="multiColumn">
		<div class="fieldGroup fieldGroupGap">
			<h2><@s.text name="label.details"/></h2>
			<div class="infoSet">
				<label class="label" for="customerId"><@s.text name="label.id"/> <#include "../common/_requiredMarker.ftl"/></label>
				<@s.textfield  name="customerId" size="30" />
			</div>
			<div class="infoSet">
				<label class="label" for="parentOrgId"><@s.text name="label.organizationalunit"/> <#include "../common/_requiredMarker.ftl"/></label>
				<@s.select  name="parentOrgId" list="parentOrgs" listKey="id" listValue="name" />
			</div>
			<div class="infoSet">
				<label class="label" for="customerName"><@s.text name="label.name"/> <#include "../common/_requiredMarker.ftl"/></label>
				<@s.textfield name="customerName" size="50" cssClass="linkedCustomerControlled" />
			</div>
		</div>
		<div class="fieldGroup">
			<h2><@s.text name="label.contact_information"/></h2>
			<div class="infoSet">
				<label class="label" for="contactName"><@s.text name="label.contactname"/></label>
				<@s.textfield  name="contactName" />
			</div>
			<div class="infoSet">
				<label class="label" for="accountManagerEmail"><@s.text name="label.email_address"/></label>
				<@s.textfield  name="accountManagerEmail" />
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.streetAddress"><@s.text name="label.streetaddress"/></label>
				<@s.textfield  name="addressInfo.streetAddress" cssClass="linkedCustomerControlled"/>
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.city"><@s.text name="label.city"/></label>
				<@s.textfield  name="addressInfo.city" cssClass="linkedCustomerControlled" />
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.state"><@s.text name="label.state"/></label>
				<@s.textfield  name="addressInfo.state" cssClass="linkedCustomerControlled"/>
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.zip"><@s.text name="label.zip"/></label>
				<@s.textfield  name="addressInfo.zip" cssClass="linkedCustomerControlled" />
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.country"><@s.text name="label.country"/></label>
				<@s.textfield  name="addressInfo.country" cssClass="linkedCustomerControlled" />
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.phone1"><@s.text name="label.phone1"/></label>
				<@s.textfield  name="addressInfo.phone1" cssClass="linkedCustomerControlled"/>
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.phone2"><@s.text name="label.phone2"/></label>
				<@s.textfield  name="addressInfo.phone2" cssClass="linkedCustomerControlled" />
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.fax1"><@s.text name="label.fax"/></label>
				<@s.textfield  name="addressInfo.fax1" cssClass="linkedCustomerControlled"/>
			</div>
		</div>
	</div>
	<div class="actions" >
		<@s.submit  key="label.save"/> 
		
		<@s.text name="label.or"/>  
		
		<#if uniqueID?exists>  
			<a href="<@s.url action="customerShow" uniqueID="${uniqueID!}" currentPage="${currentPage!}" listFilter="${listFilter!}"/>" ><@s.text name="hbutton.cancel" /></a>
		<#else>
			<a href="<@s.url action="customerList" currentPage="${currentPage}" listFilter="${listFilter!}"/>" ><@s.text name="label.cancel"/></a>
		</#if>
		 
		
	</div>
</@s.form>


<head>
	<#if customer.linkedOrg?exists>
		<@n4.includeScript>
			document.observe("dom:loaded", function() {
					$$(".linkedCustomerControlled").invoke("disable");
				});
		</@n4.includeScript>
	</#if>
</head>