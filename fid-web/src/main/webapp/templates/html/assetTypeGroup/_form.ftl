<#include "/templates/html/common/_formErrors.ftl" />

<div class="infoSet">
	<label for="name"><@s.text name="indicator.required"/> <@s.text name="label.name"/></label>
	<@s.textfield name="name" required="true"/>
</div>
<#if securityGuard.lotoProceduresEnabled>
    <div class="infoSet">
        <label for="lotoDevice"><@s.text name="label.loto_device"/></label>
        <@s.checkbox name="lotoDevice"/>
    </div>
    <div class="infoSet">
        <label for="lotoLock"><@s.text name="label.loto_lock"/></label>
        <@s.checkbox name="lotoLock"/>
    </div>
</#if>



