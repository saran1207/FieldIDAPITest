<@n4.includeScript src="selection"/>

<script type="text/javascript">
    addItemsToSelectionUrl = '<@s.url action="addItemsToSelection" namespace="/ajax"/>';
    deleteItemsFromSelectionUrl = '<@s.url action="deleteItemsFromSelection" namespace="/ajax"/>';
    addEntireSearchToSelectionUrl = '<@s.url action="addEntireResultToSelection" namespace="/ajax"/>';
    clearSelectionUrl = '<@s.url action="clearSelection" namespace="/ajax"/>';

    document.observe("dom:loaded", function() {
        checkHeaderBoxIfAllItemsAreSelected();
    });
</script>
