${action.setPageType('event_type', 'event_form')!}

<script type="text/javascript">
    var eventTypeFormUrl = '<@s.url namespace="/" action="eventTypeForm"/>';

    function refreshPageToNewEventTypeId(id) {
        window.location = eventTypeFormUrl + "?uniqueID=" + id;
    }

    function onSuccessfulSessionRefresh() {
        window.location.reload();
    }
</script>

<#assign eventFormEditUrl>/fieldid/w/eventFormEdit/id/#{eventType.id}</#assign>

<iframe name="eventFormEditor" id="eventFormEditor" width="980" height="2000" src="${eventFormEditUrl}" style="border: none;" frameBorder="0">
    
</iframe>