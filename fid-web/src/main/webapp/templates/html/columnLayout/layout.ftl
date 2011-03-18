<title>
    <@s.text name="title.column_layout" />
</title>

<script>
    var templatesUrl = '<@s.url action="templates"/>';

    function goBackToTemplates() {
        window.location = templatesUrl;
    }

    function onSuccessfulSessionRefresh() {
        window.location.reload();
    }

</script>

<#assign columnLayoutUrl>/fieldid/w/columnsLayout?type=${action.layoutType}</#assign>

<iframe name="columnsLayoutEditor" id="columnsLayoutEditor" width="100%" height="1500" src="${columnLayoutUrl}" style="border: none;" frameBorder="0">

</iframe>