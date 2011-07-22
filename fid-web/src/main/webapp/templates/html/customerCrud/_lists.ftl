<#if userLimitService.readOnlyUsersEnabled>
	<div id="users_container" style="display:none" >
		<#include "../userCrud/_userList.ftl" />
		<div class="actions">
			<a href="<@s.url value="readOnlyUserEdit.action" uniqueID="" customer="${uniqueID}"/>"><@s.text name="label.addreadonlyuser" /></a>
		</div>
	</div>	
</#if>