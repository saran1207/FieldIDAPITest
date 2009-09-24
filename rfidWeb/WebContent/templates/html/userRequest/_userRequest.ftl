<div class="fluidSets">

	<div class="infoSet">
		<label class="label "><@s.text name="label.created"/></label> 
		<span class="fieldHolder">${action.formatDateTime(userRequest.created)}</span>
	</div>

	<div class="infoSet">
		<label class="label "><@s.text name="label.userid"/></label> 
		<span class="fieldHolder">${userRequest.userAccount.userID}</span>
	</div>
	<div class="infoSet">
		<label class="label "><@s.text name="label.emailaddress"/></label> 
		<span class="fieldHolder">${userRequest.userAccount.emailAddress}</span>  
	</div>
	<div class="infoSet">	
		<label class="label "><@s.text name="label.firstname"/></label> 
		<span class="fieldHolder">${userRequest.userAccount.firstName}</span>  
	</div>
	<div class="infoSet">
		<label class="label "><@s.text name="label.lastname"/></label> 
		<span class="fieldHolder">${userRequest.userAccount.lastName}</span>  
	</div>
	<div class="infoSet">
		<label class="label "><@s.text name="label.position"/></label> 
		<span class="fieldHolder">${userRequest.userAccount.position!}</span>  
	</div>
	<div class="infoSet">
		<label class="label "><@s.text name="label.timezone"/></label> 
		<span class="fieldHolder">${userRequest.userAccount.timeZoneID}</span>  
	</div>
	<div class="infoSet">
		<label class="label "><@s.text name="label.companyname"/></label> 
		<span class="fieldHolder">${userRequest.companyName}</span>  
	</div>
	<div class="infoSet">
		<label class="label "><@s.text name="label.phonenumber"/></label> 
		<span class="fieldHolder">${userRequest.phoneNumber}</span>  
	</div>
					
	<div class="infoSet">
		<label class="label "><@s.text name="label.comments"/></label> 
		<span class="fieldHolder">${userRequest.comment}</span>  
	</div>		
</div>