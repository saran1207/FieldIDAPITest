 <p> you can only have integration or job sites selected as extended features.</p>
<@s.form action="organizationUpdate" method="post">
	<@s.hidden name="id" value="%{id}" /> 
	<@s.fielderror/>
	
	
	<@s.textfield name="primaryOrg.serialNumberFormat"  label="Serial Number Format" />
	<@s.textfield name="primaryOrg.dateFormat" label="Date Format" />
	<@s.label name="otherDateFormat" label="Other Date Format" />
	<@s.label name="formattedDate" label="Example Date" />

	
	
	<@s.submit />
	<@s.submit value="Cancel" name="redirect-action:organizations" />
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
