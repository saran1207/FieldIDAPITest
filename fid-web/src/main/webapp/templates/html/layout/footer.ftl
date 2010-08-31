<#escape x as x?html >
	<div id="pageFooter">
		<div>
			<p>
				<a href="<@s.url action="editPassword" namespace="/"/>" ><@s.text name="label.changepassword" /></a> |
				<a href="http://www.n4systems.com/support" target="_blank"><@s.text name="label.help_support"/></a>  
			</p>
			<p>
				<#include "../common/_versionNumber.ftl" />
			</p>				
		</div>
		<#include "_poweredBy.ftl"/>
	</div>
</#escape>