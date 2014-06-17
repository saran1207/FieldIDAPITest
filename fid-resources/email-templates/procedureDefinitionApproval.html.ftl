<style type="text/css">

    .notificationMessage {
        font-family: "lucida grande",tahoma,verdana,arial,sans-serif;
        width: 100%;
        float:left;
    }

    hr {
        float:left;
    }

    a {
        text-decoration:none;
        color:#0099ff;
    }

    a.visited {
        color:#0066aa;
    }

    .notificationMessage p {
        padding-bottom:6px;
    }

</style>

<div class="notificationMessage">

    <p>
        ${procedureDefinition.developedBy.fullName} has authored a Lockout/Tagout Procedure for ${procedureDefinition.asset.type.name} ${procedureDefinition.asset.identifier}
            <#if procedureDefinition.asset.advancedLocation?exists>
                at ${procedureDefinition.asset.advancedLocation.fullName}
            </#if>
    </p>

    <p>
        <#if approvalGroup?exists>
            This procedure is currently waiting approval by the group, ${approvalGroup.name}.
        <#else>
            This procedure is currently waiting approval by you.
        </#if>
    </p>

    You can certify or reject the new procedure here: <a href="${systemUrl}/fieldid/w/procedureDefinitions?uniqueID=${procedureDefinition.asset.id?c}">Procedures</a>

</div>