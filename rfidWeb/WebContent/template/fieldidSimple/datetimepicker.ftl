<#include "/${parameters.templateDir}/fieldidSimple/controlheader.ftl" />
<#if !parameters.id?exists >${parameters.put( "id", parameters.name ) }</#if>
  
<#if parameters.type == "date">
	<#assign format=sessionUser.otherDateFormat/>
	<#assign visualFormat=sessionUser.displayDateFormat/>
	<#assign showTime="false"/>
	<#assign dateClass="date">
<#else>
	<#assign format=sessionUser.otherDateTimeFormat/>
	<#assign visualFormat=sessionUser.displayDateTimeFormat/>
	<#assign showTime="true"/>
	<#assign dateClass="dateTime">
</#if>
<#if !parameters.cssClass?exists > 
	${(parameters.put('cssClass',"")?contains(" ")?string("","") )!}
</#if>
	
<#if !parameters.title?exists >  	
	<#assign extraVar_= ( parameters.put('title',visualFormat) )!""/>
</#if>
<#assign extraVar_= ( parameters.put('cssClass', parameters.cssClass + " " + dateClass) )!""/>
<#include "/${parameters.templateDir}/simple/text.ftl"/>
<#if parameters.type != "date">
	<span class="timeZone">${sessionUser.timeZoneName}</span>
</#if>

<img class="datePicker" src="<@s.url value="/images/icons/FieldID_CALENDAR-CHECK-normal.png"/>"  id="dateTrigger_${parameters.id}"/>
<script type="text/javascript">
  Calendar.setup(
    {
      inputField  : "${parameters.id}",  // ID of the input field
      ifFormat    : "${format}",    // the date format
      daFormat    : "${format}",
      button      : "dateTrigger_${parameters.id}", // ID of the button
      showsTime   : ${showTime},
      timeFormat  : "12"
    }
  );
</script>

