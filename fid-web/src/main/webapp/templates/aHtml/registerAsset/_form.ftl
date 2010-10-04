<div class="formBody">
	<@s.form action="saveRegNetworkAsset" namespace="/aHtml/iframe" theme="fieldid" cssClass="fullForm fluidSets" method="post"> 
		
		<#include '_basicForm.ftl'/>
		
		<div class="expand">
			<p>
				<img id="plus" src="<@s.url value="/images/plus.png"/>" alt="+" />
				<img id="minus" src="<@s.url value="/images/minus.png"/>" alt="-" style="display:none"/>
				<a id="expand_details" onclick="openSection('extraDetails', 'expand_details', 'collapse_details');plus.style.display='none';minus.style.display='';return false" href="javascript:void(0);" ><@s.text name="label.addmoredetails"/></a>
				<a id="collapse_details" onclick="closeSection('extraDetails', 'collapse_details', 'expand_details');plus.style.display='';minus.style.display='none';return false" href="javascript:void(0);" style="display:none"><@s.text name="label.addlessdetails"/></a>
			</p>
		</div>
		
		<#include '_detailedForm.ftl' />
				
		<div id="actions">
			<p>
				<@s.submit id="saveButton" key="hbutton.registerthisasset" />
				<@s.text name="label.or"/>
				<a href="" onclick="closeLightBox();return false;"><@s.text name="label.cancel"/></a>
			</p>
		</div>
		
	</@s.form>
</div>
