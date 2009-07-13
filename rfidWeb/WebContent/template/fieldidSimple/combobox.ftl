<#include "/${parameters.templateDir}/fieldidSimple/controlheader.ftl" />
<#include "/${parameters.templateDir}/simple/select.ftl" />
<#if !parameters.comboBoxOff?exists || parameters.comboBoxOff == false >
	<script type="text/javascript">
		new toCombo('${parameters.id}');
	</script>
</#if>
