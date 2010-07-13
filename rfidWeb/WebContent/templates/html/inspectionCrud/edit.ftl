<#assign form_action="EDIT" /> 
${action.setPageType('inspection', 'edit')!}

<@s.form action="inspectionUpdate" cssClass="fullForm fluidSets" theme="fieldid" onsubmit="return checkForUploads()" >
	<#include "_form.ftl"/>

	<div class="actions">
		<@s.submit key="hbutton.save" /> | 
		<@s.reset id="delete" key="label.delete"/> 
		<@s.text name="label.or"/>
		<a href="<@s.url action="inspection" uniqueID="${uniqueID}" productId="${productId}" />"><@s.text name="label.cancel"/></a>
	</div>
</@s.form>
<head>
	<@n4.includeScript>
		onDocumentLoad(function() {
			$('delete').observe('click', function(event) {
					event.stop();
					redirect('<@s.url action="inspectionDelete" uniqueID="${uniqueID}" productId="${productId}" />' );
				});
		});
	</@n4.includeScript>
</head>