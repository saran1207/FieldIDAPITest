<head>
    <@n4.includeStyle href="newCss/component/matt_buttons" />
    <@n4.includeStyle href="newCss/asset/actions-menu.css" />
    <script type="text/javascript">
        var subMenu = (function() {

            var init = function(showLeft) {
                addMenuHandlers();
            };

            function addMenuHandlers() {
                jQuery(document).delegate('.actions .menu > a', 'click', function() {
                    jQuery(this).next('.menu-items').first().show();
                    return false;
                });
                jQuery(document).delegate('"#fieldidBody', 'click', function(e) {
                    jQuery('.actions .menu-items').hide();
                });
            };

            return {
                init : init
            };

        })();

        jQuery(document).ready(function(){
            subMenu.init();
        });
    </script>
</head>
<div class="event-header">
    <h1 class="event-summary"> <span class="greyText">${event.type.displayName!}  /</span> ${event.asset.identifier!}</h1>
    <p class="owner-summary">
        <img src="/fieldid/images/location-icon.png">
        <#if !event.advancedLocation.isBlank()>
            ${helper.getFullNameOfLocation(event.advancedLocation)!?html},
        </#if>

        <#if event.owner.customerOrg?exists>
            ${(event.owner.customerOrg.name)!?html} (${(event.owner.internalOrg.name)!?html})
        <#else>
            ${(event.owner.internalOrg.name)!?html}
        </#if>
        <#if event.owner.divisionOrg?exists>
            , ${(event.owner.divisionOrg.name)!?html}
        </#if>
    </p>

    <div class="actions">
        <a class="mattButton summary" href="<@s.url namespace="/" value="w/assetSummary?uniqueID=${asset.id}"/>">
            <@s.text name="label.assetsummary"/>
        </a>
        <a class="mattButton edit" href="<@s.url action="selectEventEdit" uniqueID="${event.id}"/>">
            <@s.text name="label.edit"/>
        </a>

        <#if event.anyCertPrintable>
            <@s.url id="eventCertUrl" action="downloadEventCert" namespace="/file" reportType="INSPECTION_CERT" uniqueID="${uniqueID}" />
            <@s.url id="observationCertUrl" action="downloadEventCert" namespace="/file" reportType="OBSERVATION_CERT" uniqueID="${uniqueID}" />

            <ul class="print-certs">
                <li class="menu">
                    <a class="mattButton" href="javascript:void(0);" ><@s.text name="label.print"/> <img src="/fieldid/images/menu-down.png"></a>
                    <ul class="menu-items">
                        <#if event.eventCertPrintable>
                            <li><a href="${eventCertUrl}" target="_blank" >${event.type.group.reportTitle?html} (<@s.text name="label.pdfreport"/>)</a></li>
                        </#if>
                        <#if event.observationCertPrintable>
                            <li><a href="${observationCertUrl}" target="_blank" ><@s.text name="label.printobservationcertificate"/></a></li>
                        </#if>
                    <ul class="menu-items">
                </li>
            </ul>
        </#if>

    </div>

</div>