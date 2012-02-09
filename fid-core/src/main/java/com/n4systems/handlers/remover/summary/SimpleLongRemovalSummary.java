package com.n4systems.handlers.remover.summary;

public class SimpleLongRemovalSummary extends RemovalSummary {

	private Long elementsToRemove;

	public SimpleLongRemovalSummary() {
		this(0L);
	}
	
	public SimpleLongRemovalSummary(Long elementsToRemove) {
		this.elementsToRemove = elementsToRemove;
	}

	public Long getElementsToRemove() {
		return elementsToRemove;
	}

	public void setElementsToRemove(Long elementsToRemove) {
		this.elementsToRemove = elementsToRemove;
	}
	
	public void setElementsToRemove(Integer elementsToRemove) {
		this.elementsToRemove = new Long(elementsToRemove);
	}

}
