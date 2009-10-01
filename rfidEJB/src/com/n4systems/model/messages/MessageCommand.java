package com.n4systems.model.messages;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;

import com.n4systems.model.parents.AbstractEntity;


@Entity 
@Table(name="messagecommands")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "commandtype", discriminatorType = DiscriminatorType.STRING)
public abstract class MessageCommand extends AbstractEntity {
	public enum Type { 
		CREATE_SAFETY_NETWORK_CONNECTION("create safety network connection");
		
		private String displayTitle;
		
		private Type(String displayTitle) {
			this.displayTitle = displayTitle;
		}
		
		public String getDisplayTitle() {
			return displayTitle;
		}
	};
	
	private boolean processed = false;
	
	
	@CollectionOfElements(fetch=FetchType.EAGER)
	private Map<String,String> paramaters = new HashMap<String, String>();


	protected Map<String, String> getParamaters() {
		return paramaters;
	}


	public boolean isProcessed() {
		return processed;
	}


	public void setProcessed(boolean processed) {
		this.processed = processed;
	}
	
}
