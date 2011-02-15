<title><@s.text name="title.massupdateassets" /></title>
<head>
	<@n4.includeStyle href="massUpdate" type="page"/>
	<#include "/templates/html/common/_calendar.ftl"/>	
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
	<style type="text/css">
	
	</style>
</head>

<div>
	<p class="instructions"><@s.text name="instruction.massupdate" /></p> 
</div>

<@s.form action="massUpdateAssetsSave" id="massUpdateAssetsSave" theme="fieldidSimple" cssClass="listForm">
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden name="searchId" />
	<@s.hidden name="currentPage" />
	<@s.url id="deleteUrl" action="massAssetConfirmDelete" namespace="/" />
	<@s.url id="updateUrl" action="massUpdateAssetsSave" namespace="/" />
	
	<table class="list">
		<tr>
			<th class="checkboxRow">&nbsp;</th>
			<th><@s.text name="label.field"/></th>
			<th><@s.text name="label.new_value"/></th>
		</tr>
		<#if securityGuard.assignedToEnabled>
			<tr>
				<td><@s.checkbox name="select['assignedUser']" id="check_assignedUser"/></td>
					<td class="labelContainer">
						<div class="infoSet">
							<label class="label" ><@s.text name="label.assignedto"/>:</label>
						</div>
					</td>
				<td>
					<div class="infoSet">
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
			<td class="labelContainer">
				<div class="infoSet">
					<label class="label" ><@s.text name="label.owner"/>:</label> 
				</div>
			</td>
			<td>
				<div class="infoSet">
					<span class="field"><@n4.orgPicker name="owner" id="owner" required="true"/></span>
				</div>
			</td>
		</tr>
			
		<tr>
			<td><@s.checkbox name="select['assetStatus']" id="check_assetStatus"/></td>
			<td class="labelContainer">
				<div class="infoSet">
					<label class="label" ><@s.text name="label.assetstatus"/>:</label> 
					
				</div>
			</td>
			<td>
				<div class="infoSet">	
					<span class="field"><@s.select  name="assetStatus" list="assetStatuses" listKey="id" listValue="name" emptyOption="true" labelposition="left" onchange="selectField('assetStatus');" /></span>
				</div>
			</td>
		</tr>
			
		<tr>
			<td><@s.checkbox name="select['purchaseOrder']" id="check_purchaseOrder"/></td>
			<td class="labelContainer">
				<div class="infoSet">
					<label class="label"><@s.text name="label.purchaseorder"/>:</label> 
				</div>
			</td>
			<td> 
				<div class="infoSet">
					<span class="field"><@s.textfield name="purchaseOrder" onchange="selectField('purchaseOrder');" /></span>
				</div>
			</td>
		</tr>
		
		<#if !securityGuard.integrationEnabled>
			<tr>
				<td><@s.checkbox name="select['nonIntegrationOrderNumber']" id="check_nonIntegrationOrderNumber"/></td>
				<td class="labelContainer">
					<div class="infoSet">
						<label class="label"><@s.text name="label.ordernumber"/>:</label> 
					</div>
				</td>
				<td>
					<div class="infoSet">
						<span class="field"><@s.textfield name="nonIntegrationOrderNumber" onchange="selectField('nonIntegrationOrderNumber');" /></span>
					</div>
				</td>	
			</tr>
		</#if>
			
		<tr>
			<td><@s.checkbox name="select['location']" id="check_location"/></td>
			<td class="labelContainer">
				<div class="infoSet">
					<label class="label"><@s.text name="label.location"/>:</label> 
				</div>
			</td>
			<td>
				<div class="infoSet">
					<span class="field"><@n4.location name="assetWebModel.location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(assetWebModel.location)}" onchange="selectField('location');" /></span>
				</div>
			</td>
		</tr>
		
		<tr>
			<td><@s.checkbox name="select['identified']" id="check_identified" /></td>
			<td class="labelContainer">
				<div class="infoSet">
					<label class="label"><@s.text name="label.identified"/>:</label>
				</div>
			</td>
			<td>
				<div class="infoSet">
					<span class="field"><@s.datetimepicker id="identified" name="identified" type="dateTime" onchange="selectField('identified');"/></span>
				</div>
			</td>
		</tr>
		
		<#if userSecurityGuard.allowedManageSafetyNetwork == true>
			<tr>
				<td><@s.checkbox name="select['published']" id="check_published" /></td>
				<td class="labelContainer">
					<div class="infoSet">
						<label class="label"><@s.text name="label.publishedstateselector"/>:</label>
					</div>
				</td>
				<td>
					<div class="infoSet">
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
			<td>
				&nbsp;
			</td>
		</tr>
	</table>

	<div class="formAction">
		<@s.submit key="label.perform_mass_update" onclick="if( !confirm( '${action.getText( 'warning.massupdate' )}' ) ) { return false; }"/>
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