<#escape x as x?html >
	<div id="pageFooter">
		<div style="float:left">
			<p>
				<#include "../common/_versionNumber.ftl" />
			</p>
			<@n4.includeScript src="lang/googleTranslate.js"/>
            <div id="google_translate_element_container" style="visibility:hidden">
            	<div id="google_translate_element"></div>
				<script type="text/javascript" src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
			</div>
		</div>
		<#include "_poweredBy.ftl"/>
	</div>
</#escape>