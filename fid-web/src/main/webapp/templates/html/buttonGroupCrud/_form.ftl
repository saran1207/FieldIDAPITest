
<div id="stateSet_${buttonGroupIndex}" class="buttons">
	<@s.form action="buttonGroupSave" cssClass="stateSetForm" namespace="/ajax" id="stateSet_${buttonGroupIndex}_form" name="stateSet_${buttonGroupIndex}_form" theme="simple" >	
		<@s.hidden name="buttonGroupIndex" value="${buttonGroupIndex!}"/> 
		<div class="messageContainer">
			<div class="message" id="${buttonGroupIndex}_message"></div>
			<div id="${buttonGroupIndex}_error" class="error"></div>
			<#include "/templates/html/common/_formErrors.ftl"/>
		</div>
		<div class="rowName">
			<p class="groupName">
				<@s.textfield name="name" value="${stateSet.name!action.getText('label.new_button_group_name')}" onchange="enableGroupSaveButton(${buttonGroupIndex})"/>
				<@s.hidden name="uniqueID" value="${stateSet.id!}"/>
			</p>
			<p id="buttons_${buttonGroupIndex}" style="display:none">
				<button id="undo_${buttonGroupIndex}" onclick="undoButtonGroup( ${stateSet.id!"null"}, ${buttonGroupIndex} ); return false;"  ><@s.text name="label.undo_changes"/></button>
			</p>
			
		</div>
		<div class="iconGroup" style="float:left">
			<ul class="buttonGroup" id="buttonStates__${buttonGroupIndex}">
				<li class="buttonGroupLabels">
					<div class="label eventButton"><@s.text name="label.image"/>:</div> 
					<div class="label" ><@s.text name="label.label"/>:</div>
					<div class="label" ><@s.text name="label.indicates"/>:</div>
					<div class="label" ><@s.text name="label.options"/>:</div>
				</li>
				${action.setStates(states)}
				
				<#list states as state >
					<#if state?exists>
						<#include "_buttonForm.ftl"/>
					</#if>
				</#list>
				<#assign lastButtonIndex=(states?size - stateSet.countOfAvailableStates()) + 5 >
				<#if stateSet.countOfAvailableStates() lt 6 >
					<#list states?size..lastButtonIndex as state_index>
						<#include "_buttonForm.ftl"/>
					</#list>
				</#if>
				
				<script type="text/javascript">
					buttonGroupIndex[${buttonGroupIndex}] = ${lastButtonIndex} + 1;
				</script> 
				
			</ul>
		</div>
	</@s.form>
</div>	


