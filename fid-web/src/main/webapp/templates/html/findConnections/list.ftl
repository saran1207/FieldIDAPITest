<#assign currentAction="findConnections.action" />
<#include '../safetyNetwork/_safetyNetworkLayout.ftl'>

<div id="safetyNetworkSplash">
	<h1>Search Results</h1>

    <#include '../common/_pagination.ftl' />

    <#if page.hasResults() && page.validPage() >

        <table class="list" style="width: 700px;">

            <#list page.list as org>
                <tr>
                    <td>
                        <#assign tenant = org.tenant>
                        <#include "../common/_displayTenantLogo.ftl"/>
                    </td>
                    <td>
                        <span>${(org.name?html)!}</span>
                        <#if org.webSite?exists>
                            <br/>
                            <a href="${action.createHref(org.webSite)}">${org.webSite}</a>
                        </#if>
                    </td>
                    <td>
                        <#if action.isConnectedCustomer(org)>
                            <@s.text name="label.added_as_customer"/><br/>
                        <#else>
                            <a href='<@s.url action="connectionInvitationAdd"/>'><@s.text name="label.add_as_customer"/></a>
                        </#if>
                        <#if action.isConnectedVendor(org)>
                            <@s.text name="label.added_as_vendor"/>
                        <#else>
                            <a href='<@s.url action="connectionInvitationAdd"/>'><@s.text name="label.add_as_vendor"/></a>
                        </#if>
                    </td>
                </tr>
            </#list>

        </table>

        <#include '../common/_pagination.ftl' />
    <#elseif !page.hasResults() >
        <div class="emptyList" >
            <h2><@s.text name="label.noresults"/></h2>
            <p>
                <@s.text name="message.emptysearch" />
            </p>
        </div>
    <#else>
        <div class="emptyList" >
            <h2><@s.text name="label.invalidpage"/></h2>
            <p>
                <@s.text name="message.invalidpage" />
                <a href="<@s.url action="findConnections" currentPage="1" />" ><@s.text name="message.backtopageone"/></a>
            </p>
        </div>
    </#if>

    <br/>

    <div id="sendAnInvite">
        <h2><@s.text name="label.cant_find"/> <a href="<@s.url action="invite"/>"><@s.text name="label.send_an_invite_link"/></a></h2>
        <p><@s.text name="label.invite_description"/></p>
    </div>

</div>



