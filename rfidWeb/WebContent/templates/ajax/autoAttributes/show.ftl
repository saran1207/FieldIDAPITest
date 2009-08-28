<#escape x as x?j_string >
	<#assign results = { "templateFound": autoAttributeCriteria?exists , "fields": lookUpOutputs } >
</#escape>	

var templateInfo = ${ json.toJson( results ) }
populateFromAutoAttributes( templateInfo );
