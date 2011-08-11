<@s.text name="label.signup_details"/> |
<a href="javascript:void(0);" onClick="editSignUp(${id});">
	<@s.text name="label.edit"/>
</a>

<br>

<label class="bold">External User Name: </label>${primaryOrg.externalUserName!}<br/>
<label class="bold">External Password: </label>${primaryOrg.externalPassword!}<br/>
<label class="bold">Plan: </label>${primaryOrg.signUpPackage.displayName}<br/>

