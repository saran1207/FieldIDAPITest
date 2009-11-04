package rfid.ejb.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.parents.legacy.LegacyBeanTenantWithCreateModifyDate;

@Entity
@Table(name = "CommentTemplate")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class CommentTempBean extends LegacyBeanTenantWithCreateModifyDate {
	private static final long serialVersionUID = 1L;
	
	private String modifiedBy;
	private String templateID;
	private String contents;
	
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
