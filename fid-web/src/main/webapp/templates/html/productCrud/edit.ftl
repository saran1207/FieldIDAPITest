
${action.setPageType('product', 'edit')!}
<@s.form action="productUpdate" cssClass="fullForm fluidSets" theme="fieldid" id="productUpdate" >
	<#include "_productForm.ftl"/>
	<@s.hidden name="customerOrderId" />
	<@s.hidden name="tagOptionId" />
	
	<div class="actions">
		<@s.submit id="saveButton" key="hbutton.save" onclick=" return false;" cssClass="saveButton save"/>
		
		<@s.submit id="saveAndInspButton" name="saveAndInspect" key="hbutton.saveandinspect"  cssClass="saveButton save"/>
		
		<@s.text name="label.or"/>
		<a href="<@s.url action="product"  uniqueID="${product.id}"/>"><@s.text name="label.cancel"/></a>
		
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
	$$('#productUpdate .save').each(function(element) {
			element.observe('click', function(event) {
				var element = Event.element(event);
				event.stop();
				saveProduct(element);
			});
		});
</script>