<title>
	<@s.text name="title.register" />
	
</title>


<@s.form action="registerUser!save" cssClass="inputForm" theme="css_xhtml" >
	<div class="formRowHolder">
		<@s.textfield key="label.userid" name="userId" labelposition="left" required="true"/>
		<@s.textfield key="label.emailaddress" name="emailAddress" labelposition="left" required="true"/>
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.firstname" name="firstName" labelposition="left" required="true"/>
		<@s.textfield key="label.lastname" name="lastName" labelposition="left" required="true"/>
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.position" name="position" labelposition="left"/>
		<@s.select key="label.timezone" name="timeZone" list="timeZones" listKey="id" listValue="name" labelposition="left"/>
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.companyname" name="companyName" labelposition="left" required="true"/>
		<@s.textfield key="label.phonenumber" name="phoneNumber" labelposition="left" required="true"/>
	</div>
	
	<div class="formRowHolder">
		<@s.password key="label.password" name="password" labelposition="left" required="true"/>
		<@s.password key="label.vpassword" name="passwordConfirmation" labelposition="left" required="true"/>
	</div>
	<div class="formRowHolder">
		<@s.textarea key="label.comments" name="comment" labelposition="left"/>
		
	</div>
	
	<div class="formAction">
		<a href="<@s.url action="login"/>" ><@s.text name="error.page.sign.link"/></a>
		<@s.reset key="hbutton.reset" />
		<@s.submit name="save" key="hbutton.submit" />
		
	</div>
	
</@s.form >

