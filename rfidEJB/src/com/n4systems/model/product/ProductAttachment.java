package com.n4systems.model.product;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.Attachment;
import com.n4systems.model.Product;
import com.n4systems.model.api.Note;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.SecurityDefiner;

@Entity
@Table(name = "productattachments")
public class ProductAttachment extends EntityWithTenant implements Saveable, Attachment {
	private static final long serialVersionUID = 1L;

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(SecurityDefiner.DEFAULT_TENANT_PATH, "product." + SecurityDefiner.DEFAULT_OWNER_PATH, null, null);
	}
	
	@ManyToOne
	private Product product;
	private Note note = new Note();
	
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
		return note.getComments();
	}

	public String getFileName() {
		return note.getFileName();
	}

	public void setComments(String comment) {
		note.setComments(comment);
	}


	public void setFileName(String fileName) {
		note.setFileName(fileName);
	}
	

	public boolean isImage() {
		return note.isImage();
	}

	@Override
	public boolean hasAttachedFile() {
		return note.hasAttachedFile();
	}

	
}
