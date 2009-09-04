
<@s.form action="promoCodeSave" method="post">
	<@s.hidden name="id" value="%{id}" />
	<@s.textfield name="promoCode.code" value="%{promoCode.code}" label="Code" />
	<@s.textfield name="diskSpaceMB" label="Max Disk Space (MB) (-1 for Unlimited)" />
	<@s.textfield name="promoCode.limitAdjuster.assets" label="Max Assets (-1 for Unlimited)" />	
	<@s.iterator  value="availableExtendedFeatures" >
		<@s.checkbox name="extendedFeatures['%{name}']" >
			<@s.param name="label"><@s.property value="name"/></@s.param>
		</@s.checkbox> 
	</@s.iterator>
	<@s.submit />
</@s.form>
	