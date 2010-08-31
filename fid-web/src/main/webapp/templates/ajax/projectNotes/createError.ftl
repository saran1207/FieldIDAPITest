
<#if !action.fieldErrors.isEmpty()  >
	var errors = ${json.toJson( action.fieldErrors ) };
	var form = $( 'addNote' ); 
	
	
	
	cleanAjaxForm( form, "addNote_" );
	
	
	<#assign html>
		<#assign errorPrefix="addNote_"/>
		<#include "/templates/html/common/_formErrors.ftl"/>
	</#assign>
	
	form.insert( { top: '${html?js_string}' } );
	
	$( 'addNote_formErrors' ).highlight();
	
	var inputs = form.getElements();
	
	for( var i = 0; inputs.size() > i; i++ ) {
		var input = inputs[i];
		if( errors[ input.name ] != null ) {
			input.addClassName( "inputError" );
			input.title= errors[ input.name ];
		}
	}
	
<#else>
	<#include "../ajax/error.ftl"/>

</#if>
${action.clearFlashScope()}