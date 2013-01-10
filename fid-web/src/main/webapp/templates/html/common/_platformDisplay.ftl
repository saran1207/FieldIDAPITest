<img id="${iconIdentifier}" src="/fieldid/images/platforms/${platformType.name()}.png" title="${platform?html}">
<script type="text/javascript">
    jQuery(document).ready(function() {
        jQuery('#${iconIdentifier}').tooltip();
    });
</script>