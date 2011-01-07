<h2><@s.text name="label.result"/></h2>
<div  class="infoSet">
	<label class="label" for="overrideResult"><@s.text name="label.result"/></label>
	<span class="fieldHolder">
        <select id="overrideResult" name="overrideResult">
            <#if action.hasAtLeastOneResultSettingCriteria()>
                <option value="auto"><@s.text name="label.set_from_criteria"/></option>
            </#if>
            <#list results as result>
                <option value="${result.name()}">${action.getText(result.label)}</option>
            </#list>
        </select>
	</span>
</div>
