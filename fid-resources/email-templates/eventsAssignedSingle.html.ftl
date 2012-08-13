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

    .assetIdentifier {
        padding:20px 10px;
        color:#999999;
        font-size:18px;
    }

    .notificationDataTitle {
        color:#999999;
        width:200px;
        clear:left;
        float:left;
        display:block;
        padding:10px;
    }

    .notificationData {
        color:#666666;
        float:left;
        display:block;
        padding:10px;
    }


</style>

<div class="notificationMessage">

    <div class="subject">
        ${subject}
    </div>


    <div class="assetIdentifier">
        ${event.asset.type.name} / ${event.asset.identifier} <#if event.asset.assetStatus?exists>/ ${event.asset.assetStatus.displayName}</#if>
        <br/>
        ${event.owner.displayName}
    </div>

    <div class="notificationDataTitle">
        Due Date
    </div>
    <div class="notificationData">
        ${dueDateStringMap.get(event.id)}
    </div>

    <div class="notificationDataTitle">
        Assignee
    </div>
    <div class="notificationData">
        ${event.assignee.fullName}
    </div>
    <div style="clear:both;"></div>

</div>