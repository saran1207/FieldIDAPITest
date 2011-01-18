<#include "/templates/html/common/_orgPicker.ftl"/>
<div id="acceptForm">
	<@s.form action="userRequestAccept" cssClass="fullForm fluidSets" theme="fieldid" >
		<#include "../common/_formErrors.ftl"/>
		<p class="instructionsBox"><@s.text name="label.choose_the_users_company"/></p>
		<@s.hidden name="uniqueID" />
		<div class="infoSet">
			<label class="label"><@s.text name="label.companyname"/> </label>
			<span class="fieldHolder">${userRequest.companyName?html}</span>
		</div>
		
		
		<div class="infoSet">
			<label class="label"><@s.text name="label.owner"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
			<@n4.orgPicker name="owner" required="true" orgType="internal"/>
		</div>
		
		<div class="formAction">
			<@s.submit key="hbutton.accept"/>
			<@s.text name="label.or"/>
			<a href="<@s.url action="userRequestView" uniqueID="${uniqueID}"/>" id="cancelAccept"><@s.text name="hbutton.cancel"/></a>
			
		</div>
	</@s.form>
</div>

