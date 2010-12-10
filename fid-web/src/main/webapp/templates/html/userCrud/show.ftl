${action.setPageType('user','view')!}

<head>
	<@n4.includeStyle href="user" type="page"/>
</head>

<div class="viewactions">
	<#if !user.isAdmin()>
	<div class="useractions delete">
		<p>
			<a href="<@s.url action="${user.employee?string('employeeUserDelete', 'readOnlyUserDelete')}" uniqueID="${(user.id)!}" />" 
			onclick="return confirm('${action.getText( 'warning.deleteuser',"", user.userID )}');"><@s.text name="label.delete_account"/></a>
		<p>
	</div>
	</#if>
	<div class="useractions changeaccount">
		<p><a href="<@s.url action="upgradeUser" uniqueID="${user.id!}" />"><@s.text name="label.change_account_type"/></a></p>
		
	</div>
	<div class="useractions email">
		<p><a href="<@s.url action="sendWelcomeEmail" uniqueID="${user.id!}" />"><@s.text name="label.send_welcome_email"/></a></p>
	</div>
</div>

<div class="leftColumn">
	<div class="userDetails viewSection smallViewSection">
		<h2><@s.text name="label.user_details"/></h2>
		<p>
			<label for="owner"><@s.text name="label.owner"/></label>
			<span class="fieldValue">${owner.name}</span>
		</p>
		<p>
			<label for="email"><@s.text name="label.emailaddress"/></label>
			<span class="fieldValue"><a href="mailto:${emailAddress}">${emailAddress}</a></span>
		</p>
		<p>
			<label for="name"><@s.text name="label.name"/></label>
			<span class="fieldValue">${firstName} ${lastName}</span>
		</p>
		<p>
			<label for="initials"><@s.text name="label.initials"/></label>
			<span class="fieldValue">${initials!}
			</span>
		</p>
		<p>
			<label for="position"><@s.text name="label.position"/></label>
			<span class="fieldValue">${position!}</span>
		</p>
		<p>
			<label for="digitalSignature"><@s.text name="label.digital_signature"/></label>
			<span id="imageUploaded" class="fieldHolder" <#if !signature.existingImage>style="display:none"</#if>  >
					<span id="uploadedImage" <#if signature.existingImage && !signature.newImage> style="display:none"</#if>><@s.text name="label.image_uploaded"/></span>
					<img id="imagePreview" <#if !signature.existingImage || signature.newImage> style="display:none"</#if> src="<@s.url action="downloadUserSignature" userId="${uniqueID!}" namespace="/file"/>" height="50" width="200" alt="<@s.text name="label.signature"/>"/>
			</span>
		</p>
	</div>
	<div class="localization viewSection smallViewSection">
		<h2><@s.text name="label.localization"/></h2>
		<p>
			<label for="country"><@s.text name="label.country"/></label>
			<span class="fieldValue">${countryName}</span>
		</p>
		<p>
			<label for="timezone"><@s.text name="label.timezone"/></label>
			<span class="fieldValue">${timeZoneName}</span>
		</p>
	</div>
</div>

<div class="rightColumn">
	<div class="account viewSection smallViewSection">
		<h2><@s.text name="label.account_heading"/></h2>
		<p>
			<label for="accountType"><@s.text name="label.accounttype"/></label>
			<span class="fieldValue">${user.userType.label}</span>
		</p>
		<p>
			<label for="lastlogin"><@s.text name="label.lastlogin"/></label>
			<span class="fieldValue">${(action.dateCreated(user)??)?string(action.formatDateTime(action.dateCreated(user)), "--")}</span>
		</p>
		<p>
			<label for="username"><@s.text name="label.username"/></label>
			<span class="fieldValue username">${userId}</span>
		</p>
	</div>
	<#if user.employee>
		<div class="viewpermissions viewSection smallViewSection">
			<h2><@s.text name="label.permissions"/></h2>
			
			<#if user.liteUser>
				<#assign permissionsList = litePermissions >			
			<#else>
				<#assign permissionsList = permissions >			
			</#if>
			<#list permissionsList as permission >
				<p>
					<label><@s.text name="${permission.name}"/></label>
						<#if action.getUserPermissionValue(permission.id)>
							<span class="fieldValue">Yes</span>
						<#else>
							<span class="fieldValue no">No</span>
						</#if>
					</span>
				</p>
			</#list>
		</div>
	</#if>
</div>