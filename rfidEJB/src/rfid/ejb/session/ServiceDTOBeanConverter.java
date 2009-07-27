package rfid.ejb.session;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.entity.UserBean;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.JobSite;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.StateSet;
import com.n4systems.model.TenantOrganization;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.webservice.dto.CustomerServiceDTO;
import com.n4systems.webservice.dto.DivisionServiceDTO;
import com.n4systems.webservice.dto.InspectionBookServiceDTO;
import com.n4systems.webservice.dto.InspectionTypeServiceDTO;
import com.n4systems.webservice.dto.JobServiceDTO;
import com.n4systems.webservice.dto.JobSiteServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.ProductTypeGroupServiceDTO;
import com.n4systems.webservice.dto.ProductTypeServiceDTO;
import com.n4systems.webservice.dto.SetupDataLastModDatesServiceDTO;
import com.n4systems.webservice.dto.StateSetServiceDTO;
import com.n4systems.webservice.dto.TenantServiceDTO;

import fieldid.web.services.dto.AutoAttributeCriteriaServiceDTO;
import fieldid.web.services.dto.EndUserServiceDTO;
import fieldid.web.services.dto.InfoOptionServiceDTO;
import fieldid.web.services.dto.ProductSerialServiceDTO;
import fieldid.web.services.dto.ProductStatusServiceDTO;
import fieldid.web.services.dto.UserServiceDTO;

@Local
public interface ServiceDTOBeanConverter {

	public com.n4systems.webservice.dto.InspectionServiceDTO convert(Inspection inspection);
	public List<com.n4systems.webservice.dto.InspectionServiceDTO> convert(InspectionGroup inspectionGroup);
	public ProductServiceDTO convert( ProductSerialServiceDTO productSerialServiceDTO );
	public Product convert( ProductServiceDTO productServiceDTO, Product targetProduct, long tenantId );
	public ProductServiceDTO convert(Product product);
	public InfoOptionBean convert( InfoOptionServiceDTO infoOptionServiceDTO );
	public InspectionTypeServiceDTO convert( InspectionType inspectionType );
	public StateSetServiceDTO convert( StateSet stateSet );
	
	public Inspection convert( com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO, Long tenantId ) throws IOException;
	public ProductStatusBean convertProductStatus( com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO );
	public Date convertNextDate( com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO );
	public InspectionSchedule convertInspectionSchedule(com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO);
		
	public ProductSerialServiceDTO convert( Product productSerial, boolean moreData );
	public com.n4systems.webservice.dto.AutoAttributeCriteriaServiceDTO convert( AutoAttributeCriteria criteria );
	public com.n4systems.webservice.dto.AutoAttributeDefinitionServiceDTO convert( AutoAttributeDefinition definition );
	public AutoAttributeCriteriaServiceDTO convert_old( AutoAttributeCriteria criteriaIn );
	public ProductStatusServiceDTO convert( ProductStatusBean productStatus );
	public ProductTypeServiceDTO convert_new( ProductType productType );
	public TenantServiceDTO convert(TenantOrganization tenant);
	public com.n4systems.webservice.dto.UserServiceDTO convert(UserBean user);
	public UserBean convert(com.n4systems.webservice.dto.UserServiceDTO user);
	public UserServiceDTO convert_old( UserBean user );
	public InspectionBookServiceDTO convert(InspectionBook inspectionBook);
	public JobSiteServiceDTO convert(JobSite jobSite);
	@Deprecated
	public EndUserServiceDTO convertLegacy(Customer customer);
	@Deprecated
	public fieldid.web.services.dto.DivisionServiceDTO convertLegacy(Division customer);
	public CustomerServiceDTO convert(Customer customer, List<Division> divisions);
	
	public DivisionServiceDTO convert(Division division);
	public ProductTypeGroupServiceDTO convert(ProductTypeGroup productTypeGroup);
	public Date convertStringToDate(String stringDate);
	public JobServiceDTO convert(Project job);
	public SetupDataLastModDatesServiceDTO convert(SetupDataLastModDates setupModDates);
}
