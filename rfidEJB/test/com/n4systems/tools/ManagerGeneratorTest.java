package com.n4systems.tools;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.ejb.legacy.User;
import com.n4systems.ejb.legacy.impl.UserManager;


public class ManagerGeneratorTest {

	
	private ManagerGenerator sut = new ManagerGenerator();

	@Test
	public void should_have_the_package_set_to_sub_package_wrapper_of_the_interface() throws Exception {
		String resultingString = sut.generateWrappingClass(User.class, UserManager.class);
	
		assertThat(resultingString, startsWith("package com.n4systems.ejb.legacy.wrapper;"));
	}
	
	@Test
	public void should_have_the_import_for_the_interface_it_is_implementing() throws Exception {
		String resultingString = sut.generateWrappingClass(User.class, UserManager.class);
	
		assertThat(resultingString, containsString("import com.n4systems.ejb.legacy.User;"));
	}
	
	@Test
	public void should_name_the_class_after_the_inteface_with_the_extention_on_it() throws Exception {
		String resultingString = sut.generateWrappingClass(User.class, UserManager.class);
	
		assertThat(resultingString, containsString("class User" + "EJBContainer "));
	}
	
	@Test
	public void should_have_the_class_implement_the_interface() throws Exception {
		String resultingString = sut.generateWrappingClass(User.class, UserManager.class);
		
		assertThat(resultingString, containsString(" implements User " ));
	}
	
	@Test
	public void should_have_the_class_extend_EJBTransactionEmulator_with_generic_of_the_interface() throws Exception {
		String resultingString = sut.generateWrappingClass(User.class, UserManager.class);
	
		assertThat(resultingString, containsString(" extends EJBTransactionEmulator<User> " ));
	}
	
	@Test
	public void should_have_the_import_for_EJBTransactionEmulator() throws Exception {
		String resultingString = sut.generateWrappingClass(User.class, UserManager.class);
	
		assertThat(resultingString, containsString("import com.n4systems.ejb.wrapper.EJBTransactionEmulator; " ));
	}
	
	
	
	@Test
	public void should_implement_the_create_manager_method_with_the_passed_in_implementation_class() throws Exception {
		String resultingString = sut.generateWrappingClass(User.class, UserManager.class);
	
		assertThat(resultingString, containsString("protected User createManager(EntityManager em) {" ));
		assertThat(resultingString, containsString("return new UserManager(em);"));
	}
	
	
	
	
}
