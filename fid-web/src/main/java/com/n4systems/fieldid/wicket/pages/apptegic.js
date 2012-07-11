var _aaq = _aaq || [];
var apptegicAccount = 'fieldid';
var apptegicBaseURL = document.location.protocol + "//" + apptegicAccount + ".apptegic.com/";

_aaq.push(['setAccount', apptegicAccount]);
_aaq.push(['setDataset', '${apptegicDataset}']);
_aaq.push(['setUser', '${user}']);
_aaq.push(['setCompany', '${company}']);
_aaq.push(['setAccountType', '${accountType}']);
_aaq.push(['setCustomVariable', 'userType', '${userType}', 'page']);
_aaq.push(['trackAction']);

(function(){
    var d=document,
            g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
    g.type='text/javascript'; g.defer=true; g.async=true;
    g.src=apptegicBaseURL+'scripts/apptegic.min.js';
    s.parentNode.insertBefore(g,s);
})();
