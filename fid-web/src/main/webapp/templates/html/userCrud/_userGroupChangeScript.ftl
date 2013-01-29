<script type="text/javascript">
   var updateGroupsUrl = '<@s.url action="filterUserTypes" namespace="/ajax" />';

    function updateUserTypes(belongsToSelect) {
        var params = new Object();
        if (belongsToSelect.selectedIndex != -1) {
            params.userBelongsToFilter = groupSelect.options[groupSelect.selectedIndex].value;
        }
        getResponse(updateGroupsUrl, "get", params);
    }
</script>