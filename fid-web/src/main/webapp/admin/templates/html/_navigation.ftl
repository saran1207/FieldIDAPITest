<ul class="options "">

    <li class=" <#if action.isPageName('tenants')>selected</#if>">
        <#if action.isPageName('tenants')>
            Tenants
        <#else>
            <a href="/fieldid/admin/organizations.action">Tenants</a>
        </#if>
    </li>

	<#if superUser>
	<li>
	    <a href="/fieldid/w/admin/users">Users</a>
	</li>

    <li class=" <#if action.isPageName('eulas')>selected</#if>">
        <#if action.isPageName('eulas')>
            EULA
        <#else>
            <a href="/fieldid/admin/eulas.action">EULA</a>
        </#if>
    </li>

    <li class=" <#if action.isPageName('orderMapping')>selected</#if>">
        <#if action.isPageName('orderMapping')>
            Order Mappings
        <#else>
            <a href="/fieldid/admin/orderMappingList.action">Order Mappings</a>
        </#if>
    </li>
    <li class=" <#if action.isPageName('mail')>selected</#if>">
        <#if action.isPageName('mail')>
            Mail
        <#else>
            <a href="/fieldid/admin/mailTest.action">Mail</a>
        </#if>
    </li>
    <li class=" <#if action.isPageName('certs')>selected</#if>">
        <#if action.isPageName('certs')>
            Certs
        <#else>
            <a href="/fieldid/admin/certSelection.action">Certs</a>
        </#if>
    </li>
    <li class=" <#if action.isPageName('tasks')>selected</#if>">
        <#if action.isPageName('tasks')>
            Tasks
        <#else>
            <a href="/fieldid/admin/taskCrud.action">Tasks</a>
        </#if>
    </li>
    <li>
        <a href="/fieldid/w/admin/config">Config</a>
    </li>
    <li>
        <a href="/fieldid/w/admin/connections">Connections</a>
    </li>
    <li>
        <a href="/fieldid/w/admin/configureLanguages">Languages</a>
    </li>
	</#if>

</ul>