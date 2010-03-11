<head>
	<@n4.includeStyle href="user" type="page"/>
	<@n4.includeScript src="timezone" />
	<@n4.includeScript src="user" />
	<@n4.includeScript>
		countryChangeUrl = "<@s.url action="getRegions" namespace="/ajax" />";
		signatureUploadUrl = "<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" typeOfUpload="userSignature"/>";
	</@n4.includeScript>
</head>
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
				<label class="label" for="owner"><@s.text name="label.owner"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				<@n4.orgPicker name="owner" required="true" orgType="${(employee)?string('internal','external')}"/>
			</div>
			
			<div class="infoSet">
				<label class="label" for="firstname"><@s.text name="label.firstname"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				<@s.textfield id="firstname" name="firstName"  required="true" cssClass="initalsInput"/>
			</div>
			<div class="infoSet">
				<label class="label" for="lastname"><@s.text name="label.lastname"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				<@s.textfield id="lastname" name="lastName"  required="true" cssClass="initalsInput"/>
			</div>
			<div class="infoSet">
				<label class="label" for="initials"><@s.text name="label.initials"/></label>
				<@s.textfield id="initials" name="initials"   />
			</div>
			
			<div class="infoSet">
				<label class="label" for="emailAddress"><@s.text name="label.emailaddress"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				<@s.textfield key="label.emailaddress" name="emailAddress"  required="true"/>
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
			
			<div class="infoSet">
				<label class="label" for="signature"><@s.text name="label.signature"/></label>
				<div id="imageUploadField" class="fieldHolder" <#if signature.existingImage>style="display:none"</#if> >
					<div>
						<@s.text name="label.signature_size"/>
					</div>
				</div>
				<div id="imageUploaded" class="fieldHolder" <#if !signature.existingImage>style="display:none"</#if>  >
					<span id="uploadedImage" <#if signature.existingImage && !signture.newImage> style="display:none"</#if>><@s.text name="label.image_uploaded"/></span>
					<img id="imagePreview" <#if !signature.existingImage || signture.newImage> style="display:none"</#if> src="<@s.url action="downloadUserSignature" userId="${uniqueID!}" namespace="/file"/>" height="50" width="200" alt="<@s.text name="label.signature"/>"/>
					<@s.hidden name="signature.removeImage" id="removeImage"/> <a href="removeImage" id="removeImageLink" onclick="removeUploadImage(); return false;"><@s.text name="label.remove"/></a>
					<@s.hidden name="signature.newImage" id="newImage"/>
					<@s.hidden name="signature.uploadDirectory" id="imageDirectory"/>
				</div>
			</div>
			
		</div>
		<div class="infoBlock">
			<div class="infoSet">
				<label class="label" for="userId"><@s.text name="label.userid"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
				<@s.textfield name="userId"/>
			</div>
			
			<#if user.new >
				<div class="infoSet">
					<div class="label">
						<label for="assignPassword"><@s.text name="label.assign_password"/></label> <@s.checkbox id="assignPassword" name="assignPassword"  theme="simple"/>
					</div>
				</div>
				<div id="passwords">
					<div class="infoSet">
						<label class="label" for="passwordEntry.password"><@s.text name="label.password"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
						<@s.password  name="passwordEntry.password"  required="true"/>
					</div>
					
					<div class="infoSet">
						<label class="label" for="passwordEntry.passwordVerify"><@s.text name="label.vpassword"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
						<@s.password name="passwordEntry.passwordVerify"  required="true"/>
					</div>
				</div>	
				<div class="infoSet">
					<label class="label" for="securityRfidNumber"><@s.text name="label.securityrfidnumber"/></label>
					<@s.textfield key="label.securityrfidnumber" name="securityRfidNumber" />
				</div>
			
			<#else>
				<div class="infoSet">
					<label class="label" for="passwordEntry.password"><@s.text name="label.password"/></label>
					<span class="fieldHolder">
						<@s.text name="label.go_to"/> <a href="<@s.url action="adminEditPassword" uniqueID="${uniqueID}"/>"><@s.text name="label.change_password_tab"/></a> <@s.text name="label.to_reset_password"/> 
					</span>
				</div>
			</#if>
			<#if !user.admin && employee>
				<div class="infoSet">
					<@s.fielderror>
							<@s.param>userPermissions</@s.param>				
					</@s.fielderror>
					<table class="permissions fieldHolder">
						
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
			<#elseif user.admin>
				<div class="infoSet">
					<label class="label" for="passwordEntry.password"><@s.text name="label.permissions"/></label>
					<span class="fieldHolder">
						<@s.text name="label.admin_user_always_have_full_permissions"/> 
					</span>
				</div>
			<#else>
				<div class="infoSet">
					<label class="label" for="passwordEntry.password"><@s.text name="label.permissions"/></label>
					<span class="fieldHolder">
						<@s.text name="label.customer_users_have_read_only_access"/>
					</span>
				</div>
			</#if>
		</div>
			
		
	</div>
	<#if user.new>
		<h2 class="header"><label for="welcomeMessage.sendEmail"><@s.text name="label.send_welcome_email"/></label> <@s.checkbox name="welcomeMessage.sendEmail" id="sendEmail" theme="simple"/></h2>
		<div class="infoSet" id="personalMessage">
			<label class="label" for="welcomeMessage.presonalMessage"><@s.text name="label.personal_message"/>  <@s.text name="label.optional"/></label>
			<@s.textarea name="welcomeMessage.personalMessage"/>
		</div>
	</#if>
	
	<div class="actions">
		<@s.submit name="save" key="label.save" />
		<@s.text name="label.or"/>
		${backToList}
	</div>
	
</@s.form >
