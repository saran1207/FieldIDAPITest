<title><@s.text name="title.massupdateassets" /></title>
<head>
	<#include "/templates/html/common/_calendar.ftl"/>	
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
</head>
<h4 >Instructions </h4>
<div class="help">
	<div class="infoSet">
		<@s.text name="instruction.massupdate" /> 
	</div>
	
</div>

<@s.form action="massUpdateAssetsSave" id="massUpdateAssetsSave" theme="fieldidSimple" cssClass="listForm">
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden name="searchId" />
	<@s.hidden name="currentPage" />
	<table class="list">
		<tr>
			<th class="checkboxRow"><@s.text name="label.select"/></th>
			<th><@s.text name="label.fieldstoupdate"/></th>
		</tr>
		<#if securityGuard.assignedToEnabled>
		
			<tr>
				<td><@s.checkbox name="select['assignedUser']" id="check_assignedUser"/></td>
				<td>
					<div class="infoSet">
						<label class="label" ><@s.text name="label.assignedto"/>:</label>
						<span class="field"><@s.select name="assignedUser" list="employees" listKey="id" listValue="displayName" headerKey="0" headerValue="${action.getText('label.unassigned')}" labelposition="left" onchange="selectField('assignedUser');" /></span>
					</div>
				</td>
			</tr>
		</#if>
		<tr>
			<td><@s.checkbox name="select['owner']" id="check_owner" /></td>
			<td>
				<div class="infoSet">
					<label class="label" ><@s.text name="label.owner"/>:</label> 
					<span class="field"><@n4.orgPicker name="owner" id="owner" required="true"/></span>
				</div>
				
			</td>
		</tr>
			
		<tr>
			<td><@s.checkbox name="select['assetStatus']" id="check_assetStatus"/></td>
			<td>
				<div class="infoSet">
					<label class="label" ><@s.text name="label.assetstatus"/>:</label> 
					<span class="field"><@s.select  name="assetStatus" list="assetStatuses" listKey="uniqueID" listValue="name" emptyOption="true" labelposition="left" onchange="selectField('assetStatus');" /></span>
				</div>
			</td>
		</tr>
			
		<tr>
			<td><@s.checkbox name="select['purchaseOrder']" id="check_purchaseOrder"/></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.purchaseorder"/>:</label> 
					<span class="field"><@s.textfield name="purchaseOrder" onchange="selectField('purchaseOrder');" /></span>
				</div>
			</td>
		</tr>
			
		<tr>
			<td><@s.checkbox name="select['location']" id="check_location"/></td>
			<td>
				<div>
					<label class="label"><@s.text name="label.location"/>:</label> 
					<div class="field">
						<@n4.location name="asset.location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(assetWebModel.location)}" onchange="selectField('location');" />
					</div>
				</div>
			</td>
		</tr>
		
		<tr>
			<td><@s.checkbox name="select['identified']" id="check_identified" /></td>
			<td>
				<label class="label"><@s.text name="label.identified"/>:</label>
				<span class="field">
					<@s.datetimepicker id="identified" name="identified" type="dateTime" onchange="selectField('identified');"/>
				</span>
			</td>
		</tr>
		
		
		<#if userSecurityGuard.allowedManageSafetyNetwork == true>
			<tr>
				<td><@s.checkbox name="select['published']" id="check_published" /></td>
				<td>
					<label class="label"><@s.text name="label.publishedstateselector"/>:</label>
					<@s.select name="published" list="publishedStates" listKey="id" listValue="name" onchange="selectField('published');" />
				</td>
			</tr>
		</#if>
		
	</table>	
		
	<div class="formAction">
		<a href="<@s.url action="searchResults" includeParams="none" searchId="${searchId!1}"currentPager="${currentPage!1}"/>"><@s.text name="label.returntosearch"/></a>
		<@s.submit key="hbutton.save" onclick="if( !confirm( '${action.getText( 'warning.massupdate' )}' ) ) { return false; }"/>
	</div>

</@s.form>
<script type="text/javascript">
	
	$('owner').observe("owner:change", function() {
		selectField('owner');
	});
	
	$('location').observe("location:change", function() {
		selectField('location');
	});
	
	function selectField( fieldType ) {
		var field = $('check_' + fieldType );
		
		field.checked = true;
	}
</script>