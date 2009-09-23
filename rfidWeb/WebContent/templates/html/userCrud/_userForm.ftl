<#include "/templates/html/common/_orgPicker.ftl"/>

<@s.form action="${userSaveAction}" cssClass="fullForm" theme="fieldid" >
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden name="uniqueID" />
	<@s.hidden name="customerId"/>
	<@s.hidden name="currentPage" />
	<@s.hidden name="listFilter" />
	<@s.hidden name="userType" />
	<div class="multiColumn">
		<div class="infoBlock">
			<div class="infoSet">
				<label class="label" for="userId"><@s.text name="label.userid"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				<@s.textfield name="userId"/>
			</div>
			<div class="infoSet">
				<label class="label" for="emailAddress"><@s.text name="label.emailaddress"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				<@s.textfield key="label.emailaddress" name="emailAddress"  required="true"/>
			</div>
			
			<#if user.new >
				<div class="infoSet">
					<label class="label" for="securityRfidNumber"><@s.text name="label.securityrfidnumber"/></label>
					<@s.textfield key="label.securityrfidnumber" name="securityRfidNumber" />
				</div>
				
				<div class="infoSet">
					<label class="label" for="passwordEntry.password"><@s.text name="label.password"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
					<@s.password  name="passwordEntry.password"  required="true"/>
				</div>
				
				<div class="infoSet">
					<label class="label" for="passwordEntry.passwordVerify"><@s.text name="label.vpassword"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
					<@s.password name="passwordEntry.passwordVerify"  required="true"/>
				</div>
			</#if>
			
			<div class="infoSet">
				<label class="label" for="owner"><@s.text name="label.owner"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				<@n4.orgPicker name="owner" required="true" orgType="${(employee)?string('internal','external')}"/>
			</div>
		</div>
		
		<div class="infoBlock">
			<div class="infoSet">
				<label class="label" for="firstname"><@s.text name="label.firstname"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				<@s.textfield id="firstname" name="firstName"  required="true"/>
			</div>
			<div class="infoSet">
				<label class="label" for="lastname"><@s.text name="label.lastname"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				<@s.textfield id="lastname" name="lastName"  required="true"/>
			</div>
			<div class="infoSet">
				<label class="label" for="initials"><@s.text name="label.initials"/></label>
				<@s.textfield id="initials" name="initials"  ondblclick="updateInitials()" />
			</div>
			<div class="infoSet">
				<label class="label" for="position"><@s.text name="label.position"/></label>
				<@s.textfield  name="position" />
			</div>
			
			<div class="infoSet">
				<label class="label" for="countryId"><@s.text name="label.country"/></label>
				<@s.select  name="countryId" list="countries" listKey="id" listValue="displayName"  cssClass="changesTimeZone"/>
			</div>
			<div class="infoSet">
				<label class="label" for="timeZoneID"><@s.text name="label.timezone"/></label>
				<@s.select id="tzlist" name="timeZoneID" list="timeZones" listKey="id" listValue="displayName"  emptyOption="false"/>
			</div>
		</div>
	</div>
	
	<#if !user.new >
		<div class="formRowHolder uploadHolder">
			<iframe src="<@s.url action="userUploadSignature"  uniqueID="${uniqueID}" />" scrollbar='no' width="100%" height="110px" style="border:0px; overflow:none;" frameborder="0"></iframe>
		</div>
	</#if>
	
	<#if !user.admin && employee>
		<div class="infoBlock">
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
	
<div class="actions">
		<@s.submit name="save" key="label.save" />
		<@s.text name="label.or"/>
		${backToList}
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
