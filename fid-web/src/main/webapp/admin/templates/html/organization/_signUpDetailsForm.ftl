<@s.form id="signUpDetailsForm" action="updateSignUp" namespace="/adminAjax" theme="fieldidSimple" >
	<@s.hidden name="id"/>
	<label class="bold">External User Name: </label><br/><span><@s.textfield name="primaryOrg.externalUserName" theme="fieldidSimple" /></span><br/>
    <label class="bold">External Password: </label><br/><span><@s.textfield name="primaryOrg.externalPassword" theme="fieldidSimple" /></span><br/>
    <label class="bold">Plan: </label> <@s.select list="signUpPackages" name="signUpPlan" listValue="name" listKey="name" /><br/>

	<div class="signUpActions">
		<input id="updateSignUpButton" type="button" onClick="updateSignUpFoo();" value="<@s.text name='label.save'/>"/>
		<@s.text name="label.or"/>
		<a href="javascript:void(0);" onClick="cancelSignUp(${id});"><@s.text name="label.cancel"/></a>
		<img id="signUpFormLoading" class="loading" src="<@s.url value="../images/loading-small.gif"/>" />
	</div>
</@s.form>