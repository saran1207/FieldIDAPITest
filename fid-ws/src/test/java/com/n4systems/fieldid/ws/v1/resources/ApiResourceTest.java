package com.n4systems.fieldid.ws.v1.resources;

import com.n4systems.model.parents.AbstractEntity;
import org.junit.Test;

import java.util.stream.Stream;

import static junit.framework.Assert.*;

public class ApiResourceTest {

	private class TestApiResource extends ApiResource<Object, AbstractEntity> {
		private String versionString;

		private TestApiResource(String versionString) {
			this.versionString = versionString;
		}

		@Override
		protected String getVersionString() {
			return versionString;
		}

		@Override
		protected Object convertEntityToApiModel(AbstractEntity entityModel) {
			return null;
		}
	}


	@Test
	public void getVersionNumber_returns_0_on_invalid_version() throws Exception {
		Stream
				.of(null, "", "1", "x.y.z", "x.y", "1.x", "1.2.x", " 1.2.3", "1.2.3 ")
				.forEach(ver -> assertEquals(0, new TestApiResource(ver).getVersionNumber()));
	}

	@Test
	public void getVersionNumber_parses_valid_3_part_version() throws Exception {
		//old style
		assertEquals(10802, new TestApiResource("1.8.2").getVersionNumber());
		//new style
		assertEquals(20150201, new TestApiResource("2015.2.1").getVersionNumber());
	}

	@Test
	public void getVersionNumber_parses_valid_2_part_version() throws Exception {
		//old style
		assertEquals(10800, new TestApiResource("1.8").getVersionNumber());
		//new style
		assertEquals(20150200, new TestApiResource("2015.2").getVersionNumber());
	}

	@Test
	public void versionLessThan_compares_valid_versions() throws Exception {
		//old style
		assertTrue(new TestApiResource("1.8.0").versionLessThan(1, 8, 1));
		assertFalse(new TestApiResource("1.8").versionLessThan(1, 8, 0));
		//new style
		assertTrue(new TestApiResource("2015.2.0").versionLessThan(2015, 2, 1));
		assertFalse(new TestApiResource("2015.2.0").versionLessThan(2015, 2, 0));
		// WEB-5860
		assertFalse(new TestApiResource("2015.5").versionLessThan(1, 8, 0));

	}
}