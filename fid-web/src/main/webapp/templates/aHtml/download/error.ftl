<div id="errorContainer" class="errorContainer">
    <div id="errorList" class="errorList">
    <@s.actionerror/>
    </div>
<@s.text name="message.cannotloaddata_support">
    <@s.param>${helpUrl}</@s.param>
</@s.text>
    </p>
</div>

<script type="text/javascript" >

    document.observe("dom:loaded", function() {
        $('error').hide();
    });


</script>