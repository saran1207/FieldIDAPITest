<div class="formRowHolder">
	<div class="wwgrp" id="wwgrp_customer">
		<span class="wwlbl" id="wwlbl_customer">
			<label class="label" for="customer"><@s.text name="label.customer"/>:</label>
		</span> 
		<span class="wwctrl" id="customerName">
			${(product.owner.name?html)!}
		</span>
		<@s.hidden name="owner" id="customer" />
		 
	</div>		 

	<div class="wwgrp" id="wwgrp_customer">
		<span class="wwlbl" >
			<label class="label" for="division"><@s.text name="label.division"/>:</label>
		</span> 
		<span class="wwctrl" id="divisionName">
			${(product.division.name?html)!}
			
		</span>
		 <@s.hidden name="division" id="division" />
	</div>
</div>