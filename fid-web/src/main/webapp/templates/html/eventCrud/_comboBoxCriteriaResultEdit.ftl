<head>
	<script language="javascript" src="<@s.url value="/javascript/combobox.js"/>"> </script>
</head>

<div class="criteriaEditContainer">
		
    <select name="criteriaResults[${criteriaCount}].textValue" class="comboBoxCriteria" headerKey="-1">
        <option></option>
        <#list criteria.options as option>
            <#if criteriaResult?exists (criteriaResult.textValue)?exists && criteriaResult.textValue == option>
                <#assign selectedStr = 'selected'/>
            <#else>
                <#assign selectedStr = ''/>
            </#if>

            <option ${selectedStr}>${option}</option>
        </#list>
        <#if (criteriaResult.textValue)?exists && !criteria.options.contains(criteriaResult.textValue)>
        	<option selected>${criteriaResult.textValue}</option>
        </#if>
    </select>
    <script type="text/javascript">
		new toCombo('criteriaResults[${criteriaCount}].textValue');
	</script>
</div>
