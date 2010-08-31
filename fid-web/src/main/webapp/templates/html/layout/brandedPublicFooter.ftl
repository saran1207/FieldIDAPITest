<div id="pageFooter">
	<div id="copyright"><@s.text name="label.copyright"/> <a href="http://www.n4systems.com/" target="_blank"><@s.text name="label.n4systems"/></a></div>
	
	<div class="poweredBy"><img src="<@s.url value="/images/poweredByFieldID.jpg"/>"/></div>

	<div id="sslCert">
		<#if !devMode>
			<a target="_blank" href="http://www.thawte.com/digital-certificates/"><@s.text name="label.aboutssl"/></a>
			<br/>
			<script type="text/javascript" src="https://siteseal.thawte.com/cgi/server/thawte_seal_generator.exe"></script>
		</#if>	
	</div>
	
</div>