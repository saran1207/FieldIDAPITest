package com.n4systems.util.uri;

import static com.n4systems.model.builders.AssetBuilder.*;
import static org.junit.Assert.*;

import java.net.URI;

import com.n4systems.model.Asset;
import org.junit.Test;

import com.n4systems.util.NonDataSourceBackedConfigContext;


public class ActionURLBuilderTest {

	
	
	@Test
	public void should_accept_the_action_target_to_product_the_path_portion_of_the_url() throws Exception {
		ActionURLBuilder sut = new ActionURLBuilder(URI.create("https://alex/"), new NonDataSourceBackedConfigContext());
		sut.setAction("products");
		assertEquals("https://alex/products.action", sut.build());
	}
	
	@Test
	public void should_add_unique_id_query_variable_on_when_entity_is_given() throws Exception {
		ActionURLBuilder sut = new ActionURLBuilder(URI.create("https://alex/"), new NonDataSourceBackedConfigContext());
		sut.setAction("productEdit");
		Asset asset = anAsset().build();
		sut.setEntity(asset);
	
		assertEquals("https://alex/productEdit.action?uniqueID=" + asset.getId() , sut.build());
	}
	
	@Test
	public void should_not_addunique_id_query_variable_on_when_entity_is_given_but_still_new() throws Exception {
		ActionURLBuilder sut = new ActionURLBuilder(URI.create("https://alex/"), new NonDataSourceBackedConfigContext());
		sut.setAction("productEdit");
		Asset asset = anAsset().build();
		asset.setId(null);
		sut.setEntity(asset);
		assertEquals("https://alex/productEdit.action", sut.build());
	}
	
	@Test
	public void should_just_create_a_folder_path_if_the_action_is_not_given() throws Exception {
		ActionURLBuilder sut = new ActionURLBuilder(URI.create("https://alex/"), new NonDataSourceBackedConfigContext());
		
	
		assertEquals("https://alex/", sut.build());
	}
	
	@Test
	public void should_just_create_a_folder_path_if_the_action_is_blank() throws Exception {
		ActionURLBuilder sut = new ActionURLBuilder(URI.create("https://alex/"), new NonDataSourceBackedConfigContext());
		sut.setAction("");
	
		assertEquals("https://alex/", sut.build());
	}
}
