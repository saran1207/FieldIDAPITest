<#if apptegicEnabled?exists && apptegicEnabled>
<script type="text/javascript">
    var _aaq = _aaq || [];
    var apptegicAccount = 'fieldid';
    var apptegicBaseURL = document.location.protocol + "//" + apptegicAccount + ".apptegic.com/";

    _aaq.push(['setAccount', apptegicAccount]);
    _aaq.push(['setDataset', '${apptegicDataset}']);
    _aaq.push(['setUser', '${(sessionUser.name)!"Not Logged In"}']);
    _aaq.push(['setCompany', '${(tenant.name)!"No Tenant"}']);
    _aaq.push(['setAccountType', '${(sessionUser.accountType)!"Paid"}']);
    _aaq.push(['setCustomVariable', 'userType', '${(sessionUser.userTypeLabel)!"Not Logged In"}', 'page']);
    _aaq.push(['trackAction']);

    (function(){
        var d=document,
                g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
        g.type='text/javascript'; g.defer=true; g.async=true;
        g.src=apptegicBaseURL+'scripts/apptegic.min.js';
        s.parentNode.insertBefore(g,s);
    })();
</script>
</#if>