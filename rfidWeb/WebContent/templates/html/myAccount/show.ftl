<head>
	<script type="text/javascript" src="<@s.url value="/javascript/myaccount.js"/>"></script>
	<link rel="StyleSheet" href="/fieldid/style/viewTree.css" type="text/css"/>
</head>

${action.setPageType('my_account', 'details')!}
<div id="accountDetails" class="crudForm largeForm bigForm pageSection">
	<h2><@s.text name="label.accountdetails"/></h2>
	<@s.hidden id="uniqueID" name="uniqueID"/>
	<div class="sectionContent">
		<div class="twoColumn" >
			<div class="infoSet">
				<label for="userName"><@s.text name="label.username"/></label>
				<span>${(currentUser.userID?html) !}</span>
			</div>
			
			<div class="infoSet">
				<label for="firstName"><@s.text name="label.firstname"/></label>
				<span>${(currentUser.firstName?html) !}</span>
			</div>
			
			<div class="infoSet">
				<label for="position"><@s.text name="label.position"/></label>
				<span>${(currentUser.position?html) !}</span>
			</div>	
			
			<#if sessionUser.anEndUser >
				<div class="infoSet">
					<label for="customer"><@s.text name="label.customer"/></label>
					<span>${(currentUser.customer.name?html) !}</span>
				</div>
				<div class="infoSet">
					<label for="division"><@s.text name="label.division"/></label>
					<span>${(currentUser.division.name?html) !}</span>
				</div>
			<#else>
				<div class="infoSet">
					<label for="organizationalUnit"><@s.text name="label.organizationalunit"/></label>
					<span>${(currentUser.organization.displayName?html) !}</span>
				</div>
			</#if>
		</div>
		
		<div class="twoColumn lastColumn" >
			<div class="infoSet">
				<label for="email"><@s.text name="label.emailaddress"/></label>
				<span>${currentUser.emailAddress?html}</span>
			</div>
			<div class="infoSet">
				<label for="lastname"><@s.text name="label.lastname"/> </label>
				<span>${(currentUser.lastName?html) !}</span>
			</div>
			
			<div class="infoSet">
				<label for="initials"><@s.text name="label.initials"/></label>
				<span>${(currentUser.initials?html) !}</span>
			</div>
		</div>
	</div>
</div>


