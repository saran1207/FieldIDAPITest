${action.setPageType('user_registration', 'list')!}

<#if userRequests.size() != 0 >
	<table class="list">
		<tr>
			<th><@s.text name="label.companyname" /></th>
			<th><@s.text name="label.userid" /></th>
			<th><@s.text name="label.firstname" /></th>
			<th><@s.text name="label.lastname" /></th>
			
			<th><@s.text name="label.comment" /></th>
			<th><@s.text name="label.created" /></th>
			<th></th>
		</tr>
		
		<#list userRequests as userRequest >
			<tr id="${userRequest.userAccount.userID?html}">
				<td>${userRequest.companyName}</td>
				<td>${userRequest.userAccount.userID}</td>
				<td>${userRequest.userAccount.firstName!}</td>
				<td>${userRequest.userAccount.lastName!}</td>
				<td>${userRequest.comment!}</td>
				<td>${action.formatDateTime(userRequest.created)}</td>
				<td>
					<a href="<@s.url value="userRequestView.action" uniqueID="${userRequest.id}" />" id="viewRequest_${userRequest.userAccount.userID?html}" ><@s.text name="link.view" /></a>
				</td>
			</tr>
		</#list>
	</table>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptylistuserregistrations" />
		</p>
	</div>
</#if>

