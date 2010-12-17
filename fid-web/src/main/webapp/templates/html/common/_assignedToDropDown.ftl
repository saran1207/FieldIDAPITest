<#list userGrouper.userOwners as owner>
	<@s.optgroup label="${owner?html}" list="%{userGrouper.getUsersForOwner('${owner?js_string}')}" listKey="id" listValue="displayName" />
</#list>
