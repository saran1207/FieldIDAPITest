<head>
	<script type="text/javascript" src="<@s.url value="/javascript/changeJobSite.js" />"></script>
	<script type="text/javascript">
		jobSiteChangeUrl = '<@s.url action="jobSite" namespace="/ajax" />';
	</script>
</head>
${action.setPageType('product', 'customer_edit')!}

<@s.form action="customerInformationUpdate" cssClass="crudForm" theme="fieldid">
	<@s.hidden name="uniqueID"/>
	<h2>
		<#if securityGuard.jobSitesEnabled>
			<@s.text name="label.siteinformation"/>
		<#else>
			<@s.text name="label.customerinformation"/>
		</#if>
	</h2>
	
	<#if !subProduct>
		<#if securityGuard.jobSitesEnabled>
			<div class="infoSet">
				<label class="label" for="jobsite"><@s.text name="label.jobsite"/></label>
				<@s.select name="jobSite" list="jobSites" required="true" listKey="id" listValue="displayName" onchange="jobSiteChange(this)"/>
			</div>
			
			<div class="infoSet">
				<label class="label" for="customer"><@s.text name="label.customer"/>:</label>
				<span class="fieldValue" id="customerName">${(product.owner.name?html)!}</span>
			</div>		 
		
			<div class="infoSet">
				<label class="label" for="division"><@s.text name="label.division"/>:</label>
				<span class="fieldValue" id="divisionName">${(product.division.name?html)!}</span>
			</div>
		
		<#else>	
			<div class="infoSet">
				<label class="label"><@s.text name="label.customername"/></label>
				<span class="fieldValue">${(product.owner.name)!}</span>
			</div>
			<div class="infoSet">
				<label for="division" class="label"><@s.text name="label.division"/></label>
				<@s.select id="division" name="division" list="divisions" listKey="id" listValue="displayName" >
					<#if !sessionUser.inDivision >
						<@s.param name="headerKey"></@s.param>
						<@s.param name="headerValue"></@s.param>
					</#if>
				</@s.select>
			</div>
		</#if>
		<div class="infoSet">
			<label><@s.text name="label.location"/></label>
			<@s.textfield name="location"/>
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
		<a href="<@s.url action="product" uniqueID="${uniqueID}"/>"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>