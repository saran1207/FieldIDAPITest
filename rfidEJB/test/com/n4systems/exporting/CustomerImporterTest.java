package com.n4systems.exporting;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.CustomerOrgViewConverter;
import com.n4systems.api.conversion.DivisionOrgViewConverter;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.ViewValidator;
import com.n4systems.exporting.beanutils.ExportMapUnmarshaler;
import com.n4systems.exporting.beanutils.MarshalingException;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.testutils.DummyTransaction;

@SuppressWarnings("unchecked")
public class CustomerImporterTest {
	private static final String[] titles = {"title1", "title2"};
	private static final FullExternalOrgView[] orgViews = {FullExternalOrgView.newCustomer(), FullExternalOrgView.newDivision()};
	private static final Map[] mapRows = {new LinkedHashMap<String, String>(), new LinkedHashMap<String, String>()};
	
	static {
		mapRows[0].put(titles[0], "value1A");
		mapRows[0].put(titles[1], "value1B");
		
		mapRows[1].put(titles[0], "value2A");
		mapRows[1].put(titles[1], "value2B");
	}
	
	@Test(expected=IllegalStateException.class)
	public void run_import_throws_exception_if_validate_not_called_first() throws ImportException {
		CustomerImporter importer = new CustomerImporter(null, null, null, null, null, null);
		
		importer.runImport();
	}

	@Test
	public void test_read_and_validate() throws IOException, ParseException, MarshalingException {
		MapReader reader = createMock(MapReader.class);
		ViewValidator<FullExternalOrgView> validator = createMock(ViewValidator.class);
		
		final ExportMapUnmarshaler<FullExternalOrgView> unmarshaler = createMock(ExportMapUnmarshaler.class);
		
		CustomerImporter importer = new CustomerImporter(reader, null, null, validator, null, null) {
			@Override
			protected ExportMapUnmarshaler<FullExternalOrgView> createMapUnmarshaler() throws IOException, ParseException {
				return unmarshaler;
			}
		};
		
		for (int i = 0; i < mapRows.length; i++) {
			expect(reader.readMap()).andReturn(mapRows[i]);
			expect(unmarshaler.toBean(mapRows[i])).andReturn(orgViews[i]);
		}
		expect(reader.readMap()).andReturn(null);
		
		for (int i = 0; i < orgViews.length; i++) {
			expect(validator.validate(orgViews[i], i + CustomerImporter.FIRST_DATA_ROW)).andReturn(Arrays.asList(ValidationResult.pass(), ValidationResult.pass()));
		}
		
		replay(reader);
		replay(unmarshaler);
		replay(validator);
		
		List<ValidationResult> results = importer.readAndValidate();
		
		assertEquals(4, results.size());
		
		verify(reader);
		verify(unmarshaler);
		verify(validator);
	}
	
	@Test
	public void read_and_validate_reads_once() throws IOException, ParseException, MarshalingException {
		Validator<FullExternalOrgView> validator = createMock(Validator.class);
		
		final AtomicInteger readCount = new AtomicInteger();
		
		CustomerImporter importer = new CustomerImporter(null, null, null, validator, null, null) {
			@Override
			protected List<FullExternalOrgView> readAllRows() throws IOException, ParseException, MarshalingException {
				readCount.incrementAndGet();
				return Arrays.asList(orgViews);
			}
		};
		
		for (int i = 0; i < orgViews.length * 2; i++) {
			expect(validator.validate((FullExternalOrgView)anyObject(), anyInt())).andReturn(Collections.EMPTY_LIST);
		}
		
		replay(validator);
		
		importer.readAndValidate();
		
		assertEquals(1, readCount.get());
		
		importer.readAndValidate();
		
		assertEquals(1, readCount.get());
		
		verify(validator);
	}
	
	@Test
	public void test_import() throws IOException, ParseException, MarshalingException, ImportException, ConversionException {
		Transaction trans = new DummyTransaction();
		CustomerOrg cust = OrgBuilder.aCustomerOrg().buildCustomer();
		DivisionOrg div = OrgBuilder.aDivisionOrg().buildDivision();
		
		TransactionManager transManager = createMock(TransactionManager.class);
		Saver<BaseOrg> orgSaver = createMock(Saver.class);
		CustomerOrgViewConverter custConverter = createMock(CustomerOrgViewConverter.class);
		DivisionOrgViewConverter divConverter = createMock(DivisionOrgViewConverter.class);
		
		CustomerImporter importer = new CustomerImporter(null, transManager, orgSaver, null, custConverter, divConverter) {
			@Override
			protected List<ValidationResult> validateRows() {
				return Collections.EMPTY_LIST;
			}

			@Override
			protected List<FullExternalOrgView> readAllRows() throws IOException, ParseException, MarshalingException {
				return Arrays.asList(orgViews);
			}
		};
		
		expect(transManager.startTransaction()).andReturn(trans);
		
		expect(custConverter.toModel(orgViews[0])).andReturn(cust);
		expect(orgSaver.saveOrUpdate(trans, cust)).andReturn(cust);
		divConverter.setParentCustomer(cust);
		
		expect(divConverter.toModel(orgViews[1])).andReturn(div);
		expect(orgSaver.saveOrUpdate(trans, div)).andReturn(div);
			
		transManager.finishTransaction(trans);
		
		replay(transManager);
		replay(orgSaver);
		replay(custConverter);
		replay(divConverter);
		
		importer.readAndValidate();
		
		int total = importer.runImport();
		
		assertEquals(2, total);
		
		verify(transManager);
		verify(orgSaver);
		verify(custConverter);
		verify(divConverter);
	}
}
