<#escape x as x?js_string >
	
	<#assign html>
		<#include "/templates/html/buttonGroupCrud/_form.ftl" >
	</#assign>
	$('stateSet_${buttonGroupIndex}').replace( "${html}" );
	buttonsUpdated[ 'state_${buttonGroupIndex}' ] = false;
	<#if !fieldErrors.isEmpty() || !actionErrors.isEmpty() >
		enableGroupSaveButton( ${buttonGroupIndex} );
		
		${ action.clearFlashMessages()! }${ action.clearFlashErrors()! }
			
	</#if>
	<#noescape>
		var messages = ${ json.toJSON( actionMessages ) };
		var errors = ${ json.toJSON( actionErrors ) };
	</#noescape>
	updateMessages( messages, errors, "${buttonGroupIndex}" );
</#escape>