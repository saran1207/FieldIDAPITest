
${action.setPageType('product', 'edit')!}
<@s.form action="productUpdate" cssClass="fullForm fluidSets" theme="fieldid" >
	<#include "_productForm.ftl"/>
	<@s.hidden name="customerOrderId" />
	<@s.hidden name="tagOptionId" />
	
	<div class="actions">
		<@s.submit id="saveButton" key="hbutton.save" onclick="saveProduct( this ); return false;" cssClass="saveButton"/>
		
		<@s.submit id="saveAndInspButton" name="saveAndInspect" key="hbutton.saveandinspect" onclick="saveProduct( this ); return false;" cssClass="saveButton"/>
		
		<@s.text name="label.or"/>
		<a href="<@s.url action="product"  uniqueID="${product.id}"/>"><@s.text name="label.cancel"/></a>
		
		<@s.url id="merge" action="productMergeAdd" uniqueID="${product.id}"/>
		 | 
		<a href="<@s.url action="productMergeAdd" uniqueID="${product.id}"/>"><@s.text name="label.merge"/></a>
		
		 | 
		<a href="<@s.url  action="productConfirmDelete" uniqueID="${product.id}"/>"><@s.text name="label.delete"/></a>
	</div>
	
</@s.form >
<script type="text/javascript" >
	var buttons = new Array( 'saveButton', 'saveAndInspButton' );
	var buttonLockMessages = new Array( '<@s.text name="hbutton.pleasewait"/>', '<@s.text name="hbutton.pleasewait"/>' );
	var buttonMessages = new Array( '<@s.text name="hbutton.save"/>', '<@s.text name="hbutton.saveandinspect"/>' );
</script>