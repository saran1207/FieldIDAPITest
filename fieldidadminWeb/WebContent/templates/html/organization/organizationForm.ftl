 <p> you can only have integration or job sites selected as extended features.</p>
<@s.form action="organizationCrud!save" method="post" enctype="multipart/form-data">
	<@s.hidden name="id" value="%{id}" /> 
	<@s.fielderror/>
	
	<@s.textfield name="tenant.name" value="%{tenant.name}" label="Name (Site Address)" />
	<@s.textfield name="primaryOrg.name" value="%{primaryOrg.name}" label="Display Name" />
	<@s.textfield name="primaryOrg.serialNumberFormat" value="%{primaryOrg.serialNumberFormat}" label="Serial Number Format" />
	<@s.textfield name="primaryOrg.dateFormat" value="%{primaryOrg.dateFormat}" label="Date Format" />
	<@s.textfield name="primaryOrg.webSite" value="%{primaryOrg.webSite}" label="Web Site url" />
	<@s.label name="otherDateFormat" label="Other Date Format" />
	<@s.label name="formattedDate" label="Example Date" />

	<@s.textfield name="diskSpaceMB" label="Max Disk Space (MB) (-1 for Unlimited)" />
	<@s.textfield name="primaryOrg.limits.users" label="Max Employee Users (-1 for Unlimited)" />
	<@s.textfield name="primaryOrg.limits.assets" label="Max Assets (-1 for Unlimited)" />
	
	<#if !id?exists>
		<@s.textfield name="adminUser.userID" label="Admin UserID" />
		<@s.password name="adminUserPass" label="Admin Pass" />
		<@s.textfield name="adminUser.firstName"  label="First Name" />
		<@s.textfield name="adminUser.lastName" label="Last Name" />
		<@s.textfield name="adminUser.emailAddress" label="Admin Email" />
	</#if>
	
	<@s.iterator  value="availableExtendedFeatures" >
		<@s.checkbox name="extendedFeatures['%{name}']" >
			<@s.param name="label"><@s.property value="name"/></@s.param>
		</@s.checkbox> 
	</@s.iterator>
	<@s.file name="logoFile" label="Logo file" />
	<@s.file name="certificateLogoFile" label="Certificate logo file" />
	<@s.submit />
	<@s.submit value="Cancel" name="redirect-action:organizationList" />
</@s.form>
<pre>
Serial Number Format Option@s.
%m = Month as a 2 digit number
%d = Day of month as a 2 digit number
%Y = 4 digit year
%y = 2 digit year
%H = Hour of day
%M = Minute of day
%S = Second of day
%L = Millisecond of day
%j = Jergen's style date code
%g = Autoincrementing counter (defaults to 6 digits, reset every year)
</pre>
