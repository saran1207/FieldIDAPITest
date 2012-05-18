<div id="pageFooter">
    <#if !devMode>
        <div id="sslCert">
            <img src="<@s.url value="/images/password-icon.png"/>"/>
            <@s.text name="label.aboutssl-prefix"/>&nbsp<a target="_blank" href="http://www.thawte.com/digital-certificates/"><@s.text name="label.aboutssl"/></a>
            <br/>
            <script type="text/javascript" src="https://siteseal.thawte.com/cgi/server/thawte_seal_generator.exe"></script>
        </div>
    </#if>
        <div class="logo">
            <div>
                <img src="<@s.url value="/images/logo-icon.png"/>"/>
            </div>
            <div style="margin-top:4px;margin-left:4px;">
                 <a href="http://www.fieldid.com/" target="_blank"><@s.text name="label.copyright"/><@s.text name="label.n4systems"/></a>
            </div>
        </div>
</div>