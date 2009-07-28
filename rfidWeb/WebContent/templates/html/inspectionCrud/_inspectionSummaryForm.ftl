<@s.hidden name="uniqueID" id="uniqueID"/>
<@s.hidden name="productId"/>
<@s.hidden name="inspectionGroupId"/>
<@s.hidden id="inspectionTypeId" name="type"/>
<div id="productSummary">
	<h2><@s.text name="label.productsummary"/></h2>
	<p>
		<label><@s.text name="${Session.sessionUser.serialNumberLabel}"/></label>
		<span>
			${product.serialNumber?html}
		</span>
	</p>
	<p>
		<label><@s.text name="label.rfidnumber"/></label>
		<span>
			${product.rfidNumber!?html}
		</span>
	</p>
	
	<p>
		<label><@s.text name="label.producttype"/></label>
		<span>
			${product.type.name!?html}
		</span>
	</p>
	
	<p>
		<label><@s.text name="label.desc"/></label>
		<span>
			${product.description?html}
		</span>
	</p>
</div>
<#if action.isParentProduct() >
	<h2><@s.text name="label.customerinformation"/></h2>
	
	<#if securityGuard.jobSitesEnabled >
		<p>
			<label><@s.text name="label.jobsite"/></label>
			<span>
				<@s.select name="jobSite" list="jobSites" listKey="id" listValue="name" onchange="jobSiteChange(this);">
					<#if (action.fieldErrors['jobSite'])?exists> 
						<@s.param name="cssClass">inputError</@s.param>
						<@s.param name="title">${action.fieldErrors['jobSite']}</@s.param>
					</#if>
				</@s.select> 
			</span>
		</p>
	</#if>
	<p>
		<label><@s.text name="label.customer"/></label>
		<#if securityGuard.jobSitesEnabled >
			<span id="customerName">${(inspection.customer.name)!}</span>
			<@s.hidden name="customer" id="customer" />
		<#else>
			<span>
				<@s.select name="customer" id="customer" list="customers" listKey="id" listValue="name" onchange="customerChanged(this); updateInspectionBooks(this.id, ${(unqiueID?exists)?string}); updateNextDate();">
					<#if !Session.sessionUser.anEndUser >
						<@s.param name="headerKey"></@s.param>
						<@s.param name="headerValue"></@s.param>
					</#if> 
				</@s.select>
			</span>
		</#if>
	</p>
	
	<p>
		<label><@s.text name="label.division"/></label>
		<#if securityGuard.jobSitesEnabled >
			<span id="divisionName">${(inspection.division.name)!}</span>
			<@s.hidden name="division" id="division"/>
		<#else>
			<span>
				<@s.select name="division" list="divisions" listKey="id" listValue="name" id="division" >
					<#if !Session.sessionUser.inDivision >
						<@s.param name="headerKey"></@s.param>
						<@s.param name="headerValue"></@s.param>
					</#if> 
				</@s.select>
			</span>
		</#if>
	</p>
	
	<p>
		<label><@s.text name="label.location"/></label>
		<span><@s.textfield name="location" /></span>
	</p>
</#if>

<h2><@s.text name="label.inspectiondetails"/></h2>
<p> 
	<label><@s.text name="label.inspectiontype"/></label>
	<span>${inspection.type.name?html}</span>
</p>
<#if action.isParentProduct() >
	<p>
		<label><@s.text name="label.inspector"/></label>
		<span>
		<#if inspection.id?exists>
			<@s.select name="inspector" list="users" listKey="id" listValue="name"  />
		<#else>
			<@s.select name="inspector" list="inspectors" listKey="id" listValue="name"  />
		</#if>
		</span>
	</p>
	<p>
		<label><@s.text name="label.inspectiondate"/></label>
		<span class="date">
			<@s.datetimepicker id="inspectionDate" onchange="updateNextDate();" name="inspectionDate" theme="fieldidSimple"  type="dateTime"/>
		</span>
		
	</p>
	<#if inspectionScheduleOnInspection>
	<p> 
		<label><@s.text name="label.scheduledon"/></label>
		<span>
			<#if inspection.schedule?exists>
				${action.formatDate(inspection.schedule.nextDate, false )}
			<#else>
				<@s.text name="label.notscheduled"/>
			</#if>
		</span>
	</p>
	<#else>
		<p <#if scheduleSuggested>class="suggested"</#if>>
		
			<label><@s.text name="label.schedulefor"/></label>
			<span>
				<@s.select id="schedule" name="scheduleId" list="schedules" listKey="id" listValue="name"/>
				<#if scheduleSuggested>
					<a href="javascript:void(0);" id="suggestedSchedule_button">?</a>
					<div id="suggestedSchedule" class="hidden quickView" ><@s.text name="label.wesuggestedascheduleforyou"/></div>
					<script type="text/javascript">
						$("suggestedSchedule_button").observe( 'click', function(event) { showQuickView('suggestedSchedule', event); } );
					</script>
				</#if>
			</span>
		</p>
	</#if>
		
	<p>
		<label><@s.text name="label.inspectionbook"/></label>
		<span id="inspectionBookSelect" <#if newInspectionBookTitle?exists>style="display:none"</#if>>
			<@s.select name="book" id="inspectionBooks" list="inspectionBooks" listKey="id" listValue="name" >
				<#if !sessionUser.anEndUser >
					<@s.param name="headerKey"></@s.param>
					<@s.param name="headerValue"></@s.param>
				</#if>
				<#if newInspectionBookTitle?exists>
					<@s.param name="disabled" value="true"/>
				</#if> 
			</@s.select>
			<a href="javascript:void(0);" onclick="changeToNewInspectionBook();"><@s.text name="label.new_inspection_book"/></a>
		</span>
		<span id="inspectionBookTitle" <#if !newInspectionBookTitle?exists>style="display:none;"</#if>>
			<@s.textfield name="newInspectionBookTitle" id="newInspectionBook" theme="fieldidSimple">
				<#if !newInspectionBookTitle?exists>
					<@s.param name="disabled" value="true"/>
				</#if>
			</@s.textfield> 
			<a href="javascript:void(0);" onclick="changeToInspectionBookSelect()"><@s.text name="label.select_existing"/></a>
		</span>
	</p>
</#if>