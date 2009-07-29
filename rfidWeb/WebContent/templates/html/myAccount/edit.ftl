<head>
	<script type="text/javascript" src="<@s.url value="/javascript/myaccount.js"/>"></script>
	<link rel="StyleSheet" href="/fieldid/style/viewTree.css" type="text/css"/>
</head>

${action.setPageType('my_account', 'details')!}
<@s.form id="accountDetails" action="myAccountUpdate" cssClass="fullForm contentBlock" theme="fieldid">
	<h2 class="sectionTitle"><@s.text name="label.accountdetails"/></h2>
	<#include "../common/_formErrors.ftl"/>
	<div class="multiColumn fluidSets">
			<div class="infoSet infoBlock">
				<label for="userName" class="label"><@s.text name="label.username"/> <#include "../common/_requiredMarker.ftl"/></label>
				<@s.textfield name="userID"/>
			</div>
					
			<div class="infoSet infoBlock">
				<label for="email" class="label"><@s.text name="label.emailaddress"/> <#include "../common/_requiredMarker.ftl"/></label>
				<@s.textfield name="emailAddress"/>
			</div>	
			
			<div class="infoSet infoBlock">
				<label for="firstName" class="label"><@s.text name="label.firstname"/> <#include "../common/_requiredMarker.ftl"/></label>
				<@s.textfield name="firstName"/>
			</div>
			
			<div class="infoSet infoBlock">
				<label for="lastname" class="label"><@s.text name="label.lastname"/> <#include "../common/_requiredMarker.ftl"/></label>
				<@s.textfield name="lastName"/>
			</div>
			
			<div class="infoSet infoBlock">
				<label for="initials" class="label"><@s.text name="label.initials"/></label>
				<@s.textfield name="initials"/>
			</div>
			
			<div class="infoSet infoBlock">
				<label for="position" class="label"><@s.text name="label.position"/></label>
				<@s.textfield name="position"/>
			</div>	
			
			
		
		
		
		<#if sessionUser.anEndUser >
			<div class="infoSet infoBlock">
				<label for="customer" class="label"><@s.text name="label.customer"/></label>
				<span class="fieldHolder">${(customer.name?html) !}</span>
			</div>
			<div class="infoSet infoBlock">
				<label for="division" class="label"><@s.text name="label.division"/></label>
				<span class="fieldHolder">${(division.name?html) !}</span>
			</div>
		<#else>
			<div class="infoSet infoBlock">
				<label for="organizationalUnit" class="label"><@s.text name="label.organizationalunit"/></label>
				<span class="fieldHolder">${(organization.displayName?html) !}</span>
			</div>
		</#if>
	
	</div>
	<div class="actions">
		<@s.submit key="label.save" id="saveButton"/> <@s.text name="label.or"/> <a href="<@s.url action="myAccount"/>"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>


