<title><@s.text name="title.confirm_secondary_org_archive" /> - ${organization.name?html}</title>
${action.setPageType('organization','list')!}

<div class="instructions">
	<@s.text name="instruction.archive_secondary_org"><@s.param >${organization.name?html}</@s.param></@s.text>
</div>

<div class="crudForm largeForm bigForm pageSection">
	<h2><@s.text name="label.removaldetails"/></h2>
	<div class="sectionContent">
		
		<div class="infoSet">
		<label for="">X<#--${removalSummary.customersToArchive}--></label> 
			<span><@s.text name="label.customers_to_archive"/></span>
		</div>
		
		<div class="infoSet">
			<label for="">X<#--${removalSummary.divisionsToArchive}--></label>
			<span><@s.text name="label.division_to_archive"/></span>
		</div>
		
		<div class="infoSet">
			<label for="">X<#--${removalSummary.usersToArchive}--></label>
			<span><@s.text name="label.users_to_archive"/></span>
		</div>
	</div>
	<div class="formAction">
		<@s.url id="cancelUrl" action="organizations"/>
		<@s.submit key="label.cancel" onclick="return redirect('${cancelUrl}');" theme="fieldid"/>
		
		<@s.url id="archiveUrl" action="organizationArchive" uniqueID="${organization.id}"/>
		<@s.submit key="label.archive" onclick="return redirect('${archiveUrl}');" theme="fieldid" />
	</div>
</div>


