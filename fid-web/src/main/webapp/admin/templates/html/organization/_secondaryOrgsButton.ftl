<td <#if secondaryOrgsEnabled> class='onIcon'<#else> class='offIcon'</#if>></td>
<td>
	<@s.form id="secondaryOrgsForm" action="updateSecondaryOrgs" namespace="/adminAjax" theme="fieldidSimple">
		<@s.hidden name="id"/>
		<#if secondaryOrgsEnabled> 
			<@s.hidden name="tenant.settings.secondaryOrgsEnabled" value="false" />
		<#else> 
			<@s.hidden name="tenant.settings.secondaryOrgsEnabled" value="true" />
		</#if>

		<#if superUser>
		<a href="javascript:void(0);" onClick="saveSecondaryOrgs();">
			<#if secondaryOrgsEnabled>on<#else>off</#if>
		</a>
		</#if>
	</@s.form>
</td>
<td class="featureLabel"><@s.text name="label.secondaryOrgs"/></td>
<td> <img id="loading_secondaryOrgs" class="loading" src="<@s.url value="../images/loading-small.gif"/>" /><td>