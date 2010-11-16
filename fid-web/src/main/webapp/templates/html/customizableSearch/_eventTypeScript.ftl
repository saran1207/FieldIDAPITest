<script type="text/javascript">
    updateEventGroupsUrl = '<@s.url action="retrieveEventTypes" namespace="/ajax" />';

    function updateEventTypes(eventTypeGroupSelect) {
        var params = new Object();
        if (eventTypeGroupSelect.selectedIndex != -1) {
            params.eventTypeGroupId = eventTypeGroupSelect.options[eventTypeGroupSelect.selectedIndex].value;
        }
        getResponse(updateEventGroupsUrl, "get", params);
    }
</script>