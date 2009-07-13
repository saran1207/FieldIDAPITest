
${action.setPageType('product', 'edit')!}
<@s.form action="productUpdate" cssClass="inputForm" theme="css_xhtml" >
	<#include "_productForm.ftl"/>
	<@s.hidden name="customerOrderId" />
	<@s.hidden name="tagOptionId" />
	
	<div class="formAction">
		<@s.url id="merge" action="productMergeAdd" uniqueID="${product.id}"/>
		<@s.reset id="merge" name="merge" key="label.merge" onclick="return redirect('${merge}');" />
		<@s.url id="deleteUrl" action="productConfirmDelete" uniqueID="${product.id}"/>
		<@s.reset id="delete" name="delete" key="hbutton.delete" onclick="return redirect('${deleteUrl}');" />
		<@s.url id="producturl" action="product"  uniqueID="${product.id}"/>
		<@s.reset id="cancel" name="cancel" key="hbutton.cancel" onclick="window.location = '${producturl}'; return false;" />
		<@s.submit id="saveButton" key="hbutton.save" onclick="saveProduct( this ); return false;" />
	</div>
	
</@s.form >
<script type="text/javascript" >
	var buttons = new Array( 'saveButton' );
	var buttonLockMessages = new Array( '<@s.text name="hbutton.pleasewait" />' );
	var buttonMessages = new Array( '<@s.text name="hbutton.save" />' );
</script>