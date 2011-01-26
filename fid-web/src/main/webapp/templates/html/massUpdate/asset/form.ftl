<title><@s.text name="title.massupdateassets" /></title>
<head>
	<#include "/templates/html/common/_calendar.ftl"/>	
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
	<style type="text/css">
		.view .label, .listForm .label {
			padding:10px 5px 5px;
		}
	</style>
</head>
<h4><@s.text name="label.instructions"/></h4>
<div class="help">
	<div class="infoSet">
		<@s.text name="instruction.massupdate" /> 
	</div>
</div>

<@s.form action="massUpdateAssetsSave" id="massUpdateAssetsSave" theme="fieldidSimple" cssClass="listForm">
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden name="searchId" />
	<@s.hidden name="currentPage" />
	<@s.url id="deleteUrl" action="massAssetConfirmDelete" namespace="/" />
	<@s.url id="updateUrl" action="massUpdateAssetsSave" namespace="/" />
	
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
						<span class="field">
							<@s.select name="assignedUser" headerKey="0" headerValue="${action.getText('label.unassigned')}" labelposition="left" onchange="selectField('assignedUser');" >
									<#include "/templates/html/common/_assignedToDropDown.ftl"/>
							</@s.select>
						</span>
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
					<span class="field"><@s.select  name="assetStatus" list="assetStatuses" listKey="id" listValue="name" emptyOption="true" labelposition="left" onchange="selectField('assetStatus');" /></span>
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
		
		<#if !securityGuard.integrationEnabled>
			<tr>
				<td><@s.checkbox name="select['nonIntegrationOrderNumber']" id="check_nonIntegrationOrderNumber"/></td>
				<td>
					<div class="infoSet">
						<label class="label"><@s.text name="label.ordernumber"/>:</label> 
						<span class="field"><@s.textfield name="nonIntegrationOrderNumber" onchange="selectField('nonIntegrationOrderNumber');" /></span>
					</div>
				</td>
			</tr>
		</#if>
			
		<tr>
			<td><@s.checkbox name="select['location']" id="check_location"/></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.location"/>:</label> 
					<span class="field"><@n4.location name="assetWebModel.location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(assetWebModel.location)}" onchange="selectField('location');" /></span>
				</div>
			</td>
		</tr>
		
		<tr>
			<td><@s.checkbox name="select['identified']" id="check_identified" /></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.identified"/>:</label>
					<span class="field"><@s.datetimepicker id="identified" name="identified" type="dateTime" onchange="selectField('identified');"/></span>
				</div>
			</td>
		</tr>
		
		
		<#if userSecurityGuard.allowedManageSafetyNetwork == true>
			<tr>
				<td><@s.checkbox name="select['published']" id="check_published" /></td>
				<td>
					<div class="infoSet">
						<label class="label"><@s.text name="label.publishedstateselector"/>:</label>
						<span class="field"><@s.select name="published" list="publishedStates" listKey="id" listValue="name" onchange="selectField('published');" /></span>
					</div>
				</td>
			</tr>
		</#if>
		<tr>
			<td><@s.checkbox name="select['delete']" id="check_delete"  onchange="changeAction();"/></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.mass_delete"/></label>
				</div>
			</td>
		</tr>
	</table>

	<div class="formAction">
		<@s.submit key="label.update" onclick="if( !confirm( '${action.getText( 'warning.massupdate' )}' ) ) { return false; }"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="searchResults" includeParams="none" searchId="${searchId!1}"currentPager="${currentPage!1}"/>"><@s.text name="label.returntosearch"/></a>
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
	
	function changeAction(){
		if($('check_delete').checked ){
			 $('massUpdateAssetsSave').writeAttribute('action', '${deleteUrl}');
		}else{
			 $('massUpdateAssetsSave').writeAttribute('action', '${updateUrl}');
		}
	}
</script>