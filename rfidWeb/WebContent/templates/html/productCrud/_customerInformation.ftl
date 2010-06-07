<div class="viewSection smallViewSection" id="customerInformation" >
	<#if securityGuard.jobSitesEnabled >
		<h2><@s.text name="label.siteinformation"/><#if sessionUser.anEndUser> <a href="<@s.url action="customerInformationEdit" uniqueID="${product.id}"/>"><@s.text name="label.littleedit"/></a></#if></h2>
	<#else>	
		<h2><@s.text name="label.customerinformation"/><#if sessionUser.anEndUser> <a href="<@s.url action="customerInformationEdit" uniqueID="${product.id}"/>"><@s.text name="label.littleedit"/></a></#if></h2>
	</#if>
	
	<p>
		<label><@s.text name="label.assignedto"/></label>
		<span class="fieldValue">${(product.assignedUser.userLabel)!}</span>
	</p>
	
	<p>
		<label><@s.text name="label.organization"/></label>
		<span class="fieldValue">${(product.getOwner().getInternalOrg().getName())!}</span>
	</p>
	<p>
		<label><@s.text name="label.customername"/></label>
		<span class="fieldValue">${(product.owner.customerOrg.name)!}</span>
	</p>
	<p>
		<label><@s.text name="label.division"/></label>
		<span class="fieldValue">${(product.owner.divisionOrg.name)!}</span>
	</p>
	<p>
		<label><@s.text name="label.location"/></label>
		<span class="fieldValue">${(product.location)!}</span>
	</p>
	<p>
		<label><@s.text name="label.referencenumber"/></label>
		<span class="fieldValue">${product.customerRefNumber!}</span>
	</p>
	<p>
		<label><@s.text name="label.purchaseorder"/></label>
		<span class="fieldValue">${product.purchaseOrder!}</span>
	</p>
</div>