package com.n4systems.api.conversion.inspection;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.InspectionView;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.Status;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.inspectionschedule.NextInspectionDateByInspectionLoader;
import com.n4systems.model.orgs.BaseOrg;

public class InspectionToViewConverterTest {

	@Test
	public void to_view_copies_simple_fields() throws ConversionException {
		InspectionToViewConverter converter = new InspectionToViewConverter(null) {
//			protected void convertDirectFields(Inspection model, InspectionView view) {}
			protected void convertInspectionStatus(Inspection model, InspectionView view) {}
			protected void convertProductIdentifier(Inspection model, InspectionView view) {}
			protected void converterInspector(Inspection model, InspectionView view) {}
			protected void convertNextDate(Inspection model, InspectionView view) {}
			protected void convertBook(Inspection model, InspectionView view) {}
			protected void convertProductStatus(Inspection model, InspectionView view) {}
			protected void convertOwnerFields(BaseOrg owner, InspectionView view) {}
		};
		
		Inspection model = new Inspection();
		model.setComments("comments");
		model.setDate(new Date());
		model.setLocation("location");
		model.setPrintable(true);
		
		InspectionView view = converter.toView(model);
		
		assertEquals(model.getComments(), view.getComments());
		assertEquals(model.getDate(), view.getInspectionDate());
		assertEquals(model.getLocation(), view.getLocation());
		assertEquals(model.isPrintable(), view.isPrintable());
	}
	
	@Test
	public void to_view_copies_inspection_status() throws ConversionException {
		InspectionToViewConverter converter = new InspectionToViewConverter(null) {
			protected void convertDirectFields(Inspection model, InspectionView view) {}
//			protected void convertInspectionStatus(Inspection model, InspectionView view) {}
			protected void convertProductIdentifier(Inspection model, InspectionView view) {}
			protected void converterInspector(Inspection model, InspectionView view) {}
			protected void convertNextDate(Inspection model, InspectionView view) {}
			protected void convertBook(Inspection model, InspectionView view) {}
			protected void convertProductStatus(Inspection model, InspectionView view) {}
			protected void convertOwnerFields(BaseOrg owner, InspectionView view) {}
		};
		
		Inspection model = new Inspection();
		model.setStatus(Status.PASS);
		
		InspectionView view = converter.toView(model);
		
		assertEquals(model.getStatus().toString(), view.getStatus().toUpperCase());
	}
	
	@Test
	public void to_view_copies_serialnumber_to_identifier() throws ConversionException {
		InspectionToViewConverter converter = new InspectionToViewConverter(null) {
			protected void convertDirectFields(Inspection model, InspectionView view) {}
			protected void convertInspectionStatus(Inspection model, InspectionView view) {}
//			protected void convertProductIdentifier(Inspection model, InspectionView view) {}
			protected void converterInspector(Inspection model, InspectionView view) {}
			protected void convertNextDate(Inspection model, InspectionView view) {}
			protected void convertBook(Inspection model, InspectionView view) {}
			protected void convertProductStatus(Inspection model, InspectionView view) {}
			protected void convertOwnerFields(BaseOrg owner, InspectionView view) {}
		};
		
		Inspection model = new Inspection();
		model.setProduct(ProductBuilder.aProduct().withSerialNumber("serial").build());
		
		InspectionView view = converter.toView(model);
		
		assertEquals(model.getProduct().getSerialNumber(), view.getIdentifier());
	}
	
	@Test
	public void to_view_copies_inspector_full_name() throws ConversionException {
		InspectionToViewConverter converter = new InspectionToViewConverter(null) {
			protected void convertDirectFields(Inspection model, InspectionView view) {}
			protected void convertInspectionStatus(Inspection model, InspectionView view) {}
			protected void convertProductIdentifier(Inspection model, InspectionView view) {}
//			protected void converterInspector(Inspection model, InspectionView view) {}
			protected void convertNextDate(Inspection model, InspectionView view) {}
			protected void convertBook(Inspection model, InspectionView view) {}
			protected void convertProductStatus(Inspection model, InspectionView view) {}
			protected void convertOwnerFields(BaseOrg owner, InspectionView view) {}
		};
		
		Inspection model = new Inspection();
		model.setInspector(UserBuilder.anEmployee().withFirstName("Mark").withLastName("F").build());
		
		InspectionView view = converter.toView(model);
		
		assertEquals(model.getInspector().getFullName(), view.getInspector());
	}
	
	@Test
	public void to_view_copies_loads_next_inspection_date() throws ConversionException {
		Inspection model = new Inspection();
		Date nextDate = new Date();
		
		NextInspectionDateByInspectionLoader loader = createMock(NextInspectionDateByInspectionLoader.class);
		expect(loader.setInspection(model)).andReturn(loader);
		expect(loader.load()).andReturn(nextDate);
		replay(loader);
		
		InspectionToViewConverter converter = new InspectionToViewConverter(loader) {
			protected void convertDirectFields(Inspection model, InspectionView view) {}
			protected void convertInspectionStatus(Inspection model, InspectionView view) {}
			protected void convertProductIdentifier(Inspection model, InspectionView view) {}
			protected void converterInspector(Inspection model, InspectionView view) {}
//			protected void convertNextDate(Inspection model, InspectionView view) {}
			protected void convertBook(Inspection model, InspectionView view) {}
			protected void convertProductStatus(Inspection model, InspectionView view) {}
			protected void convertOwnerFields(BaseOrg owner, InspectionView view) {}
		};
		
		InspectionView view = converter.toView(model);
		
		assertEquals(nextDate, view.getNextInspectionDate());
		
		verify(loader);
	}
	
