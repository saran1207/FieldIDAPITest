<title><@s.text name="title.register" /></title>

<head>
	<script type="text/javascript" src="<@s.url value="javascript/timezone.js" />"></script>
	<script type="text/javascript">
		countryChangeUrl = "<@s.url action="getRegions" namespace="/ajax" />";
	</script>
</head>
<div id="mainContent">
	<div class="titleBlock">
		<h1><@s.text name="title.register"/></h1>
		<p class="titleSummary"><@s.text name="label.request_account"/></p>
	</div>
	
	<@s.form action="registerUserCreate" cssClass="fullForm" theme="fieldid" >
		<#include "/templates/html/common/_formErrors.ftl"/>
		<div class="multiColumn">
			<div class="infoBlock">
				<h2><@s.text name="label.company_details"/></h2>
				<div class="infoSet">
					<label class="label"><@s.text name="label.companyname"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
					<@s.textfield  name="companyName" id="companyName"/>
				</div>
				<div class="infoSet">
					<label class="label"><@s.text name="label.firstname"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
					<@s.textfield name="firstName" />
				</div>
				<div class="infoSet">
					<label class="label"><@s.text name="label.lastname"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
					<@s.textfield  name="lastName"  />
				</div>
				
				<div class="infoSet">
					<label class="label"><@s.text name="label.emailaddress"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
					<@s.textfield  name="emailAddress"  />
				</div>
				
				<div class="infoSet">
					<label class="label"><@s.text name="label.country"/></label>
					<@s.select name="countryId" list="countries" listKey="id" listValue="displayName" onchange="countryChanged(this); return false;"/>
				</div>
				<div class="infoSet">
					<label class="label"><@s.text name="label.timezone"/></label>
					<@s.select id="tzlist" name="timeZone" list="timeZones" listKey="id" listValue="displayName" emptyOption="false"/>
				</div>
				
				<div class="infoSet">
					<label class="label"><@s.text name="label.position"/></label>
					<@s.textfield  name="position" />
				</div>
				<div class="infoSet">
					<label class="label"><@s.text name="label.phonenumber"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
					<@s.textfield  name="phoneNumber"  />
				</div>
				<div class="infoSet">
					<label class="label"><@s.text name="label.comments"/></label>
					<@s.textarea name="comment" />
					
				</div>
			</div>
			<div class="infoBlock">
				<h2><@s.text name="label.sign_in_details"/></h2>
				<div class="infoSet">
					<label class="label"><@s.text name="label.user_name"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
					<@s.textfield name="userId"  />
				</div>
				<div class="infoSet">
					<label class="label"><@s.text name="label.password"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
					<@s.password  name="password"  />
				</div>
				<div class="infoSet">
					<label class="label"><@s.text name="label.vpassword"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
					<@s.password  name="passwordConfirmation"  />
				</div>
			</div>
		</div>
		<div class="actions">
			<@s.submit name="save" key="label.send_request" /> <@s.text name="label.or"/>  <a href="<@s.url action="login"/>"/><@s.text name="label.return_to_sign_in"/></a>
		</div>
		
	</@s.form >
</div>
<div id="secondaryContent">
	<h2><@s.text name="label.how_soon_can_i_login"/></h2>
	<p class="titleSummary"><@s.text name="label.how_soon_can_i_login.full"><@s.param>${securityGuard.tenant.displayName?html}</@s.param></@s.text></p>
	<h2><@s.text name="label.will_i_get_notified_when_i_can_sign_in"/></h2>
	<p class="titleSummary"><@s.text name="label.will_i_get_notified_when_i_can_sign_in.full"/></p>
</div>

<script type="text/javascript">
	$('companyName').select();
</script>