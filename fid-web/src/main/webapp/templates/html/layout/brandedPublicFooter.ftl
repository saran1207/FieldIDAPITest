<div id="pageFooter">
    <#if !devMode>
        <div id="sslCert">
            <img src="<@s.url value="/images/password-icon.png"/>"/>
            <@s.text name="label.aboutssl-prefix"/>&nbsp<a target="_blank" href="http://www.thawte.com/digital-certificates/"><@s.text name="label.aboutssl"/></a>
        </div>
    </#if>
        <div class="logo">
            <div>
                <img src="<@s.url value="/images/logo-icon.png"/>"/>
            </div>
            <div style="margin-top:4px;margin-left:4px;">
                 <a href="http://www.fieldid.com/" target="_blank"><@s.text name="label.copyright"/>&nbsp;<@s.text name="label.masterlock"/></a>
            </div>
        </div>
</div>