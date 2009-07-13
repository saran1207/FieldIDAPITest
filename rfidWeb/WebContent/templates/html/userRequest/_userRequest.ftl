<div class="view">

	<div>
		<span class="label "><@s.text name="label.created"/> </span> <span class="field ">${action.formatDateTime(userRequest.created)}</span>
	</div>

	<div>
		<span class="label "><@s.text name="label.userid"/> </span> <span class="field ">${userRequest.userAccount.userID}</span>
	</div>
	<div>
		<span class="label "><@s.text name="label.emailaddress"/> </span> <span class="field ">${userRequest.userAccount.emailAddress}</span>  
	</div>
	<div>	
			<span class="label "><@s.text name="label.firstname"/> </span> <span class="field ">${userRequest.userAccount.firstName}</span>  
	</div>
	<div>
			<span class="label "><@s.text name="label.lastname"/> </span> <span class="field ">${userRequest.userAccount.lastName}</span>  
	</div>
	<div>
			<span class="label "><@s.text name="label.position"/> </span> <span class="field ">${userRequest.userAccount.position!}</span>  
	</div>
	<div>
			<span class="label "><@s.text name="label.timezone"/> </span> <span class="field ">${userRequest.userAccount.timeZoneID}</span>  
	</div>
	<div>
		
			<span class="label "><@s.text name="label.companyname"/> </span> <span class="field ">${userRequest.companyName}</span>  
	</div>
	<div>
			<span class="label "><@s.text name="label.phonenumber"/> </span> <span class="field ">${userRequest.phoneNumber}</span>  
	</div>
					
	<div>
			<span class="label "><@s.text name="label.comments"/> </span> <span class="field ">${userRequest.comment}</span>  
	</div>		
</div>