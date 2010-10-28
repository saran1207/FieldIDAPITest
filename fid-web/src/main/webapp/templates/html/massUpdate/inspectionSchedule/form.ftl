<title><@s.text name="title.massupdateinspectionschedules" /></title>
<head>
	<#include "/templates/html/common/_calendar.ftl"/>
</head>
<h4 >Instructions </h4>
<div class="help">
	<p>
		<@s.text name="instruction.massupdate" /> 
	</p>
</div>

<@s.form action="massUpdateEventScheduleSave" theme="simple" cssClass="listForm">
	<@s.hidden name="searchId" />
	<@s.hidden name="currentPage" />
	<table class="list">
		<tr>
			<th class="checkboxRow"><@s.text name="label.select"/></th>
			<th><@s.text name="label.fieldstoupdate"/></th>
		</tr>
		<tr>
			<td><@s.checkbox name="select['nextDate']" id="check_nextDate"/></td>
			<td>
				<label class="label"><@s.text name="label.nextinspectiondate"/>:</label>
				<span class="field">
					<@s.textfield id="input_nextDate" key="label.tdate" name="nextDate" size="10" labelposition="left" onchange="selectField('nextDate');" title="${Session.sessionUser.displayDateFormat}" />
					<img src="images/icons/FieldID_CALENDAR-CHECK-normal.png" border="0" id="nextDateTrigger">
					<script type="text/javascript">
						Calendar.setup({
							inputField  : "input_nextDate",
							ifFormat    : "${Session.sessionUser.otherDateFormat}",
							button      : "nextDateTrigger"
						});
					</script>
					<p>
						<@s.fielderror>
							<@s.param>nextDate</@s.param>				
						</@s.fielderror>
					</p>
				</span>
			</td>
		</tr>
		<tr>
			<td><@s.checkbox name="select['removeIncomplete']" id="check_removeIncomplete" onchange="disableOtherFields(this.checked);" /></td>
			<td>
				<p>
					<label class="label"><@s.text name="label.removeincompleteschedules"/></label> 
				</p>
			</td>
		</tr>
	</table>	
		
	<div class="formAction">
		<a href="<@s.url action="scheduleResults" includeParams="none" searchId="${searchId!1}"currentPager="${currentPage!1}"/>"><@s.text name="label.returntoschedules"/></a>
		<@s.submit key="hbutton.save" onclick="if( !confirm( '${action.getText( 'warning.massupdate' )}' ) ) { return false; }"/>
	</div>

</@s.form>
<script type="text/javascript">
	function selectField( fieldType ) {
		var field = $('check_' + fieldType );
		
		field.checked = true;
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
</script>