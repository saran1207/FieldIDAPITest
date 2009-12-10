var errors =  '';

<#list action.actionErrors as error >
	errors += '${error!} ' + "\n";
</#list>
<#list action.fieldErrors?values as errorField >
	errors += '${errorField!} ' + "\n";
</#list>


alert(errors);

