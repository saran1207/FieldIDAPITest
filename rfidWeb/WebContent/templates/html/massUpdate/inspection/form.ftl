<title><@s.text name="title.massupdateinspections" /></title>
<head>
	<#include "/templates/html/common/_columnView.ftl"/>
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
	</script>
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>

<h4 >Instructions </h4>
<div class="help">
	<div class="infoSet">
		<@s.text name="instruction.massupdate" /> 
	</div>
	
</div>

<@s.form action="massUpdateInspectionsSave" theme="fieldidSimple" cssClass="listForm">
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
			<td><@s.checkbox name="select['productStatus']" id="check_productStatus" /></td>
			<td>
				<div class="infoSet">
					<label class="label" ><@s.text name="label.productstatus"/>:</label> 
					<span class="field"><@s.select  name="productStatus" list="productStatuses" listKey="uniqueID" listValue="name" emptyOption="true" labelposition="left" onchange="selectField('productStatus');" /></span>
				</div>
			</td>
		</tr>
			
		<tr>
			<td><@s.checkbox name="select['inspectionBook']" id="check_inspectionBook"/></td>
			<td>
				<div class="infoSet">
					<label class="label" ><@s.text name="label.inspectionbook"/>:</label> 
					<span class="field"><@s.select  name="inspectionBook" list="inspectionBooks" listKey="id" listValue="name" emptyOption="true" labelposition="left" onchange="selectField('inspectionBook');" /></span>
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
	</table>	
		
	<div class="formAction">
		<a href="<@s.url action="reportResults" includeParams="none" searchId="${searchId!1}"currentPager="${currentPage!1}"/>"><@s.text name="label.returntoreport"/></a>
		<@s.submit key="hbutton.save" onclick="if( !confirm( '${action.getText( 'warning.massupdate' )}' ) ) { return false; }"/>
	</div>

</@s.form>
