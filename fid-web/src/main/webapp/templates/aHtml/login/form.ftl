<p class="instructions">
	<@s.text name="message.sessionexpired"/>
</p>

<p class="instructions">
	<@s.text name="message.enterpasswordlogbackin"/>
</p>
<div>
  <div class="message" >
      <@s.actionmessage />
  </div>
  <div class="error">
      <@s.actionerror />
  </div>
</div>
<@s.form action="createSession" namespace="/aHtml" id="quickLoginForm" theme="fieldid" cssClass="easyForm quickLoginForm">
	<#include "/templates/html/common/_formErrors.ftl" />
	<@s.hidden name="companyID" id="companyID"/>
	<@s.hidden name="signIn.userName" id="userName"/>
	
	<label class='label'><@s.text name="label.password"/></label>
	<@s.password name="signIn.password" id="password"/>
	<div class="actions">
		<@s.submit name="hbutton.login" id="loginButton"/>
		<@s.url action="login" namespace="/" id="cancelUrl"/>
		<@s.submit key="hbutton.back_to_login" onclick="redirect('${cancelUrl}');"/>
	</div>
		
</@s.form>

<script type="text/javascript"> 
	$('quickLoginForm').observe( 'submit', quickLoginSubmit );
</script>