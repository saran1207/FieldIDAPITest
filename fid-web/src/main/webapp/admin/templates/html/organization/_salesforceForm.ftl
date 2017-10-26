<#include "../../../../templates/html/common/_formErrors.ftl"/>
<@s.form id="salesforceForm" action="updateSalesforceId" namespace="/adminAjax" theme="fieldidSimple" >
    <@s.hidden name="id"/>
    <label class="bold">Salesforce ID: </label>
    <span>
    <@s.textfield name="salesforceId" onkeypress="return doSubmitSalesforceId(event);"/>
	</span>
    <div id="salesforceFormActions">
        <input id="updateSalesforceIdButton" type="button" onClick="updateSalesforceId();" value="<@s.text name='label.save'/>"/>
        <@s.text name="label.or"/>
        <a href="javascript:void(0);" onClick="cancelSalesforceId(${id});">
            <@s.text name="label.cancel"/>
        </a>
    </div>
</@s.form>