	@Test
	public void to_view_copies_book_name() throws ConversionException {
		InspectionToViewConverter converter = new InspectionToViewConverter(null) {
			protected void convertDirectFields(Inspection model, InspectionView view) {}
			protected void convertInspectionStatus(Inspection model, InspectionView view) {}
			protected void convertProductIdentifier(Inspection model, InspectionView view) {}
			protected void converterInspector(Inspection model, InspectionView view) {}
			protected void convertNextDate(Inspection model, InspectionView view) {}
//			protected void convertBook(Inspection model, InspectionView view) {}
			protected void convertProductStatus(Inspection model, InspectionView view) {}
			protected void convertOwnerFields(BaseOrg owner, InspectionView view) {}
		};
		
		InspectionBook book = new InspectionBook();
		book.setName("inspection book");
		
		Inspection model = new Inspection();
		model.setBook(book);
		
		InspectionView view = converter.toView(model);
		
		assertEquals(model.getBook().getName(), view.getInspectionBook());
	}
	
	@Test
	public void to_view_allows_null_books() throws ConversionException {
		InspectionToViewConverter converter = new InspectionToViewConverter(null) {
			protected void convertDirectFields(Inspection model, InspectionView view) {}
			protected void convertInspectionStatus(Inspection model, InspectionView view) {}
			protected void convertProductIdentifier(Inspection model, InspectionView view) {}
			protected void converterInspector(Inspection model, InspectionView view) {}
			protected void convertNextDate(Inspection model, InspectionView view) {}
//			protected void convertBook(Inspection model, InspectionView view) {}
			protected void convertProductStatus(Inspection model, InspectionView view) {}
			protected void convertOwnerFields(BaseOrg owner, InspectionView view) {}
		};
		
		InspectionView view = converter.toView(new Inspection());
		
		assertNull(view.getInspectionBook());
	}
	
	@Test
	public void to_view_copies_product_status_name() throws ConversionException {
		InspectionToViewConverter converter = new InspectionToViewConverter(null) {
			protected void convertDirectFields(Inspection model, InspectionView view) {}
			protected void convertInspectionStatus(Inspection model, InspectionView view) {}
			protected void convertProductIdentifier(Inspection model, InspectionView view) {}
			protected void converterInspector(Inspection model, InspectionView view) {}
			protected void convertNextDate(Inspection model, InspectionView view) {}
			protected void convertBook(Inspection model, InspectionView view) {}
//			protected void convertProductStatus(Inspection model, InspectionView view) {}
			protected void convertOwnerFields(BaseOrg owner, InspectionView view) {}
		};
		
		ProductStatusBean pse = new ProductStatusBean();
		pse.setName("product status");
		
		Inspection model = new Inspection();
		model.setProductStatus(pse);
		
		InspectionView view = converter.toView(model);
		
		assertEquals(model.getProductStatus().getName(), view.getProductStatus());
	}
	
	@Test
	public void to_view_allows_null_product_status() throws ConversionException {
		InspectionToViewConverter converter = new InspectionToViewConverter(null) {
			protected void convertDirectFields(Inspection model, InspectionView view) {}
			protected void convertInspectionStatus(Inspection model, InspectionView view) {}
			protected void convertProductIdentifier(Inspection model, InspectionView view) {}
			protected void converterInspector(Inspection model, InspectionView view) {}
			protected void convertNextDate(Inspection model, InspectionView view) {}
			protected void convertBook(Inspection model, InspectionView view) {}
//			protected void convertProductStatus(Inspection model, InspectionView view) {}
			protected void convertOwnerFields(BaseOrg owner, InspectionView view) {}
		};
		
		InspectionView view = converter.toView(new Inspection());
		
		assertNull(view.getProductStatus());
	}
	
	@Test
	public void to_view_copies_owner_fields() throws ConversionException {
		InspectionToViewConverter converter = new InspectionToViewConverter(null) {
			protected void convertDirectFields(Inspection model, InspectionView view) {}
			protected void convertInspectionStatus(Inspection model, InspectionView view) {}
			protected void convertProductIdentifier(Inspection model, InspectionView view) {}
			protected void converterInspector(Inspection model, InspectionView view) {}
			protected void convertNextDate(Inspection model, InspectionView view) {}
			protected void convertBook(Inspection model, InspectionView view) {}
			protected void convertProductStatus(Inspection model, InspectionView view) {}
//			protected void convertOwnerFields(BaseOrg owner, InspectionView view) {}
		};
		
		BaseOrg org = OrgBuilder.aDivisionOrg().withName("division").withParent(
				OrgBuilder.aCustomerOrg().withName("customer").withParent(
						OrgBuilder.aPrimaryOrg().withName("primary").build()
				).build()
		).build();
		
		
		Inspection model = new Inspection();
		model.setOwner(org);
		
		InspectionView view = converter.toView(model);
		
		assertEquals(model.getOwner().getName(), view.getDivision());
		assertEquals(model.getOwner().getParent().getName(), view.getCustomer());
		assertEquals(model.getOwner().getParent().getParent().getName(), view.getOrganization());
	}
}
