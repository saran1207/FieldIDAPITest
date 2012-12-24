
<#assign newState=!(buttons[button_index])?exists />
<#assign existingState=(buttons[button_index].id)?exists />
<#assign disableInput=newState />
<li id="buttonState__${buttonGroupIndex}_${button_index}" <#if button?exists && button.retired >class="retired hidden"</#if> >
	<#if existingState >
		<@s.hidden id="buttonState__${buttonGroupIndex}_${button_index}_retired" name="buttons[${button_index}].retired" theme="simple"/>
		<@s.hidden id="buttonState__${buttonGroupIndex}_${button_index}_id" name="buttons[${button_index}].id" theme="simple" />
		<div class="eventButton">
			<img src="<@s.url value="/images/eventButtons/${(buttons[button_index].buttonName)!'btn0'}.png"/>"/>
			<@s.hidden name="buttons[${button_index}].buttonName" value="${(buttons[button_index].buttonName)!'btn0'}" theme="simple"/>
		</div> 
		<div><@s.hidden name="buttons[${button_index}].displayText" />${buttons[button_index].displayText}</div>
		<div><@s.hidden name="buttons[${button_index}].statusName" /> ${ action.getText( buttons[button_index].eventResult.label ) }</div>
	
	<#else>
		<@s.textfield id="buttonState__${buttonGroupIndex}_${button_index}_retired" name="buttons[${button_index}].retired" theme="simple" cssClass="disabledInput" disabled="${disableInput?string}"/>
		<@s.textfield id="buttonState__${buttonGroupIndex}_${button_index}_id" name="buttons[${button_index}].id" theme="simple" cssClass="disabledInput" disabled="${disableInput?string}" />
		<div class="eventButton">
			<a href="javascript:void(0);" onclick="changeButtonImage(this); enableGroupSaveButton(${buttonGroupIndex})">
				<img src="<@s.url value="/images/eventButtons/${(buttons[button_index].buttonName)!'btn0'}.png"/>"  <#if !(buttons[button_index].buttonName)?exists >style="visibility:hidden"</#if>/>
				<@s.textfield name="buttons[${button_index}].buttonName" value="${(buttons[button_index].buttonName)!'btn0'}" cssClass="disabledInput"  theme="simple" disabled="${disableInput?string}"/>
			</a>
		</div> 
		<div><@s.textfield name="buttons[${button_index}].displayText" onchange="enableGroupSaveButton(${buttonGroupIndex})" theme="simple" disabled="${disableInput?string}" /></div>
		<div><@s.select name="buttons[${button_index}].statusName" list="buttonStatuses" listKey="id" listValue="%{ getText( label ) }" onchange="enableGroupSaveButton(${buttonGroupIndex})" theme="simple" disabled="${disableInput?string}"/></div>
	</#if>
	<div>
		<#if existingState>
			<span id="buttonState__${buttonGroupIndex}_${button_index}_retire" ><a href="javascript:void(0);" onclick="retireButton( ${buttonGroupIndex}, '${button_index}'); enableGroupSaveButton(${buttonGroupIndex});" theme="simple"><@s.text name="label.retire"/></a></span>
		<#else>
			<span id="buttonState__${buttonGroupIndex}_${button_index}_add" <#if !newState >style="display:none"</#if>><a href="javascript:void(0);" onclick="enableButton( ${buttonGroupIndex}, '${button_index}' ); enableGroupSaveButton(${buttonGroupIndex});"><@s.text name="label.add"/></a></span>
			<span id="buttonState__${buttonGroupIndex}_${button_index}_remove" <#if newState >style="display:none"</#if>><a href="javascript:void(0);" onclick="disableButton( ${buttonGroupIndex}, '${button_index}')"><@s.text name="label.remove"/></a></span>
		</#if>
	</div>
</li>