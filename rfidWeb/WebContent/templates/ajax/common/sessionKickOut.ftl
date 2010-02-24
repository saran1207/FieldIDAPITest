<#assign sessionKickOut><#include "../../html/common/sessionKickOut.ftl"/></#assign>
$('systemNotices').update('${sessionKickOut?js_string}');
