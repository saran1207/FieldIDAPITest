<div class="formBody">
	<@s.form action="saveRegNetworkAsset" namespace="/aHtml/iframe" theme="fieldid" cssClass="fullForm fluidSets" method="post"> 
		
		<#include '_basicForm.ftl'/>
		
		<div class="expand">
			<p>
				<img src="<@s.url value="/images/plus.png"/>" alt"+">
				<a id="expand_details" onclick="openSection('extraDetails', 'expand_details', 'collapse_details');return false" href="javascript:void(0);" ><@s.text name="label.addmoredetails"/></a>
				<a id="collapse_details" onclick="closeSection('extraDetails', 'collapse_details', 'expand_details');return false" href="javascript:void(0);" style="display:none"><@s.text name="label.addlessdetails"/></a>
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
