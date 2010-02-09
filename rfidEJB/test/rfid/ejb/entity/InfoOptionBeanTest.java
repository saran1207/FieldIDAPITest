package rfid.ejb.entity;

import static org.junit.Assert.*;

import org.junit.Test;

public class InfoOptionBeanTest {

	@Test
	public void should_create_blank_info_option_with_a_blank_name_and_0_id() {
		InfoFieldBean someInfoField = new InfoFieldBean();
		InfoOptionBean blankInfoOption = InfoOptionBean.createBlankInfoOption(someInfoField);
		
		assertEquals(0L, blankInfoOption.getUniqueID().longValue());
		assertEquals(someInfoField, blankInfoOption.getInfoField());
		assertEquals("", blankInfoOption.getName());
	}

}
