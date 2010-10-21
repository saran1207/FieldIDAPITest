package com.n4systems.model.product;

import static com.n4systems.model.builders.AssetBuilder.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.testutils.DummyEntityManager;

public class ChildProductLoaderTest {

	@SuppressWarnings("serial")
	public class Tree<T> extends ArrayList<Tree<T>> {
		private T value;
		
		public Tree(T value) {
			super();
			this.value = value;
		}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}
		
		private int childSize() {
			int size = size();
			for (Tree<T> sub: this) {
				size += sub.childSize();
			}
			return size;
		}
		
		public int totalSize() {
			return childSize() + 1;
		}
		
		public Tree<T> find(T v) {
			if (value.equals(v)) {
				return this;
			}
			
			Tree<T> s = null;
			for (Tree<T> sub: this) {
				s = sub.find(v);
				if (s != null) {
					break;
				}
			}
			
			return s;
		}
		
		public List<T> values() {
			List<T> values = new ArrayList<T>(size());
			for (Tree<T> v: this) {
				values.add(v.getValue());
			}
			return values;
		}
	}
	
	private Tree<Asset> tree;
	
	@Before
	public void createProductTree() {
		tree = new Tree<Asset>(anAsset().build());
		
		tree.add(new Tree<Asset>(anAsset().withSerialNumber("0").build()));
		tree.add(new Tree<Asset>(anAsset().withSerialNumber("1").build()));
		tree.add(new Tree<Asset>(anAsset().withSerialNumber("2").build()));
		
		tree.get(0).add(new Tree<Asset>(anAsset().withSerialNumber("3").build()));
		tree.get(0).add(new Tree<Asset>(anAsset().withSerialNumber("4").build()));
		tree.get(0).add(new Tree<Asset>(anAsset().withSerialNumber("5").build()));
		
		tree.get(1).add(new Tree<Asset>(anAsset().withSerialNumber("6").build()));
		
		tree.get(1).get(0).add(new Tree<Asset>(anAsset().withSerialNumber("7").build()));
		
		tree.get(2).add(new Tree<Asset>(anAsset().withSerialNumber("8").build()));
		tree.get(2).add(new Tree<Asset>(anAsset().withSerialNumber("9").build()));
	}
	
	@Test
	public void load_child_tree_loads_all_children() {
		LinkedChildProductLoader childProdLoader = new LinkedChildProductLoader() {
			protected List<Asset> load(EntityManager em) {
				Tree<Asset> assets = tree.find(asset);
				return assets.values();
			}
		};
		
		RecursiveLinkedChildProductLoader loader = new RecursiveLinkedChildProductLoader(childProdLoader);
		
		List<Asset> assets = loader.setProduct(tree.getValue()).load(new DummyEntityManager());
		
		for (int i = 0; i < 10; i++) {
			assertEquals(String.valueOf(i), assets.get(i).getSerialNumber());
		}
	}
	
	@Test(expected=SecurityException.class)
	public void load_throws_exception_on_null_product() {
		new RecursiveLinkedChildProductLoader().load((EntityManager)null);
	}
	
}
