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
    ${rejector.fullName} has rejected a Lockout/Tagout Procedure for ${procedureDefinition.asset.type.name} ${procedureDefinition.asset.identifier}
    <#if procedureDefinition.asset.advancedLocation?exists>
        at ${procedureDefinition.asset.advancedLocation.fullName}
    </#if>
    </p>

    <p>Procedure Code: ${procedureDefinition.procedureCode}</p>
    <p>Revision Number: ${procedureDefinition.revisionNumber}</p>

    <p>
        ${rejectionMessage}
    </p>

    You can revise the procedure here: <a href="${systemUrl}/fieldid/w/procedureDef?id=${procedureDefinition.id?c}">Revise Procedure</a>

</div>