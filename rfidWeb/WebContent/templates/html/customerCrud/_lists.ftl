<#if securityGuard.partnerCenterEnabled>
	<div id="users_container" style="display:none" >
		<#include "../userCrud/_userList.ftl" />
		<div class="actions">
			<a href="<@s.url value="customerUserEdit.action" uniqueID="" customer="${uniqueID}"/>"><@s.text name="label.addcustomeruser" /></a>
		</div>
		
	</div>	
</#if>