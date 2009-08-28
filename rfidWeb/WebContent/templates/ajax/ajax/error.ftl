var errors =  '';

<#list action.actionErrors as error >
	errors += '${error!} ' + "\n";
</#list>
alert( errors );

<@s.fielderror/>