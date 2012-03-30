<a href='<@s.url action="event" namespace="/aHtml/iframe"  assetId="${asset.id}" uniqueID="${event.id}"/>${additionsToQueryString!}' class="eventLightbox">
	<#if urlLabel?exists>
		 <@s.text name="${urlLabel}"/>
	<#else>
		<@s.text name="label.view"/>
	</#if>
</a>

<script type="text/javascript">

    jQuery(document).ready(function(){
        jQuery('.eventLightbox').colorbox({ajax:true, maxHeight: '600px', width: '600px'});
    });

</script>