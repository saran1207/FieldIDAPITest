<div id="pageFooter">
	<div id="sslCert">
		<#if !devMode>
			<p>
				<script type="text/javascript" src="https://siteseal.thawte.com/cgi/server/thawte_seal_generator.exe"></script>
			</p>
		</#if>
		
		<p>
			<a target="_blank" href="http://www.thawte.com/digital-certificates/"><@s.text name="label.aboutssl"/></a>
		</p>
	</div>
	<#include "_poweredBy.ftl"/>
</div>