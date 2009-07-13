${action.setPageType('safety_network', 'list')!}

<div class="pageSection crudForm sectionContent">
	<div class="infoSet">
		<label class="label"><@s.text name="label.fidacfull"/></label>
		<span class="fieldHolder">${sessionUser.tenant.fidAC!}</span>
	</div>
</div>


<#if Session.sessionUser.tenant.manufacturer >
	<div class="quickForm" >
		<@s.form action="safetyNetworkList!add" cssClass="simpleInputForm" theme="css_xhtml" >
			<@s.textfield name="fidAC" key="label.fidacfull" labelposition="left" maxlength="8"  />
			<div class="formAction" >
				<@s.submit key="label.linktoacompany" theme="simple" />
			</div>
		</@s.form>
	</div>
</#if>



<#if linkedTenants ?exists && linkedTenants.size() != 0 >
	<table class="list">
	<tr>
		<th></th>
		<th><@s.text name="label.company_name"/></th>
		<th><@s.text name="label.catalog"/></th>
		
	</tr>
	
	<#list linkedTenants as linkedTenant >
		<tr>
			<td><img src="<@s.url action="downloadTenantLogo" namespace="/file" includeParams="none" uniqueID="${linkedTenant.id}"/>" height="40"/></td>
			<td>${linkedTenant.displayName}</td>
			<td>
				<#if action.tenantHasCatalog(linkedTenant)>
					<a href="<@s.url action="publishedCatalog" uniqueID="${linkedTenant.id}"/>"><@s.text name="label.import_catalog"/></a>
				<#else>
					<@s.text name="label.not_published"/>
				</#if>
			</td>
		</tr>
	</#list>
	</table>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptysafetynetwork" />
		</p>
	</div>
</#if>
