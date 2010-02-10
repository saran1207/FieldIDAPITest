package com.n4systems.fieldid.admin;

import java.io.FileInputStream;
import static watij.finders.FinderFactory.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.n4systems.fieldid.FieldIDMisc;
import com.n4systems.fieldid.datatypes.ConfirmProductTypeDelete;
import com.n4systems.fieldid.datatypes.ProductAttribute;
import com.n4systems.fieldid.datatypes.ProductAttributeType;
import com.n4systems.fieldid.datatypes.ProductType;

import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class ManageProductTypes extends TestCase {

	IE ie = null;
	Properties p;
	InputStream in;
	String propertyFile = "manageproducttypes.properties";
	Finder manageProductTypesFinder;
	Finder manageProductTypesContentHeader;
	Finder addProductTypeFinder;
	Finder addProductTypeGroupFinder;
	Finder addProductTypeNameFinder;
	Finder addProductTypeWarningsFinder;
	Finder addProductTypeInstructionsFinder;
	Finder addProductTypeCautionsURLFinder;
	Finder addProductTypeHasManufacturerCertificate;
	Finder addProductTypeManufacturerCertificateTextFinder;
	Finder addProductTypeProductDescriptionTemplateFinder;
	Finder addProductTypeUploadImageFinder;
	Finder addProductTypeAddAttributesButtonFinder;
	Finder addProductTypeAttachAFileButtonFinder;
	Finder addProductTypeSaveButtonFinder;
	Finder viewProductTypeContentHeader;
	Finder editProductTypeLink2Finder;
	Finder editProductTypeDeleteButtonFinder;
	Finder confirmDeleteProductTypeContentHeaderFinder;
	Finder confirmDeleteProductTypeDeleteButtonFinder;
	Finder addProductTypeAttributeButtonFinder;
	private Finder productTypeViewAllLinkFinder;
	private Finder manageProductTypesLinksFinder;
	private Finder manageProductTypeInspectionTypeLinkFinder;
	private Finder saveInspectionTypesFinder;
	private Finder subComponentsFinder;
	private Finder subComponentListFinder;
	private Finder subComponentSelectListFinder;
	private Finder addSubComponentButtonFinder;
	private Finder saveSubComponentsButtonFinder;

	public ManageProductTypes(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			manageProductTypesFinder = xpath(p.getProperty("manageproducttype", "NOT SET"));
			manageProductTypesContentHeader = xpath(p.getProperty("listproducttypecontentheader", "NOT SET"));
			addProductTypeFinder = xpath(p.getProperty("addproducttype", "NOT SET"));
			addProductTypeGroupFinder = id(p.getProperty("addproducttypegroup", "NOT SET"));
			addProductTypeNameFinder = id(p.getProperty("addproducttypename", "NOT SET"));
			addProductTypeWarningsFinder = id(p.getProperty("addproducttypewarnings", "NOT SET"));
			addProductTypeInstructionsFinder = id(p.getProperty("addproducttypeinstructions", "NOT SET"));
			addProductTypeCautionsURLFinder = id(p.getProperty("addproducttypecautionsurl", "NOT SET"));
			addProductTypeHasManufacturerCertificate = id(p.getProperty("addproducttypehasmanufacturercertificate", "NOT SET"));
			addProductTypeManufacturerCertificateTextFinder = id(p.getProperty("addproducttypemanufacturercertificate", "NOT SET"));
			addProductTypeProductDescriptionTemplateFinder = id(p.getProperty("addproducttypeproductdescriptiontemplate", "NOT SET"));
			addProductTypeUploadImageFinder = id(p.getProperty("addproducttypeuploadimage", "NOT SET"));
			addProductTypeAddAttributesButtonFinder = xpath(p.getProperty("addproducttypeaddattributesbutton", "NOT SET"));
			addProductTypeAttachAFileButtonFinder = text(p.getProperty("addproducttypeattachafilebutton", "NOT SET"));
			addProductTypeSaveButtonFinder = id(p.getProperty("addproducttypesavebutton", "NOT SET"));
			viewProductTypeContentHeader = xpath(p.getProperty("viewproducttypecontentheader", "NOT SET"));
			editProductTypeLink2Finder = xpath(p.getProperty("editproducttype2", "NOT SET"));
			editProductTypeDeleteButtonFinder = xpath(p.getProperty("editproducttypedeletebutton", "NOT SET"));
			confirmDeleteProductTypeContentHeaderFinder = xpath(p.getProperty("confirmproducttypedeletepageheader", "NOT SET"));
			confirmDeleteProductTypeDeleteButtonFinder = id(p.getProperty("confirmproducttypedeletedeletebutton", "NOT SET"));
			addProductTypeAttributeButtonFinder = xpath(p.getProperty("addproducttypeattributeaddbutton", "NOT SET"));
			productTypeViewAllLinkFinder = xpath(p.getProperty("viewalllink", "NOT SET"));
			manageProductTypesLinksFinder = xpath(p.getProperty("manageproducttypeslinks", "NOT SET"));
			manageProductTypeInspectionTypeLinkFinder = xpath(p.getProperty("manageproducttypeinspectiontypes", "NOT SET"));
			saveInspectionTypesFinder = xpath(p.getProperty("inspectiontypessavebutton", "NOT SET"));
			subComponentsFinder = xpath(p.getProperty("manageproducttypesubcomponents", "NOT SET"));
			subComponentListFinder = xpath(p.getProperty("subcomponentlist", "NOT SET"));
			subComponentSelectListFinder = xpath(p.getProperty("subcomponentselectlist", "NOT SET"));
			addSubComponentButtonFinder = xpath(p.getProperty("addsubcomponentbutton", "NOT SET"));
			saveSubComponentsButtonFinder = xpath(p.getProperty("savesubcomponentsbutton", "NOT SET"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile	+ "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception occurred");
		}
	}

	/**
	 * Goes to the Manage Product Types section. It assumes you are already on
	 * the Administration page and have the appropriate permission for accessing
	 * this section.
	 * 
	 * @throws Exception
	 */
	public void gotoManageProductTypes() throws Exception {
		Link manageProductTypes = ie.link(manageProductTypesFinder);
		assertTrue("Could not find the link to Manage Product Types", manageProductTypes.exists());
		manageProductTypes.click();
		checkListProductTypesContentHeader();
	}

	/**
	 * Checks for the string "List Product Types" underneath the company logo
	 * and for a link to "Add Product Type".
	 * 
	 * @throws Exception
	 */
	private void checkListProductTypesContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(manageProductTypesContentHeader);
		assertTrue("Could not find the content header for the Manage Product Type page", contentHeader.exists());
		Link addProductType = ie.link(addProductTypeFinder);
		assertTrue("Could not find the Add Product Type link on the Manage Product Types page", addProductType.exists());
	}

	/**
	 * Checks to see if the link to Manage Product Types exists. Assumes you are
	 * already on the Administration page.
	 * 
	 * @return true if the Manage Product Type link is available.
	 * @throws Exception
	 */
	public boolean isManageProductTypes() throws Exception {
		Link manageProductTypes = ie.link(manageProductTypesFinder);
		return manageProductTypes.exists();
	}

	/**
	 * Assumes you are at the List Product Types page, it will click the link to
	 * add product type then fill in all the fields using the ProductType class
	 * provided.
	 * 
	 * @param npt
	 * @throws Exception
	 */
	public void gotoAddProductType() throws Exception {
		Link addProductType = ie.link(addProductTypeFinder);
		assertTrue("Could not find a link to Add Product Type", addProductType.exists());
		addProductType.click();
	}

	/**
	 * Fill in the form for adding a new product type. This assumes you
	 * are already on the page to add a new product type, that is you
	 * have used the gotoAddProductType() method.
	 * 
	 * TODO
	 * 	add ability to add attributes
	 * 	add ability to upload image
	 * 	add ability to attach a file
	 * 
	 * @param npt
	 * @throws Exception
	 */
	public void setAddProductTypeForm(ProductType npt) throws Exception {
		assertNotNull(npt);
		assertNotNull("Product Type Name is a required field.", npt.getName());
		assertFalse("Product Type Name is a required field.", npt.getName().equals(""));
		
		FieldIDMisc.stopMonitor();
		SelectList group = ie.selectList(addProductTypeGroupFinder);
		assertTrue("Could not find the select list for Group", group.exists());
		String groupOption = npt.getGroup(); 
		if(groupOption != null) {
			Option o = group.option(text(groupOption));
			assertTrue("Could not find the group '" + groupOption + "'", o.exists());
			o.select();
		}
		TextField name = ie.textField(addProductTypeNameFinder);
		assertTrue("Could not find the text field for Name", name.exists());
		name.set(npt.getName());
		TextField warnings = ie.textField(addProductTypeWarningsFinder);
		assertTrue("Could not find the text field for Warnings", warnings.exists());
		if(npt.getWarnings() != null) {
			warnings.set(npt.getWarnings());
		}
		TextField instructions = ie.textField(addProductTypeInstructionsFinder);
		assertTrue("Could not find the text field for Instructions", instructions.exists());
		if(npt.getInstructions() != null) {
			instructions.set(npt.getInstructions());
		}
		TextField cautionsURL = ie.textField(addProductTypeCautionsURLFinder);
		assertTrue("Could not find the text field for Cautions URL", cautionsURL.exists());
		if(npt.getCautionsURL() != null) {
			cautionsURL.set(npt.getCautionsURL());
		}
		Checkbox hasManufacturerCertificate = ie.checkbox(addProductTypeHasManufacturerCertificate);
		assertTrue("Could not find the check box for Has Manufacturer Certificate", hasManufacturerCertificate.exists());
		hasManufacturerCertificate.set(npt.getHasManufacturerCertificate());
		TextField manufacturerCertificateText = ie.textField(addProductTypeManufacturerCertificateTextFinder);
		assertTrue("Could not find the text field for Manufacturer Certificate Text", manufacturerCertificateText.exists());
		if(npt.getManufacturerCertificateText() != null) {
			manufacturerCertificateText.set(npt.getManufacturerCertificateText());
		}
		TextField productDescriptionTemplate = ie.textField(addProductTypeProductDescriptionTemplateFinder);
		assertTrue("Could not find the text field for Product Description Template", productDescriptionTemplate.exists());
		if(npt.getProductDescriptionTemplate() != null) {
			productDescriptionTemplate.set(npt.getProductDescriptionTemplate());
		}
		Span uploadImage = ie.span(addProductTypeUploadImageFinder);
		assertTrue("Could not find the span for Upload Image fragment", uploadImage.exists());
		if(npt.getUploadImage() != null) {
			fail("Upload Image not supported");
		}
		Button addAttributes = ie.button(addProductTypeAddAttributesButtonFinder);
		assertTrue("Could not find the button to add attributes", addAttributes.exists());
		ProductAttribute[] pat = npt.getAttributes();
		if(pat != null) {
			setProductAttributes(pat);
		}
		if(npt.getAttachments() != null && !npt.getAttachments().isEmpty()) {
			Button attachAFile = ie.button(addProductTypeAttachAFileButtonFinder);
			assertTrue("Could not find the button to attach a file", attachAFile.exists());
			fail("Add Attachments not supported");
		}
		FieldIDMisc.startMonitor();
	}

	// TODO: TEST
	private void setProductAttributes(ProductAttribute[] pat) throws Exception {
		assertNotNull(pat);
		assertTrue(pat.length > 0);
		FieldIDMisc.stopMonitor();
		int dropDownOptionIndex = 0;
		for(int i = 0; i < pat.length; i++) {
			ProductAttribute pa = pat[i];
			assertNotNull("No name defined for this attribute", pa.getName());
			assertNotNull("No type defined for this attribute", pa.getType());
			Button add = ie.button(addProductTypeAttributeButtonFinder);
			assertTrue("Could not find the Add button for Attributes", add.exists());
			add.click();
			Div currentRow = ie.div(xpath("//DIV[@id='field_" + i + "']"));
			TextField name = ie.textField(id("productTypeUpdate_infoFields_" + i + "__name"));
			assertTrue("Could not find the Name field for product attribute "+ i, name.exists());
			name.set(pa.getName());
			SelectList type = ie.selectList(id("productTypeUpdate_infoFields_" + i + "__fieldType"));
			assertTrue("Could not find the Type field for product attribute " + i, type.exists());
			Option o = type.option(text(pa.getType()));
			assertTrue("Could not find the option '" + pa.getType() + "' on the select list", o.exists());
			o.select();
			Checkbox required = ie.checkbox(id("productTypeUpdate_infoFields_" + i + "__required"));
			assertTrue("Could not find the Required checkbox for product attribute " + i, required.exists());
			required.set(pa.getRequired());
			// handle each type
			if(pa.getType().equals(ProductAttributeType.SelectBox)) {
				String[] selectDropDowns = pa.getProductAttributeType().getDropDowns();
				Link editDropDown = currentRow.link(xpath("DIV/A[contains(text(),'Edit Drop Down')]"));
				assertTrue("Could not find the Edit Drop Down link for the current row", editDropDown.exists());
				editDropDown.click();
				for(int j = 0; j < selectDropDowns.length; j++) {
					Button add2 = ie.button(xpath("//DIV[@id='infoOptionContainer_" + i + "']/INPUT[@value='Add']"));
					assertTrue("Could not find the Add button to add Drop Down Options", add2.exists());
					add2.click();
					TextField dd = ie.textField(id("productTypeUpdate_editInfoOptions_" + dropDownOptionIndex + "__name"));
					dropDownOptionIndex++;
					assertTrue("Could not find the text field for the drop down being added", dd.exists());
					dd.set(selectDropDowns[j]);
				}
			} else if(pa.getType().equals(ProductAttributeType.ComboBox)) {
				String[] comboDropDowns = pa.getProductAttributeType().getDropDowns();
				Link editDropDown = currentRow.link(xpath("DIV/A[contains(text(),'Edit Drop Down')]"));
				assertTrue("Could not find the Edit Drop Down link for the current row", editDropDown.exists());
				editDropDown.click();
				for(int j = 0; j < comboDropDowns.length; j++) {
					Button add2 = ie.button(xpath("//DIV[@id='infoOptionContainer_" + i + "']/INPUT[@value='Add']"));
					assertTrue("Could not find the Add button to add Drop Down Options", add2.exists());
					add2.click();
					TextField dd = ie.textField(id("productTypeUpdate_editInfoOptions_" + dropDownOptionIndex + "__name"));
					dropDownOptionIndex++;
					assertTrue("Could not find the text field for the drop down being added", dd.exists());
					dd.set(comboDropDowns[j]);
				}
			} else if(pa.getType().equals(ProductAttributeType.UnitOfMeasure)) {
				String def = pa.getProductAttributeType().getDefaultUnitOfMeasure();
				assertNotNull("You defined a Unit Of Measure but supplied no default value", def);
				SelectList defMeasure = ie.selectList(id("productTypeUpdate_infoFields_" + i + "__defaultUnitOfMeasure"));
				assertTrue("Could not find the select list for default unit of measure", defMeasure.exists());
				Option o2 = defMeasure.option(text(def));
				assertTrue("Could not find the default unit of measure option '" + def + "'", o2.exists());
				o2.select();
			}
		}
		FieldIDMisc.startMonitor();
	}

	private void checkViewProductTypePageContentHeader(String name) throws Exception {
		HtmlElement contentHeader = ie.htmlElement(viewProductTypeContentHeader);
		assertTrue("Could not find the content header for the Manage Product Type - " + name + " page", contentHeader.exists());
//		String expecting = "Manage Product Type - " + name;
//		String found = contentHeader.text().trim();
// TODO: waiting for WEB-991 to be fixed
// 		assertTrue("Was expecting '" + expecting + "' but found '" + found + "'", found.contains(expecting));
	}


	/**
	 * Click the Save button on Add Product Type. This assumes you have called
	 * setAddProductTypeForm and filled in the form.
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void addProductType(String name) throws Exception {
		Button save = ie.button(addProductTypeSaveButtonFinder);
		assertTrue("Could not find the button to save", save.exists());
		save.click();
		FieldIDMisc misc = new FieldIDMisc(ie);
		misc.checkForErrorMessagesOnCurrentPage();
		checkViewProductTypePageContentHeader(name);
	}

	public void deleteProductType(String name) throws Exception {
		Button delete = ie.button(editProductTypeDeleteButtonFinder);
		assertTrue("Could not find the Delete button on the Edit Product Type page", delete.exists());
		delete.click();
		checkConfirmProductTypeDeletePageContent(name);
	}
	
	private void checkConfirmProductTypeDeletePageContent(String name) throws Exception {
		HtmlElement contentHeader = ie.htmlElement(confirmDeleteProductTypeContentHeaderFinder);
		assertTrue("Could not find the content header for the Confirm Product Type Delete - " + name + " page", contentHeader.exists());
		String expecting = "Confirm Product Type Delete - " + name;
		String found = contentHeader.text().trim();
 		assertTrue("Was expecting '" + expecting + "' but found '" + found + "'", found.contains(expecting));
	}

	public void confirmDeleteProductType() throws Exception {
		Button delete = ie.button(confirmDeleteProductTypeDeleteButtonFinder);
		assertTrue("Could not find the Delete button on the Confirm Product Type Delete page", delete.exists());
		delete.click();
		ie.waitUntilReady();
		checkManageProductTypesContentHeader();
	}

	/**
	 * Page is now named Manage Product Types rather than
	 * List Product Types. This method is just to make the
	 * code more readable.
	 * 
	 * @throws Exception
	 */
	private void checkManageProductTypesContentHeader() throws Exception {
		checkListProductTypesContentHeader();
	}

	/**
	 * There are two places to go to Edit Product Type. One is from
	 * the Manage Product Types page, clicking an Edit link. The other
	 * is from the Manage Product Type page. You are either on the
	 * view, Select Inspection Types, Inspection Frequencies or Product
	 * Configuration page and want to switch to the Edit page. This
	 * method is for the latter.
	 * 
	 * @param name
	 */
	public void gotoEditProductType2(String name) throws Exception {
		Link edit = ie.link(editProductTypeLink2Finder);
		assertTrue("Could not find the Edit link for Manage Product Type - " + name, edit.exists());
		edit.click();
		checkManageProductTypePageContentHeader(name);
	}

	private void checkManageProductTypePageContentHeader(String name) throws Exception {
		checkViewProductTypePageContentHeader(name);		
	}

	public boolean isProductType(String name) throws Exception {
		boolean result = false;
		Finder productTypeLinkFinder = xpath("//DIV[@id='pageContent']/TABLE/TBODY/TR/TD[1]/A[text()='" + name + "']");
		Link productType = ie.link(productTypeLinkFinder);
		result = productType.exists();
		return result;
	}

	/**
	 * There are two places to go to Edit Product Type. One is from
	 * the Manage Product Types page, clicking an Edit link. The other
	 * is from the Manage Product Type page. You are either on the
	 * view, Select Inspection Types, Inspection Frequencies or Product
	 * Configuration page and want to switch to the Edit page. This
	 * method is for the former.
	 * 
	 * @param name
	 */
	public void gotoEditProductType(String name) throws Exception {
		Link edit = ie.link(xpath("//DIV[@id='pageContent']/TABLE/TBODY/TR/TD[1]/A[text()='" + name + "']/../../TD/A[text()='Edit']"));
		assertTrue("Could not find the Edit link for '" + name + "'", edit.exists());
		edit.click();
		checkManageProductTypePageContentHeader(name);
	}

	public ConfirmProductTypeDelete getProductTypeRemovalDetails() throws Exception {
		ConfirmProductTypeDelete results = new ConfirmProductTypeDelete();
		Divs removalDetails = ie.divs(xpath("//DIV[@id='pageContent']/DIV/DIV[@class='sectionContent']/DIV[@class='infoSet']"));
		assertNotNull(removalDetails);
		Iterator<Div> i = removalDetails.iterator();
		while(i.hasNext()) {
			Div d = i.next();
			Label count = d.label(0);
			long n = Long.parseLong(count.text().trim());
			Span s = d.span(0);
			if(s.text().contains("Product(s) will be deleted.")) {
				results.setProducts(n);
			} else if(s.text().contains("Inspection(s) will be deleted.")) {
				results.setInspections(n);
			} else if(s.text().contains("Schedule(s) will be deleted.")) {
				results.setSchedules(n);
			} else if(s.text().contains("Product Code Mapping(s) being deleted.")) {
				results.setProductCodeMappings(n);
			} else if(s.text().contains("Product Type(s) will have this removed as a Sub Product Type.")) {
				results.setMasterProductTypes(n);
			} else if(s.text().contains("Product(s) will be detached from their Master Products.")) {
				results.setMasterProducts(n);
			} else if(s.text().contains("Sub Product(s) being detached from deleted Master Products")) {
				results.setSubProducts(n);
			}
		}
		return results;
	}
	
	public void validate() throws Exception {
		assertTrue(isManageProductTypes());
		gotoManageProductTypes();
		List<String> productTypeNames = getProductTypeNames();
		assertTrue(productTypeNames.size() > 0);	// there should always be at least one product type, e.g. '*'
		gotoAddProductType();
		FieldIDMisc misc = new FieldIDMisc(ie);
		String name = "validate-" + misc.getRandomInteger();
		ProductType npt = new ProductType(name);
		npt.setCautionsURL("http://www.fieldid.com/fieldid/");
		npt.setHasManufacturerCertificate(true);
		npt.setInstructions("Instructions go here.");
		npt.setManufacturerCertificateText("Manufacturer Certificate Text");
		npt.setWarnings("Warning! Warning! Danger Will Robinson!");
		setAddProductTypeForm(npt);
		addProductType(name);
		gotoEditProductType2(name);
		gotoViewAll();
		assertTrue(isProductType(name));
		gotoEditProductType(name);
		deleteProductType(name);
		ConfirmProductTypeDelete results = getProductTypeRemovalDetails();
		assertTrue(results.getProducts() == 0);
		assertTrue(results.getInspections() == 0);
		assertTrue(results.getSchedules() == 0);
		assertTrue(results.getProductCodeMappings() == 0);
		assertTrue(results.getSubProducts() == 0);
		assertTrue(results.getMasterProductTypes() == 0);
		assertTrue(results.getMasterProducts() == 0);
		confirmDeleteProductType();
	}

	public void gotoViewAll() throws Exception {
		Link viewAll = ie.link(productTypeViewAllLinkFinder);
		assertTrue("Could not find the link to View All", viewAll.exists());
		viewAll.click();
		checkListProductTypesContentHeader();
	}
	
	/**
	 * Assumes you are on the Manage Product Types page.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getMasterProductTypeNames() throws Exception {
		List<String> results = new ArrayList<String>();
		List<String> links = new ArrayList<String>();
		Links productTypes = helperGetProductTypeLinks();
		Iterator<Link> i = productTypes.iterator();
		while(i.hasNext()) {
			Link pt = i.next();
			String name = pt.text().trim();
			links.add(name);
		}
		
		Iterator<String> j = links.iterator();
		while(j.hasNext()) {
			String name = j.next();
			Link pt = ie.link(text(name));
			pt.click();
			gotoSubComponents(name);
			List<String> tmp = getSubComponents();
			if(tmp.size() > 0) {
				results.add(name);
			}
			gotoViewAll();
		}
	
		return results;
	}
	
	public List<String> getProductTypeNames() throws Exception {
		List<String> names = new ArrayList<String>();
		Links productTypes = helperGetProductTypeLinks();
		Iterator<Link> i = productTypes.iterator();
		while(i.hasNext()) {
			Link pt = i.next();
			String name = pt.text().trim();
			names.add(name);
		}
		return names;
	}
	
	private Links helperGetProductTypeLinks() throws Exception {
		Links productTypes = ie.links(manageProductTypesLinksFinder);
		assertNotNull(productTypes);
		return productTypes;
	}

	public void gotoInspectionTypes(String name) throws Exception {
		Link l = ie.link(manageProductTypeInspectionTypeLinkFinder);
		assertTrue("Could not find the link to Inspection Types", l.exists());
		l.click();
		this.checkManageProductTypePageContentHeader(name);
	}

	public void setInspectionType(String inspectionType) throws Exception {
		Checkbox c = ie.checkbox(xpath("//FORM[@id='productTypeEventTypesSave']/TABLE[@class='list']/TBODY/TR/TD[@class='name' and contains(text(),'" + inspectionType + "')]/../TD[1]/INPUT"));
		assertTrue("Could not find the checkbox for inspection type '" + inspectionType + "'", c.exists());
		c.set(true);
	}

	public void saveInspectionTypes(String productType) throws Exception {
		Button b = ie.button(saveInspectionTypesFinder);
		assertTrue("Could not find the button to save Inspection Types for '" + productType + "'", b.exists());
		b.click();
		FieldIDMisc misc = new FieldIDMisc(ie);
		misc.checkForErrorMessagesOnCurrentPage(); 
		checkManageProductTypePageContentHeader(productType);
	}

	public void gotoSubComponents(String productType) throws Exception {
		Link l = ie.link(subComponentsFinder);
		assertTrue("Could not find the link to Sub-Components", l.exists());
		l.click();
		checkManageProductTypePageContentHeader(productType);
	}

	public List<String> getSubComponents() throws Exception {
		List<String> results = new ArrayList<String>();
		
		Spans subcomponents = ie.spans(subComponentListFinder);
		assertNotNull(subcomponents);
		Iterator<Span> i = subcomponents.iterator();
		while(i.hasNext()) {
			Span subcomponent = i.next();
			String s = subcomponent.text().trim();
			results.add(s);
		}
		
		return results;
	}

	public void addSubComponent(String subProductType) throws Exception {
		SelectList sl = ie.selectList(subComponentSelectListFinder);
		assertTrue("Could not find the select list of product types to add", sl.exists());
		Option o = sl.option(text(subProductType));
		assertTrue("Could not find the product type '" + subProductType + "' on the Add list", o.exists());
		o.select();
		Button add = ie.button(addSubComponentButtonFinder);
		assertTrue("Could not find the button to add a subcomponent", add.exists());
		add.click();
	}

	public void saveSubComponents(String masterProductType) throws Exception {
		Button save = ie.button(saveSubComponentsButtonFinder);
		assertTrue("Could not find the Save button on Sub-Components", save.exists());
		save.click();
		checkManageProductTypePageContentHeader(masterProductType);
	}

	public void gotoProductType(String productType) throws Exception {
		assertNotNull(productType);
		Links pts = helperGetProductTypeLinks();
		Link pt = pts.link(productType);
		assertTrue("Could not find a link to view '" + productType + "'", pt.exists());
		pt.click();
		checkViewProductTypePageContentHeader(productType);
	}
}
