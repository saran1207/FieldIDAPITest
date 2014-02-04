<style type="text/css">

    .notificationMessage {
        font-family: "lucida grande",tahoma,verdana,arial,sans-serif;
        width: 100%;
        float:left;
    }

    hr {
        float:left;
    }

    .subject {
        background-color:#e9e9e9;
        padding:30px;
        margin-bottom: 10px;
    }

    .subject .title{
        font-size:24px;
        width:100%;
        color:#666666;
    }

    .subject .message{
        font-size:12px;
        width:100%;
        color:#999999;
    }

    a {
        text-decoration:none;
        color:#0099ff;
    }

    a.visited {
        color:#0066aa;
    }

    .event{
        border-bottom: 1px dotted #666666;
        margin-bottom: 10px;
        width: 100%;
        float:left;
    }

    .info {
        padding: 30px;
        width: 60%;
        float:left;
    }

    .image {
        padding-top: 30px;
        float:left;
    }

</style>

<div class="notificationMessage">

    <div class="subject">
        <#if assignee?exists>
            <div class="title">${assignee.fullName}, you've been assigned work</div>
        <#elseif assignedGroup?exists>
            <div class="title">A group you are a member of, ${assignedGroup.name}, has been assigned work</div>
        </#if>
        <div class="message">
            This is an automated email to let you know that you have been assigned work to do. <a href="${systemUrl}/fieldid/w/dashboard">Login</a> to view these on your Field ID Dashboard.
        </div>
    </div>

    <#list events as event>
        <div class="event">
            <div class="info">
                <#if event.asset?exists>
                    ${event.asset.type.name} / ${event.asset.identifier}
                    <br/>
                    ${event.owner.displayName}
                <#elseif event.place?exists>
                    ${event.place.displayName}
                </#if>
                <br/>
                <br/>
                ${event.type.name}
                <br/>
                <br/>
                <#if event.type.actionEventType && event.priority?exists>
                    ${event.priority.name}
                    <br/>
                    <p>
                    ${(event.notes?replace('\n', '<br/>'))!}
                    </p>
                    <br/>
                </#if>
                Due
                ${dueDateStringMap.get(event.id)}
                </a>
                <br/>
                <br/>
                <#if event.type.actionEventType>
                    Assigned by ${event.modifiedBy.fullName}
                </#if>
            </div>
            <div class="image">
                <#if criteriaImageMap.get(event.id)?exists>
                    <img src="${criteriaImageMap.get(event.id)}"/>
                </#if>
            </div>
        </div>
    </#list>

    <hr>

</div>