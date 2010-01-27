
<div class="infoSet" id="safetyNetworkRegistration">
	<label for="linkedProduct" class="label"><@s.text name="label.register_product"/></label>
	<div style="float:left">
		<@n4.safetyNetworkSmartSearch name="linkedProduct" theme="fieldidSimple" refreshRegistration="${refreshRegistration?string}"/>
	</div>
</div>	
	
<#if userSecurityGuard.allowedManageSafetyNetwork == true && publishedState?exists>
	<div class="infoSet">
		<label for="publishedState" class="label"><@s.text name="label.publishedstateselector"/>  ${refreshRegistration?string}</label>
		<@s.select name="publishedState" list="publishedStates" listKey="id" listValue="name" />
	</div>
</#if>
