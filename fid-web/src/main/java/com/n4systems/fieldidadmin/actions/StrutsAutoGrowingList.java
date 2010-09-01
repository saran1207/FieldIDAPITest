package com.n4systems.fieldidadmin.actions;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class StrutsAutoGrowingList<E> implements List<E> {

	private List<E> delegate;
	private ObjectFactor<E> objectFactory;

	public StrutsAutoGrowingList(List<E> delegate, ObjectFactor<E> objectFactory) {
		super();
		this.delegate = delegate;
		this.objectFactory = objectFactory;
	}

	public boolean add(E e) {
		return delegate.add(e);
	}

	public void add(int index, E element) {
		delegate.add(index, element);
	}

	public boolean addAll(Collection<? extends E> c) {
		return delegate.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return delegate.addAll(index, c);
	}

	public void clear() {
		delegate.clear();
	}

	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	public E get(int index) {
		while (index >= delegate.size())
			delegate.add(objectFactory.create());

		
		return delegate.get(index);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public int indexOf(Object o) {
		return delegate.indexOf(o);
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public Iterator<E> iterator() {
		return delegate.iterator();
	}

	public int lastIndexOf(Object o) {
		return delegate.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return delegate.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return delegate.listIterator(index);
	}

	public E remove(int index) {
		return delegate.remove(index);
	}

	public boolean remove(Object o) {
		return delegate.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}

	public E set(int index, E element) {
		return delegate.set(index, element);
	}

	public int size() {
		return delegate.size();
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return delegate.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return delegate.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}
	
	
	

}
