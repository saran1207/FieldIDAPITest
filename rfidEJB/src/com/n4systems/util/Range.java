package com.n4systems.util;

import com.n4systems.exceptions.InvalidArgumentException;

public class Range<T extends Comparable<T>> {

	private final T beginning;
	private final T ending;
	
	
	public Range(T beginning, T ending) {
		super();
		
		if (beginning == null || ending == null) {
			throw new InvalidArgumentException("both parts of the range have to be non null");
		}
		
		if (beginning.compareTo(ending) > 0) {
			throw new InvalidArgumentException("the beginning of the range must be small than or equal to the ending");
		}
		
		
		this.beginning = beginning;
		this.ending = ending;
	}


	public T getBeginning() {
		return beginning;
	}


	public T getEnding() {
		return ending;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beginning == null) ? 0 : beginning.hashCode());
		result = prime * result + ((ending == null) ? 0 : ending.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Range<?>)) {
			return false;
		}
		
		Range<?> other = (Range<?>) obj;
		return (beginning.equals(other.beginning) && ending.equals(other.ending)) ;
	}
	
	
	
}
