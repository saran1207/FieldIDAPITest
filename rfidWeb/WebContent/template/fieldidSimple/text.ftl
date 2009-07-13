<#include "/${parameters.templateDir}/fieldidSimple/controlheader.ftl" />

<#assign extraVar_= parameters.put( "cssClass", parameters.cssClass!"" + "textbox" )!""/>
<#include "/${parameters.templateDir}/simple/text.ftl" />
