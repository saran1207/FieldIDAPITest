${action.setPageType('event_type', 'event_form')!}

<script type="text/javascript">
    var eventTypeViewUrl = '<@s.url namespace="/" action="eventType" uniqueID="${eventType.id}"/>';
    var promptBeforeLeaving = true;

    function onSuccessfulSessionRefresh() {
        promptBeforeLeaving = false;
        window.location.reload();
    }

    function navigateBackToEvenTypeView() {
        promptBeforeLeaving = false;
        window.location = eventTypeViewUrl;
    }

    window.onbeforeunload = function() {
        if (promptBeforeLeaving)
            return "It looks like you're about to leave this page. Please ensure that any changes are saved.";
    }
</script>

<#assign eventFormEditUrl>/fieldid/w/eventFormEdit/id/#{eventType.id}</#assign>

<iframe name="eventFormEditor" id="eventFormEditor" width="100%" height="730" src="${eventFormEditUrl}" style="border: none;" frameBorder="0">
    
</iframe>