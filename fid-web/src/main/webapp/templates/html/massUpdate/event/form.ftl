<title><@s.text name="title.massupdateevents" /></title>
<head>
	<#include "/templates/html/common/_columnView.ftl"/>
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>

<h4 >Instructions </h4>
<div class="help">
	<div class="infoSet">
		<@s.text name="instruction.massupdate" /> 
	</div>
	
</div>

<@s.form action="massUpdateEventsSave" theme="fieldidSimple" cssClass="listForm">
	<@s.url id="deleteUrl" action="massUpdateEventsConfirmDelete" namespace="/" />
	<@s.url id="updateUrl" action="massUpdateEventsSave" namespace="/" />
	<@s.hidden name="searchId" />
	<@s.hidden name="currentPage" />
	<table class="list">
		<tr>
			<th class="checkboxRow"><@s.text name="label.select"/></th>
			<th><@s.text name="label.fieldstoupdate"/></th>
		</tr>
		
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
			<td><@s.checkbox name="select['assetStatus']" id="check_assetStatus" /></td>
			<td>
				<div class="infoSet">
					<label class="label" ><@s.text name="label.assetstatus"/>:</label> 
					<span class="field"><@s.select  name="assetStatus" list="assetStatuses" listKey="id" listValue="name" emptyOption="true" labelposition="left" onchange="selectField('assetStatus');" /></span>
				</div>
			</td>
		</tr>
			
		<tr>
			<td><@s.checkbox name="select['eventBook']" id="check_eventBook"/></td>
			<td>
				<div class="infoSet">
					<label class="label" ><@s.text name="label.eventbook"/>:</label> 
					<span class="field"><@s.select  name="eventBook" list="eventBooks" listKey="id" listValue="name" emptyOption="true" labelposition="left" onchange="selectField('eventBook');" /></span>
				</div>
			</td>
		</tr>
		
		<tr>
			<td><@s.checkbox name="select['location']" id="check_location"/></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.location"/>:</label> 
					<div class="field">
					 	<@n4.location name="location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(location)}" />
					 </div>
				</div>
			</td>
		</tr>
		
		<tr>
			<td><@s.checkbox name="select['printable']" id="check_printable"/></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.printable"/>:</label> 
					<span class="field"><@s.checkbox name="printable" onchange="selectField('printable');"/></span>
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
		</tr>
	</table>	
		
	<div class="formAction">
		<@s.submit key="label.update" onclick="if( !confirm( '${action.getText( 'warning.massupdate' )}' ) ) { return false; }"/>
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
		
		function changeAction(){
			if($('check_delete').checked ){
				 $('massUpdateEventsSave').writeAttribute('action', '${deleteUrl}');
			}else{
				 $('massUpdateEventsSave').writeAttribute('action', '${updateUrl}');
			}
		}
	</script>