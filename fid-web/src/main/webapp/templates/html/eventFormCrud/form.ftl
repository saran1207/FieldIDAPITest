${action.setPageType('event_type', 'event_form')!}

<script type="text/javascript">
    var eventTypeFormUrl = '<@s.url namespace="/" action="eventTypeForm"/>';

    function refreshPageToNewEventTypeId(id) {
        window.location = eventTypeFormUrl + "?uniqueID=" + id;
    }

    function onSuccessfulSessionRefresh() {
        window.location.reload();
    }

    window.onbeforeunload = function() {
        return "It looks like you're about to leave this page. Please ensure that any changes are saved.";
    }
</script>

<#assign eventFormEditUrl>/fieldid/w/eventFormEdit/id/#{eventType.id}</#assign>

<iframe name="eventFormEditor" id="eventFormEditor" width="100%" height="730" src="${eventFormEditUrl}" style="border: none;" frameBorder="0">
    
</iframe>