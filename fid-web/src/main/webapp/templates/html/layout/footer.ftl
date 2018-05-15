<#escape x as x?html >
	<div id="pageFooter">
		<div style="float:left">
			<p>
				<#include "../common/_versionNumber.ftl" />
			</p>
			<@n4.includeScript src="lang/googleTranslate.js"/>
            <div id="google_translate_element_container" style="visibility:hidden">
				<#if action.isGoogleTranslateAllowed()>
            		<div id="google_translate_element"></div>
					<script type="text/javascript">
                        if (isGoogleTranslateAllowedForCurrentLanguage())
                        	loadGoogleTranslate();
                        else
                        	hideGoogleTranslateWidget();
					</script>
				</#if>
			</div>
		</div>
		<#include "_poweredBy.ftl"/>
	</div>
</#escape>