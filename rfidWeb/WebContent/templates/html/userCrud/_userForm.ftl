<#include "/templates/html/common/_orgPicker.ftl"/>

<@s.form action="${userSaveAction}" cssClass="inputForm" theme="css_xhtml" >
	
	<@s.hidden name="uniqueID" />
	<@s.hidden name="currentPage" />
	<@s.hidden name="listFilter" />
	<@s.hidden name="userType" />
	
	<div class="formRowHolder">
		<@s.textfield key="label.userid" name="userId" labelposition="left" required="true"/>
		<@s.textfield key="label.emailaddress" name="emailAddress" labelposition="left" required="true"/>
	</div>
	
	<div class="formRowHolder">
		<@s.textfield id="firstname" key="label.firstname" name="firstName" labelposition="left" required="true"/>
		<@s.textfield id="lastname" key="label.lastname" name="lastName" labelposition="left" required="true"/>
	</div>
	<div class="formRowHolder">
		<@s.textfield key="label.position" name="position" labelposition="left"/>
		<@s.textfield id="initials" key="label.initials" name="initials" labelposition="left" ondblclick="updateInitials()" />
	</div>
	
	<#if !uniqueID?exists >
		<div class="formRowHolder">
			<@s.textfield key="label.securityrfidnumber" name="securityRfidNumber" labelposition="left"/>
		</div>
	</#if>
	
	<div class="formRowHolder">
		<@s.select key="label.country" name="countryId" list="countries" listKey="id" listValue="displayName" labelposition="left" cssClass="changesTimeZone"/>
		<@s.select id="tzlist" key="label.timezone" name="timeZoneID" list="timeZones" listKey="id" listValue="displayName" labelposition="left" emptyOption="false"/>
	</div>

	<div class="formRowHolder">
		<label for="owner"><@s.text name="label.owner"/></label>
		<@n4.orgPicker name="owner" required="true" orgType="${(employee)?string('internal','external')}"/>
	</div>
	
	<#if !uniqueID?exists >	
		<div class="formRowHolder">
			<@s.password key="label.password" name="password" labelposition="left" required="true"/>
			<@s.password key="label.vpassword" name="passwordConfirmation" labelposition="left" required="true"/>
		</div>
	<#else>
		<@s.hidden name="password" value="123456" />
		<@s.hidden name="passwordConfirmation" value="123456"/>
	</#if>
	
	<#if uniqueID?exists >
		<div class="formRowHolder uploadHolder">
			<iframe src="<@s.url action="userUploadSignature"  uniqueID="${uniqueID}" />" scrollbar='no' width="100%" height="110px" style="border:0px; overflow:none;" frameborder="0"></iframe>
		</div>
	</#if>
	
	<#if !customerUser && !user.admin>
		<div>
			<@s.fielderror>
					<@s.param>userPermissions</@s.param>				
			</@s.fielderror>
			<table class="list" style="width:750px">
				
				<tr class="titleRow">
					<th class="permissionName"><@s.text name="label.permissions"/></th>
					<th class="radio"><@s.text name="label.on"/></th>
					<th class="radio"><@s.text name="label.off"/></th>
				</tr>
			
				<#list permissions as permission >
					<tr >
						<td class="permissionName">
							<@s.text name="${permission.name}"/>
						</td>
						<td class="radio">
							<@s.radio name="userPermissions['${permission.id}']" list="on"  theme="simple" cssClass="permissionOn"/>
						</td>
						<td class="radio">
							<@s.radio name="userPermissions['${permission.id}']" list="off"  theme="simple" cssClass="permissionOff"/>
						</td>
					</tr >
				</#list>
				<tr>
					<td class="permissionName"></td>
					<td class="radio"><input type="reset" name="allOn" onclick="allPermissionsOn();return false;" value="<@s.text name="label.allon"/>"/></td>
					<td class="radio"><input type="reset" name="allOff" onclick="allPermissionsOff();return false;" value="<@s.text name="label.alloff"/>"/></td>
				</tr>
			</table>
		</div>
	</#if>
	
	<div class="formAction">
		${backToList}
		<@s.submit name="save" key="hbutton.submit" />
	</div>
	
</@s.form >
<head>
	<style>
		.radio {
			width: 60px;
		}
	</style>
	<script type="text/javascript" src="<@s.url value="javascript/timezone.js" />"></script>
	<script type="text/javascript" >
		countryChangeUrl = "<@s.url action="getRegions" namespace="/ajax" />";
		
		function updateInitials() {
			var initials = $('initials');
			var firstName = $('firstname').value;
			var lastName = $('lastname').value;
			var newValue = "";
			
			if (firstName.length > 0) {
				newValue += firstName[0];
			}
			
			if (lastName.length > 0) {
				newValue += lastName[0];
			}
			
			if(newValue.length > 0) {
				initials.value=newValue.toLowerCase()
			}
		}
		
		
		function allPermissionsOn() {
			var onElements = $$('.permissionOn');
			for( var i = 0; i < onElements.length; i++ ) {
				onElements[i].checked=true;
			}
		
		}
		
		function allPermissionsOff() {
			var offElements = $$('.permissionOff');
			for( var i = 0; i < offElements.length; i++ ) {
				offElements[i].checked=true;
					
			} 
		}
	</script>
</head>
