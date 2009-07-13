
<graph YAxisName='<@s.text name="label.riskofnoncompliance"/>' YAxisMaxValue="100" shownames='1' 
	showvalues='0'  showLegend='0' formatNumberScale='1' decimalPrecision='2' limitsDecimalPrecision='0' divLineDecimalPrecision='0' 
	formatNumber='1' chartTopMargin='15' rotateNames='1' >
	<categories>
		<#list risk.getDateSet( 10 ) as date>
			<#if date.equals( risk.getStartDate() ) >
				<#assign showName=1/>
   				<#assign extraLabel><@s.text name="label.lastinspection"/></#assign>
			<#elseif date.equals( risk.getToday() )  >
				<#assign showName=1/>
   				<#assign extraLabel><@s.text name="label.today"/></#assign>
			<#elseif date.equals( risk.getComplianceDate() ) >
   				<#assign showName=1/>
   				<#assign extraLabel><@s.text name="label.nextinspectiondate"/></#assign>
   			<#else >
   				<#assign showName=0/>
   				<#assign extraLabel=""/>
			</#if>
			<category name='${action.formatDate(date,false)}' showname='${showName}' hoverText="${action.formatDate(date,false)} ${extraLabel}"/>
		</#list>
	</categories>

	
   	
   
   <dataset seriesname="" color="333333" showValues="0" areaAlpha="50" showAreaBorder="1" areaBorderThickness="2" areaBorderColor="333333">
   		<#list risk.getDateSet( 10 ) as date>
   			<#if date.equals(risk.getStartDate()) >
   				<set value='${ risk.sample( date )?string( "0.####") }'/>
   			<#else>
   				<set />
   			</#if>
   		</#list>
   	</dataset>
   
	<dataset seriesname="" color="333333" showValues="0" areaAlpha="50" showAreaBorder="1" areaBorderThickness="2" areaBorderColor="333333">
   		<#list risk.getDateSet( 10 ) as date>
   			<#if date.before( risk.getToday() ) >
   				<set value='${ risk.sample( date )?string( "0.####") }'/>
   			<#else>
   				<set />
   			</#if>
   		</#list>
   	</dataset>
   	
   	<dataset seriesname="" color="99FF99" showValues="0" areaAlpha="50" showAreaBorder="1" areaBorderThickness="2" areaBorderColor="99FF99">
   		<#list risk.getDateSet( 10 ) as date>
   			<#if date.equals( risk.getToday() ) ||  date.equals( risk.getDayBefore( risk.today ) ) || date.equals( risk.getDayAfter( risk.today ) )>
   				<set value='${ risk.sample( date )?string( "0.####") }'/>
   			<#else>
   			
   				<set />
   			</#if>
   		</#list>
   	</dataset>
   	
   	
   	<dataset seriesname="" color="00DD00" showValues="0" areaAlpha="50" showAreaBorder="1" areaBorderThickness="2" areaBorderColor="00DD00">
   		<#list risk.getDateSet( 10 ) as date>
   			<#if  !date.equals( risk.getComplianceDate() ) && !date.before( risk.getToday() ) &&	!date.equals( risk.getToday() )  && !date.equals( risk.getStartDate()) && date.before( risk.getComplianceDate() ) >
   				<set value='${ risk.sample( date )?string( "0.####") }' />
   			<#else>
   				<set />
   			</#if>
   		</#list>
   	</dataset>
   	
   	<dataset seriesname="" color="DDDD00" showValues="0" areaAlpha="50" showAreaBorder="1" areaBorderThickness="2" areaBorderColor="DDDD00" >
   		<#list risk.getDateSet( 10 ) as date>
   			<#if  date.equals( risk.complianceDate ) ||  date.equals( risk.getDayBefore( risk.complianceDate ) ) || date.equals( risk.getDayAfter( risk.complianceDate ) )>
   				<set value='${ risk.sample( date )?string( "0.####") }' />
   			<#else>
   				<set />
   			</#if>
   		</#list>
   	</dataset>
   			
   	<dataset seriesname="" color="DD0000" showValues="0" areaAlpha="50" showAreaBorder="1" areaBorderThickness="2" areaBorderColor="DD0000">
   		<#list risk.getDateSet( 10 ) as date>
   			<#if  !date.equals( risk.getComplianceDate() ) && !date.before( risk.getToday() ) &&	!date.equals( risk.getToday() )  && !date.equals( risk.getStartDate() ) && date.after( risk.getComplianceDate() ) >
   				<set value='${ risk.sample( date )?string( "0.####") }' />
   			<#else>
   				<set />
   			</#if>
   		</#list>
   	</dataset>			
   			
   	
</graph>