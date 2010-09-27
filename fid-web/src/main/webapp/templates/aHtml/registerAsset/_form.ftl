<div class="formBody">
	<@s.form action="saveRegNetworkAsset" namespace="/aHtml/iframe" theme="fieldid" cssClass="fullForm fluidSets" method="get"> 
		
		<#include '_basicForm.ftl'/>
		
		<#include '_detailedForm.ftl' />
		
		<div id="actions">
			<@s.submit id="saveButton" key="hbutton.save" />
			<@s.text name="label.or"/>
			<button onclick="closeLightBox();return false;"><@s.text name="label.cancel"/></button>
		</div>
	</@s.form>
</div>
