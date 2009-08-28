<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>

<h1><@s.text name="title.sign_up_for_an_account"/></h1>
<div>
	<table id="packages">
		<thead>
			<tr>
				<th></th>
				<#list packages as package>
					<th id="package_${package.name}" <#if package.preferred>class="preferred"</#if>>
						${package.name?html}
					</th>
				</#list>
			</tr>
		</thead>
		<tbody>
			<tr id="priceRow">
				<td class="description"><@s.text name="label.price"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						$${package.priceInDollars?html}
					</td>
				</#list>
			</tr>
			
			<tr>
				<td class="description"><@s.text name="label.users"/></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<@s.text name="${package.numberOfUsersLabel}"/>
					</td>
				</#list>
			</tr>
			
			
			<tr>
				<td class="description"></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
						<a href="<@s.url action="signUpAdd" signUpPackageId="${package.name}"/>"><@s.text name="label.sign_up_now"/></a>
					</td>
				</#list>
			</tr>
			
		</tbody>
		<tfoot>
			<tr>
				<td></td>
				<#list packages as package>
					<td <#if package.preferred>class="preferred"</#if>>
					</td>
				</#list>
			</tr>
		</tfoot>
			
	</table>
</div>