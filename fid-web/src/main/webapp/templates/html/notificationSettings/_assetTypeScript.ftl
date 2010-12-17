<script type="text/javascript">
    updateAssetGroupsUrl = '<@s.url action="retrieveNotificationAssetTypes" namespace="/ajax" />';

    function updateAssetTypes(assetTypeGroupSelect) {
        var params = new Object();
        if (assetTypeGroupSelect.selectedIndex != -1) {
            params.assetTypeGroupId = assetTypeGroupSelect.options[assetTypeGroupSelect.selectedIndex].value;
        }
        getResponse(updateAssetGroupsUrl, "get", params);
    }
</script>