<#escape x as x?html >
	<div id="pageFooter">
		<div>
			<p>
				<a href="<@s.url action="editPassword"/>" ><@s.text name="label.changepassword" /></a> |
				<a href="http://n4systems.helpserve.com/" target="_blank" ><@s.text name="label.support"/></a>  
			</p>
			<p>
				<#include "../common/_versionNumber.ftl" />
			</p>				
		</div>
		<#include "_poweredBy.ftl"/>
	</div>
</#escape>