package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.parents.legacy.LegacyBeanTenantWithCreateModifyDate;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;

@Entity
@Table(name = "CommentTemplate")
public class CommentTempBean extends LegacyBeanTenantWithCreateModifyDate implements FilteredEntity {
	private static final long serialVersionUID = 1L;
	
	private String modifiedBy;
	private String templateID;
	private String contents;
	
	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets(TENANT_ID_FIELD);
	}
	
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getTemplateID() {
		return templateID;
	}

	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}

}
