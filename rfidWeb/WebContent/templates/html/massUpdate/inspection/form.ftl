<title><@s.text name="title.massupdateinspections" /></title>
<head>
	<script type="text/javascript">
		
		function selectField( fieldType ) {
			var field = $('check_' + fieldType );
			
			field.checked = true;
		}
	</script>
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>

<h4 >Instructions </h4>
<div class="help">
	<p>
		<@s.text name="instruction.massupdate" /> 
	</p>
	
</div>

<@s.form action="massUpdateInspectionsSave" theme="simple" cssClass="listForm">
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
				<p>
					<label class="label" ><@s.text name="label.owner"/>:</label> 
					<span class="field"><@n4.orgPicker name="owner" /></span>
				</p>
				
			</td>
			
		</tr>
			
		<tr>
			<td><@s.checkbox name="select['inspectionBook']" id="check_inspectionBook"/></td>
			<td>
				<p>
					<label class="label" ><@s.text name="label.inspectionbook"/>:</label> 
					<span class="field"><@s.select  name="inspectionBook" list="inspectionBooks" listKey="id" listValue="name" emptyOption="true" labelposition="left" onchange="selectField('inspectionBook');" /></span>
				</p>
			</td>
		</tr>
		
		<tr>
			<td><@s.checkbox name="select['location']" id="check_location"/></td>
			<td>
				<p>
					<label class="label"><@s.text name="label.location"/>:</label> 
					<span class="field"><@s.textfield name="location" onchange="selectField('location');"/></span>
				</p>
			</td>
		</tr>
		
		<tr>
			<td><@s.checkbox name="select['printable']" id="check_printable"/></td>
			<td>
				<p>
					<label class="label"><@s.text name="label.printable"/>:</label> 
					<span class="field"><@s.checkbox name="printable" onchange="selectField('printable');"/></span>
				</p>
			</td>
		</tr>
	</table>	
		
	<div class="formAction">
		<a href="<@s.url action="reportResults" includeParams="none" searchId="${searchId!1}"currentPager="${currentPage!1}"/>"><@s.text name="label.returntoreport"/></a>
		<@s.submit key="hbutton.save" onclick="if( !confirm( '${action.getText( 'warning.massupdate' )}' ) ) { return false; }"/>
	</div>

</@s.form>
