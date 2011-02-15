<title><@s.text name="title.massupdateevents" /></title>
<head>
	<@n4.includeStyle href="massUpdate" type="page"/>
	<#include "/templates/html/common/_columnView.ftl"/>
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>

<div>
	<p class="instructions"><@s.text name="instruction.massupdate"><@s.param >${numberSelected}</@s.param><@s.param ><@s.text name="label.events"/></@s.param></@s.text></p> 
</div>

<@s.form action="massUpdateEventsSave" theme="fieldidSimple" cssClass="listForm">
	<@s.url id="deleteUrl" action="massUpdateEventsConfirmDelete" namespace="/" />
	<@s.url id="updateUrl" action="massUpdateEventsSave" namespace="/" />
	<@s.hidden name="searchId" />
	<@s.hidden name="currentPage" />
	<table class="list">
		<tr>
			<th class="checkboxRow">&nbsp;</th>
			<th><@s.text name="label.field"/></th>
			<th><@s.text name="label.new_value"/></th>
		</tr>
		
		<tr>
			<td><@s.checkbox name="select['owner']" id="check_owner" /></td>
			<td>
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
			<td><@s.checkbox name="select['assetStatus']" id="check_assetStatus" /></td>
			<td>
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
			<td><@s.checkbox name="select['eventBook']" id="check_eventBook"/></td>
			<td>
				<div class="infoSet">
					<label class="label" ><@s.text name="label.eventbook"/>:</label> 
				</div>
			</td>
			<td>
				<div class="infoSet">
					<span class="field"><@s.select  name="eventBook" list="eventBooks" listKey="id" listValue="name" emptyOption="true" labelposition="left" onchange="selectField('eventBook');" /></span>
				</div>
			</td>
		</tr>
		<tr>
			<td><@s.checkbox name="select['location']" id="check_location"/></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.location"/>:</label> 
				</div>
			<td>
				<div class="infoSet">
					<span class="field"><@n4.location name="location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(location)}" /></span>
				</div>
			</td>
		</tr>
		<tr>
			<td><@s.checkbox name="select['printable']" id="check_printable"/></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.printable"/>:</label> 
				</div>
			</td>
			<td>
				<div class="field">
					<@s.checkbox name="printable" onchange="selectField('printable');"/>
				</div>
			</td>
		</tr>
		<tr>
			<td><@s.checkbox name="select['delete']" id="check_delete"  onchange="changeAction();"/></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.delete_selected_events"/></label>
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
		<a href="<@s.url action="reportResults" includeParams="none" searchId="${searchId!1}"currentPager="${currentPage!1}"/>"><@s.text name="label.returntoreport"/></a>
	</div>

</@s.form>
	<script type="text/javascript">
		onDocumentLoad(function() {
			$('owner').observe("owner:change", function() {
				selectField('owner');
			});
			
			$('location').observe("location:change", function() {
				selectField('location');
			});
		});
		
		function selectField( fieldType ) {
			var field = $('check_' + fieldType );
			field.checked = true;
		}
		
		$$('input[type="checkbox"]').each(function(checkBox) {
			checkBox.observe('click', function() {
			 	if (!checkBox.checked){
					checkBox.up(1).removeClassName('selected');	
			 	}else{
			 		checkBox.up(1).addClassName('selected');
			 	}
		 	}) 
		});
			
		function changeAction(){
			if($('check_delete').checked ){
				 $('massUpdateEventsSave').writeAttribute('action', '${deleteUrl}');
			}else{
				 $('massUpdateEventsSave').writeAttribute('action', '${updateUrl}');
			}
		}
	</script>