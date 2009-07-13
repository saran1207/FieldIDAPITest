package com.n4systems.model.product;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.Product;
import com.n4systems.model.api.Note;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;

@Entity
@Table(name = "productattachments")
public class ProductAttachment extends EntityWithTenant implements Saveable, FilteredEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Product product;
	private Note note = new Note();

	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets(TENANT_ID_FIELD);
	}
	
	public ProductAttachment() {
		super();
	}
	
	public ProductAttachment(Note note) {
		super();
		this.note = note;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public String getComments() {
		return getComment();
	}

	public String getComment() {
		return note.getComment();
	}

	public String getFileName() {
		return note.getFileName();
	}

	public void setComment(String comment) {
		note.setComment(comment);
	}

	public void setComments(String comment) {
		setComment(comment);
	}

	public void setFileName(String fileName) {
		note.setFileName(fileName);
	}
}
