package com.n4systems.model.ui.releasenotes;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.junit.Test;

import com.n4systems.util.serialization.XStreamSerializer;


public class XstreamingReleaseNotesTest {

	@Test
	public void should_successfully_serialize_and_deserialize_back() throws Exception {
		XStreamSerializer<ReleaseNotes> xStreamSerializer = new XStreamSerializer<ReleaseNotes>(ReleaseNotes.class);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		ReleaseNotes releaseNotes = new ReleaseNotes();
		releaseNotes.setTitle("title");
		releaseNotes.setUrl("url");
		releaseNotes.getBullets().add(new BulletPoint("bullet one"));
		releaseNotes.getBullets().add(new BulletPoint("bullet two"));
		xStreamSerializer.serialize(releaseNotes, outputStream);
		
		outputStream.toString();
		
		
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		ReleaseNotes convertedNotes = xStreamSerializer.deserialize(inputStream);
		
		assertReleaseNotesEqual(releaseNotes, convertedNotes);
	}
	
	

	private void assertReleaseNotesEqual(ReleaseNotes expected, ReleaseNotes actual) {
		assertEquals(expected.getTitle(), actual.getTitle());
		assertEquals(expected.getUrl(), actual.getUrl());
		assertEquals((List<BulletPoint>)expected.getBullets(), (List<BulletPoint>)actual.getBullets());
	}

}
