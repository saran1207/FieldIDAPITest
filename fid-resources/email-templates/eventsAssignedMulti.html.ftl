<style type="text/css">

    .notificationMessage {
        font-family: "lucida grande",tahoma,verdana,arial,sans-serif
    }

    table {
        width:90%;
    }

    th {
        text-align:left;
        color:#999999;
        padding:6px;
    }

    td {
        padding:6px;
    }

    .subject {
        font-size:24px;
        width:90%;
        background-color:#e9e9e9;
        padding:30px;
        color:#666666;
    }

    a {
        text-decoration:none;
        color:#0099ff;
    }

    a.visited {
        color:#0066aa;
    }

    .notificationText {
        padding:20px 10px;
    }

</style>

<div class="notificationMessage">

<div class="subject">
    ${subject}
</div>


<div class="notificationText">

    Hi ${events.get(0).assignee.firstName},

    <br>&nbsp;<br>

    You have been assigned ${events.size()} new tasks to complete in Field ID.

</div>

<table>
    <tr>
        <th>Asset</th>
        <th>Event Type</th>
        <th>Due Date</th>
    </tr>
<#list events as event>
    <tr>
        <td>
            ${event.asset.type.name} / ${event.asset.identifier} <#if event.asset.assetStatus?exists>/ ${event.asset.assetStatus.displayName}</#if>
            <br/>
            ${event.owner.displayName}
        </td>
        <td>
            ${event.type.name}
        </td>
        <td>
            <a href="${systemUrl}/fieldid/w/performEvent?type=${event.type.id.toString()}&assetId=${event.asset.id.toString()}&scheduleId=${event.id.toString()}">
                ${dueDateStringMap.get(event.id)}
            </a>
        </td>
    </tr>
</#list>
</table>

<hr>

</div>