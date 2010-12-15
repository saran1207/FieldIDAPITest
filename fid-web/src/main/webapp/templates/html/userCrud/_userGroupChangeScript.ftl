<script type="text/javascript">
   var updateGroupsUrl = '<@s.url action="filterUserTypes" namespace="/ajax" />';

    function updateUserTypes(groupSelect) {
        var params = new Object();
        if (groupSelect.selectedIndex != -1) {
            params.userGroup = groupSelect.options[groupSelect.selectedIndex].value;
        }
        getResponse(updateGroupsUrl, "get", params);
    }
</script>