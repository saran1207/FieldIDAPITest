<img id="platform_${platformType.name()}" src="/fieldid/images/platforms/${platformType.name()}.png" title="${platform?html}">
<script type="text/javascript">
    jQuery(document).ready(function() {
        jQuery('#platform_${platformType.name()}').tooltip();
    });
</script>