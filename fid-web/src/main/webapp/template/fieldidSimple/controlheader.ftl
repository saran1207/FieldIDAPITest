<#assign hasFieldErrors = parameters.name?exists && fieldErrors?exists && fieldErrors[parameters.name]?exists/>
<#if hasFieldErrors >
	<#if !parameters.cssClass?exists > 
		${(parameters.put('cssClass',"")?contains(" ")?string("","") )!}
	</#if>
	${( parameters.put('cssClass', parameters.cssClass + " inputError" )?contains(" ")?string("","") )! }
	
  	${ (parameters.put( 'title', fieldErrors[parameters.name] )?contains(" ")?string("","") )! }
</#if>