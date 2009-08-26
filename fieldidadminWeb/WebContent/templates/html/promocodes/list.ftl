<table>
Total Promo Codes: ${promoCodes?size}
<#list promoCodes as promoCode>
<tr>
	<td>${promoCode.code?html}</td>
	<td><a href="<@s.url action="promoCodeEdit"/>?id=${promoCode.id}">Edit</a></td>
	<td><a href="<@s.url action="promoCodeDelete"/>?id=${promoCode.id}">Delete</a></td>
</tr>
</#list>
</table>
<a href="<@s.url action="promoCodeEdit" />">Add</a>
