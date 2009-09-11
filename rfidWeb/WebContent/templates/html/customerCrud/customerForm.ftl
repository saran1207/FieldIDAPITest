<title>
	<#if uniqueID?exists  >
		${action.setPageType('customer','edit')!}
	<#else>
		${action.setPageType('customer','add')!}
	</#if>
</title>
<@s.form action="customerEdit!save" cssClass="inputForm oneColumn" theme="css_xhtml">
	<@s.hidden name="uniqueID" />
	<@s.hidden name="currentPage" />
	
	<div class="formRowHolder">
		<@s.textfield key="label.customerid" required="true" name="customerId" size="30" labelposition="left" />
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.customername" required="true" name="customerName" size="50" labelposition="left" />
	</div>
	<div class="formRowHolder">
		<@s.select key="label.organizationalunit" name="parentOrgId" list="parentOrgs" listKey="id" listValue="name" labelposition="left" required="true"/>
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.contactname" name="contactName" labelposition="left" />
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.contactemail" name="accountManagerEmail" labelposition="left" />
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.streetaddress" name="addressInfo.streetAddress" labelposition="left" />
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.city" name="addressInfo.city" labelposition="left" />
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.state" name="addressInfo.state" labelposition="left" />
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.zip" name="addressInfo.zip" labelposition="left" />
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.country" name="addressInfo.country" labelposition="left" />
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.phone1" name="addressInfo.phone1" labelposition="left" />
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.phone2" name="addressInfo.phone2" labelposition="left" />
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.fax" name="addressInfo.fax1" labelposition="left" />
	</div>
	<div class="formAction" >
		<a href="<@s.url action="customerList" currentPage="${currentPage}" listFilter="${listFilter!}"/>" ><@s.text name="label.backtocustomerlist"/></a> 
		<#if uniqueID?exists>  
			| <a href="<@s.url action="customerShow" uniqueID="${uniqueID!}" currentPage="${currentPage!}" listFilter="${listFilter!}"/>" ><@s.text name="hbutton.cancel" /></a>
		</#if> 
		<@s.submit key="hbutton.save" />
	</div>
</@s.form>
