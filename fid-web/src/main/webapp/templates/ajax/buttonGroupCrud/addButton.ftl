<#escape x as x?j_string >
<#assign button_index=buttonIndex/>

<#assign html>
	<#include "/templates/html/buttonGroupCrud/_buttonForm.ftl"/>	
</#assign>
	
	$('buttonStates__${buttonGroupIndex}').insert( "${html}" );
	$('buttonState__${buttonGroupIndex}_${buttonIndex}').hide();
	$('buttonState__${buttonGroupIndex}_${buttonIndex}').show();
	enableGroupSaveButton( ${buttonGroupIndex} );

</#escape>