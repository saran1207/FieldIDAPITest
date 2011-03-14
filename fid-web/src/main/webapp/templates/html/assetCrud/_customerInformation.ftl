<div class="viewSection smallViewSection" id="customerInformation" >
	
	<h2><@s.text name="label.owner"/><#if sessionUser.anEndUser || sessionUser.liteUser> <a href="<@s.url action="customerInformationEdit" uniqueID="${asset.id}"/>"><@s.text name="label.littleedit"/></a></#if></h2>
	
	<#if securityGuard.assignedToEnabled>
		<p>
			<label><@s.text name="label.assignedto"/></label>
			<span class="fieldValue">${(asset.assignedUser.userLabel)!action.getText('label.unassigned')}</span>
		</p>
	</#if>
	<p>
		<label><@s.text name="label.organization"/></label>
		<span class="fieldValue">${(asset.getOwner().getInternalOrg().getName())!}</span>
	</p>
	<p>
		<label><@s.text name="label.customername"/></label>
		<span class="fieldValue">${(asset.owner.customerOrg.name)!}</span>
	</p>
	<p>
		<label><@s.text name="label.division"/></label>
		<span class="fieldValue">${(asset.owner.divisionOrg.name)!}</span>
	</p>

	<p>
		<label><@s.text name="label.location"/></label>
		<span class="fieldValue">${(helper.getFullNameOfLocation(asset.advancedLocation))?html}</span>
	</p>
	<p>
		<label><@s.text name="label.referencenumber"/></label>
		<span class="fieldValue">${asset.customerRefNumber!}</span>
	</p>
	<p>
		<label><@s.text name="label.purchaseorder"/></label>
		<span class="fieldValue">${asset.purchaseOrder!}</span>
	</p>
</div>