
<#include "/templates/html/common/_orgPicker.ftl"/>

<div id="acceptForm" style="width:652px">
	<@s.form  action="userRequestAccept" cssClass="fullForm oneColumn" theme="fieldid" >
		<div class="infoSet">
			<label class="label"><@s.text name="label.companyname"/> </label>
			<span class="fieldHolder">${userRequest.companyName?html}</span>
			
		</div>
		<@s.hidden name="uniqueID" />
		
		
		<div class="infoSet">
			<label class="label"><@s.text name="label.owner"/></label>
			<@n4.orgPicker name="owner" required="true" orgType="external"/>
		</div>
		
	
		
		<div class="formAction">
						
			<@s.submit key="hbutton.accept" onclick="return formSubmit();"/>
			<@s.text name="label.or"/>
			<href<@s.submit type="button" key="hbutton.cancel" onclick="formCancel(); return false"/>
		</div>
	</@s.form>
	

</div>