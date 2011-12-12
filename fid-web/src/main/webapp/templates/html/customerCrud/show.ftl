${action.setPageType('customer','show')!}

<head>
	<@n4.includeStyle href="user" type="page"/>
	<@n4.includeStyle href="customers" type="page"/>
</head>

<div class="viewactions">
	<#if !customer.linked >
		<div class="useractions delete">
			<p>
				<a href="<@s.url value="customerArchive.action" uniqueID="${customer.id}" />" onclick="return confirm('<@s.text name="label.areyousurearchivecustomer" />');" ><@s.text name="label.archive" /></a>
			<p>
		</div>
	</#if>
	<div class="useractions merge">
		<p><a href="<@s.url action="mergeCustomers" uniqueID="${customer.id}"/>"><@s.text name="label.merge"/></a></p>
	</div>
	<#if userLimitService.readOnlyUsersEnabled>
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
		
		<#if logoImageDirectory?exists>
			<div class="logo">
				<span></span><img src="<@s.url action="downloadOrgLogo" namespace="/file" uniqueID="${customer.id}"  />" alt="<@s.text name="label.organizationalunit"/>"/>
			</div>
		</#if>
		
		<div class="details <#if !logoImageDirectory?exists>noImage</#if>">
			<h3 class="customerName">${customerName!}</h3>
					
			<div class="customerDetails">
				<label class="label" for="id"><@s.text name="label.id"/></label>:
				<span>${customerId!}</span>
			</div>
			<div class="customerDetails">
				<label class="label" for="organization"><@s.text name="label.organizationalunit"/></label>:
				<span>${customer.parent.name!}</span>
			</div>
			<div class="customerLinks"> 
				<a href='<@s.url action="divisions" customerId="${customer.id}"/>'>${divisionCount} <@s.text name="label.divisions"/></a>
				<a href='<@s.url action="customersUsers" customerId="${customer.id}"/>'>${userCount} <@s.text name="label.user_accounts"/></a>
			</div>
		</div>
		
		<div class="notes">
			<label><@s.text name="label.notes" /></label>
			<div class="noteText">${action.replaceCR((customer.notes?html)!"")}</div>
		</div>

	</div>
	
	<div class="userDetails viewSection smallViewSection">
		<h2><@s.text name="label.contact_information"/></h2>
		<p>
			<label for="contactName"><@s.text name="label.contactname"/></label>
			<span class="fieldValue">${contactName!}</span>
		</p>
		<p>
			<label for="contactEmail"><@s.text name="label.email_address"/></label>
			<span class="fieldValue"><a href="mailto:${accountManagerEmail!}">${accountManagerEmail!}</a></span>
			
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


