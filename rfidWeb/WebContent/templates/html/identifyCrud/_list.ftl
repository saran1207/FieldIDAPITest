<div id="orderResults">
<#if order?exists >
	<div class="orderDetails crudForm largeForm pageSection">
		<h2 class="orderTitle" ><@s.text name="label.orderdetails"/></h2>
		<div class="sectionContent">
			<div class="twoColumn" >
				<div class="infoSet">
					<label><@s.text name="label.ordernumber"/></label>
					<span>${order.orderNumber}</span>
				</div>
				<div class="infoSet">
					<label><@s.text name="label.orderdate"/></label>
					<span>${action.formatDate(order.orderDate, false)}</span>
				</div>
				<div class="infoSet">
					<label><@s.text name="label.description"/></label>
					<span>${order.description!}</span>
				</div>
				
			</div>
			<div class="twoColumn lastColumn" >
				<div class="infoSet">
					<label><@s.text name="label.purchaseorder"/></label>
					<span>${order.ponumber!}</span>
				</div>
				<div class="infoSet">
					<label><@s.text name="label.customer"/></label>
					<span>${(order.customer.displayName)!}</span>
				</div>
				<div class="infoSet">
					<label><@s.text name="label.division"/></label>
					<span>${(order.division.displayName)!}</span>
				</div>
			</div>
		</div>
	</div>
	<div class="pageSection">
		<#if order.orderType.name() == "SHOP" >
			<#include "_lineItemTable.ftl"/>
		<#else>
			<#include "_customerOrder.ftl"/>
		</#if>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults"/></h2>
		<p>
			<@s.text name="message.ordernotfound">
				<@s.param name="value">${tagOption.text}</@s.param>
				<@s.param name="value">"${orderNumber}"</@s.param>
			</@s.text>
		</p>
	</div>
</#if>

</div>