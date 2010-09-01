 <p> you can only have integration or job sites selected as extended features.</p>
<@s.form action="organizationUpdate" namespace="/admin" method="post">
	<@s.hidden name="id" value="%{id}" /> 
	<@s.fielderror/>
	
	
	<@s.textfield name="primaryOrg.serialNumberFormat"  label="Serial Number Format" />
	<@s.textfield name="primaryOrg.dateFormat" label="Date Format" />
	<@s.label name="otherDateFormat" label="Other Date Format" />
	<@s.label name="formattedDate" label="Example Date" />
	<@s.textfield name="diskSpace" label="Disk space limit in bytes (-1 for unlimited)" />
	<@s.textfield name="assets" label="Asset limit (-1 for unlimited)" />
	<@s.textfield name="users" label="User limit (-1 for unlimited)" />
	<@s.textfield name="secondaryOrgs" label="Secondary Orgs limit (-1 for unlimited)" />

	
	<@s.label name="extendedFeaturesLabel" label="Extended features (if you change any, add a note below)" />
	
	<@s.iterator  value="availableExtendedFeatures" >
		<@s.checkbox name="extendedFeatures['%{name}']" >
			<@s.param name="label"><@s.property value="name"/></@s.param>
		</@s.checkbox> 
	</@s.iterator>	
	
	<@s.label label="Extras" />
	<@s.checkbox name="primaryOrg.plansAndPricingAvailable" label="Show Plans and pricing when Partner Center enabled" labelposition="left" />
	
	
	<@s.submit />
	<@s.submit value="Cancel" name="redirectAction:organizations" />
</@s.form>
<pre>
Serial Number Format Options.
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
<br />
<#if id?exists>
<@s.form action="organizationNote" namespace="/admin" method="post">
	<@s.hidden name="id" value="${id}" />
	<@s.actionmessage />
	<@s.actionerror />
	
	<@s.textfield name="title" label="Title" />
	<@s.textarea name="note" label="Note" cols="50" rows="10" />
	<@s.submit />
</@s.form>
</#if>