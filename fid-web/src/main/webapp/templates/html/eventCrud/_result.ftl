<h2><@s.text name="label.result"/></h2>
<div  class="infoSet">
	<label class="label" for="overrideResult"><@s.text name="label.result"/></label>
	<span class="fieldHolder">
        <select id="overrideResult" name="modifiableEvent.overrideResult">
            <#if action.isAutoResultAvailableForEvent()>
                <option value="auto"><@s.text name="label.set_from_criteria"/></option>
            </#if>
            <#list results as result>
                <#if overrideResult?exists && overrideResult == result.name()>
                    <#assign selectedStr = 'selected'/>
                <#else>
                    <#assign selectedStr = ''/>
                </#if>
                <option value="${result.name()}" ${selectedStr}>${action.getText(result.label)}</option>
            </#list>
        </select>
	</span>
</div>

<div class="infoSet">
    <label class="label" for="eventStatus"><@s.text name="label.event_status"/></label>
    <span class="fieldHolder">
        <@s.select name="eventStatus" headerKey="" headerValue="" list="eventStatuses" listKey="id" listValue="displayName" theme="simple"/>
    </span>
</div>
