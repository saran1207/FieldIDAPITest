<#escape x as x?j_string >
<#assign state_index=buttonIndex/>

<#assign html>
	<#include "/templates/html/buttonGroupCrud/_buttonForm.ftl"/>	
</#assign>
	
	$('buttonStates__${buttonGroupIndex}').insert( "${html}" );
	$('buttonState__${buttonGroupIndex}_${buttonIndex}').hide();
	Effect.Appear( 'buttonState__${buttonGroupIndex}_${buttonIndex}', { duration: 0.75} );
	enableGroupSaveButton( ${buttonGroupIndex} );

</#escape>