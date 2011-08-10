<#assign form_action="EDIT" /> 
${action.setPageType('event', 'edit')!}

<@s.form action="eventUpdate" theme="simple" onsubmit="return checkForUploads()" >
	<#include "_form.ftl"/>
	<@s.url id="deleteUrl" action="eventDelete" uniqueID="${uniqueID}" assetId="${assetId}" />
	<div class="actions">
		<@s.submit key="hbutton.save" />
		<@s.text name="label.or"/>
		<a href="<@s.url action="event" uniqueID="${uniqueID}" assetId="${assetId}" />"><@s.text name="label.cancel"/></a>
		<@s.text name="label.or"/>
		<a href="#" onclick="javascript: if (window.confirm( '<@s.text name="label.confirm_event_delete"/>' )) return redirect( '${deleteUrl}' );" ><@s.text name='label.delete'/></a>
	</div>
	
</@s.form>
