<title><@s.text name="title.massupdateeventschedules" /></title>
<head>
	<#include "/templates/html/common/_calendar.ftl"/>
</head>

<div>
	<p class="instructions"><@s.text name="instruction.massupdate"><@s.param >${numberSelected}</@s.param><@s.param ><@s.text name="label.events"/></@s.param></@s.text></p> 
</div>

<@s.form action="massUpdateEventScheduleSave" theme="simple" cssClass="listForm">
	<@s.hidden name="searchId" />
	<@s.hidden name="currentPage" />
	<table class="list">
		<tr>
			<th class="checkboxRow"></th>
			<th><@s.text name="label.field"/></th>
			<th><@s.text name="label.new_value"/></th>
		</tr>
		<tr>
			<td><@s.checkbox name="select['nextDate']" id="check_nextDate"/></td>
			<td>
				<div class="infoSet">
					<label class="label"><@s.text name="label.nexteventdate"/>:</label>
				</div>
			</td>
			<td>
				<div class="infoSet">
					<@s.textfield id="input_nextDate" key="label.tdate" name="nextDate" size="10" labelposition="left" onchange="selectField('nextDate');" title="${Session.sessionUser.displayDateFormat}" />
					<img src="images/calendar-icon.png" border="0" id="nextDateTrigger">
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
