<div id="pageFooter">
    <#if !devMode>
        <div id="sslCert">
            <a target="_blank" href="http://www.thawte.com/digital-certificates/"><@s.text name="label.aboutssl"/></a>
            <br/>
            <script type="text/javascript" src="https://siteseal.thawte.com/cgi/server/thawte_seal_generator.exe"></script>
        </div>
    </#if>
        <div class="logo">
            <@s.text name="label.copyright"/> <a href="http://www.fieldid.com/" target="_blank"><@s.text name="label.n4systems"/></a>
            <img src="<@s.url value="/images/poweredByFieldID.jpg"/>"/>
        </div>
</div>