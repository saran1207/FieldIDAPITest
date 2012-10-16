<title><@s.text name="title.massupdateeventschedules" /></title>
<head>
	<@n4.includeStyle href="massUpdate" type="page"/>
	<#include "/templates/html/common/_datetimepicker.ftl"/>
</head>

<div>
	<p class="instructions"><@s.text name="instruction.massupdate"><@s.param >${numberSelected}</@s.param><@s.param ><@s.text name="label.schedules"/></@s.param></@s.text></p> 
</div>

<@s.form action="massUpdateEventScheduleSave" theme="simple" cssClass="listForm">
	<@s.hidden name="searchId" />
	<@s.hidden name="currentPage" />
	<table class="list">
		<tr>
			<th class="checkboxRow"></th>
			<th class="fieldColumn"><@s.text name="label.field"/></th>
			<th><@s.text name="label.new_value"/></th>
		</tr>
		<tr>
			<td><@s.checkbox name="select['dueDate']" id="check_nextDate"/></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.nexteventdate"/>:</label>
				</div>
			</td>
			<td>
				<div class="infoSet">
					<@s.textfield id="input_nextDate" key="label.tdate" name="dueDate" size="10" labelposition="left" onchange="selectField('dueDate');" title="${Session.sessionUser.displayDateFormat}" cssClass="datepicker"/>
					<script type="text/javascript">
						initDatePicker();
					</script>
					<p>
						<@s.fielderror>
							<@s.param>dueDate</@s.param>
						</@s.fielderror>
					</p>
				</div>
			</td>
		</tr>
		<tr>
			<td><@s.checkbox name="select['removeIncomplete']" id="check_removeIncomplete" onchange="disableOtherFields(this.checked);" /></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.removeincompleteschedules"/></label> 
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
		<a href="/fieldid/w/runLastReport"><@s.text name="label.returntoschedules"/></a>
	</div>

</@s.form>
<script type="text/javascript">
	function selectField( fieldType ) {
		var field = $('check_' + fieldType );
		
		field.checked = true;
		field.up(1).addClassName('selected');
	}
	
	function disableOtherFields(disable) {
		if (disable) {
			$('check_nextDate').disable();
			$('input_nextDate').disable();
		} else {
			$('check_nextDate').enable();
			$('input_nextDate').enable();
		}
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
</script>
