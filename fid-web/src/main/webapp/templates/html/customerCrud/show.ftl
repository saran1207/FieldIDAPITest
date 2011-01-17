${action.setPageType('customer','show')!}

<head>
	<@n4.includeStyle href="user" type="page"/>
</head>

<div class="viewactions">
	<#if !customer.linked >
		<div class="useractions delete">
			<p>
				<a href="<@s.url value="customerArchive.action" uniqueID="${customer.id}" />" onclick="return confirm('<@s.text name="label.areyousurearchivecustomer" />');" ><@s.text name="label.archive" /></a>
			<p>
		</div>
	</#if>
	<#if securityGuard.readOnlyUserEnabled>
		<div class="useractions addUser">
			<p><a href="<@s.url action="customersUserAdd" uniqueID=""  customerId="${customer.id}"/>"><@s.text name="label.add_user"/></a></p>
		</div>
	</#if>
	<#if securityGuard.emailAlertsEnabled>
		<div class="useractions email">
			<p><a href="<@s.url action="notificationSettings"/>"><@s.text name="label.setup_email_settings"/></a></p>
		</div>
	</#if>
</div>

<div class="viewRow" >
	<div class="userDetails viewSection smallViewSection">
		<h2><@s.text name="label.details"/></h2>
		<p>
			<label for="id"><@s.text name="label.id"/></label>
			<span class="fieldValue">${customerId!}</span>
		</p>
		<p>
			<label for="organization"><@s.text name="label.organizationalunit"/></label>
			<span class="fieldValue">${customer.parent.name!}</span>
		</p>
		<p>
			<label for="customerName"><@s.text name="label.name"/></label>
			<span class="fieldValue">${customerName!}</span>
		</p>
		<p>
			<label for="created"><@s.text name="label.created" /></label>
			<span class="fieldValue"><#if customer.createdBy?exists>${customer.createdBy.fullName!},&nbsp;</#if>${action.formatDateTime(customer.created)}</span>
		</p>
		<p>
			<label for="lastModified"><@s.text name="label.last_modified" /></label>
			<span class="fieldValue"><#if customer.modifiedBy?exists>${customer.modifiedBy.fullName!},&nbsp;</#if>${action.formatDateTime(customer.modified)}</span>
		</p>
	</div>
	
	<div class="userDetails viewSection smallViewSection">
		<h2><@s.text name="label.contact_information"/></h2>
		<p>
			<label for="contactName"><@s.text name="label.contactname"/></label>
			<span class="fieldValue">${contactName!}</span>
		</p>
		<p>
			<label for="contactEmail"><@s.text name="label.email_address"/></label>
			<span class="fieldValue">${accountManagerEmail!}</span>
		</p>
		<p>
			<label for="streetAddress"><@s.text name="label.streetaddress"/></label>
			<span class="fieldValue">${addressInfo.streetAddress!}</span>
		</p>
		<p>
			<label for="city"><@s.text name="label.city"/></label>
			<span class="fieldValue">${addressInfo.city!}</span>
		</p>
		<p>
			<label for="state"><@s.text name="label.state"/></label>
			<span class="fieldValue">${addressInfo.state!}</span>
		</p>
		<p>
			<label for="zip"><@s.text name="label.zip"/></label>
			<span class="fieldValue">${addressInfo.zip!}</span>
		</p>
		<p>
			<label for="country"><@s.text name="label.country"/></label>
			<span class="fieldValue">${addressInfo.country!}</span>
		</p>
		<p>
			<label for="phone1"><@s.text name="label.phone1"/></label>
			<span class="fieldValue">${addressInfo.phone1!}</span>
		</p>
		<p>
			<label for="phone2"><@s.text name="label.phone2"/></label>
			<span class="fieldValue">${addressInfo.phone2!}</span>
		</p>
		<p>
			<label for="fax"><@s.text name="label.fax"/></label>
			<span class="fieldValue">${addressInfo.fax1!}</span>
		</p>
	</div>
</div>


