<ul class="options "">
    <li class=" <#if action.isPageName('tenants')>selected</#if>">
        <#if action.isPageName('tenants')>
            Tenants
        <#else>
            <a href="/fieldid/admin/organizations.action">Tenants</a>
        </#if>
    </li>
    <li class=" <#if action.isPageName('eulas')>selected</#if>">
        <#if action.isPageName('eulas')>
            EULA
        <#else>
            <a href="/fieldid/admin/eulas.action">EULA</a>
        </#if>
    </li>
    <li class=" <#if action.isPageName('releaseNotes')>selected</#if>">
        <#if action.isPageName('releaseNotes')>
            Release Notes
        <#else>
            <a href="/fieldid/admin/releaseNotes.action">Release Notes</a>
        </#if>
    </li>
    <li class=" <#if action.isPageName('instructionalVideos')>selected</#if>">
        <#if action.isPageName('instructionalVideos')>
            Instructional Videos
        <#else>
            <a href="/fieldid/admin/instructionalVideos.action">Instructional Videos</a>
        </#if>
    </li>
    <li class=" <#if action.isPageName('unitOfMeasure')>selected</#if>">
        <#if action.isPageName('unitOfMeasure')>
            Units of Measure
        <#else>
            <a href="/fieldid/admin/unitOfMeasureList.action">Units of Measure</a>
        </#if>
    </li>
    <li class=" <#if action.isPageName('orderMapping')>selected</#if>">
        <#if action.isPageName('orderMapping')>
            Order Mappings
        <#else>
            <a href="/fieldid/admin/orderMappingList.action">Order Mappings</a>
        </#if>
    </li>
    <li class=" <#if action.isPageName('importObservations')>selected</#if>">
        <#if action.isPageName('importObservations')>
            Observation Importer
        <#else>
            <a href="/fieldid/admin/importObservations.action">Observation Importer</a>
        </#if>
    </li>
    <li class=" <#if action.isPageName('mail')>selected</#if>">
        <#if action.isPageName('mail')>
            Mail
        <#else>
            <a href="/fieldid/admin/mailTest.action">Mail</a>
        </#if>
    </li>
    <li class=" <#if action.isPageName('config')>selected</#if>">
        <#if action.isPageName('config')>
            Config
        <#else>
            <a href="/fieldid/admin/configCrud.action">Config</a>
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
    <li class=" <#if action.isPageName('systemPass')>selected</#if>">
        <#if action.isPageName('systemPass')>
            SystemPass
        <#else>
            <a href="/fieldid/admin/changeAdminPass.action">System Pass</a>
        </#if>
    </li>
</ul>