package com.n4systems.model.ui.releasenotes;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.n4systems.util.serialization.Serializer;


public class ReleaseNoteFileSystemRepositoryTest {

	
	private final class SerializerFake implements Serializer<ReleaseNotes> {
		ReleaseNotes releaseNote;
		
		public SerializerFake(ReleaseNotes releaseNotes) {
			this.releaseNote = releaseNotes;
		}
		
		@Override
		public ReleaseNotes deserialize(InputStream in) {
			return releaseNote;
		}

		@Override
		public void serialize(ReleaseNotes object, OutputStream out) {
		}
	}

	@Test
	public void should_return_loaded_release_note_when_the_element_found_in_the_file() throws Exception {
		
		SerializerFake serializer = new SerializerFake(new ReleaseNotes());
		File releaseNoteFile = new File(getClass().getResource("releaseNotes.xml").toURI());
		
		ReleaseNoteFileSystemRepository sut = new ReleaseNoteFileSystemRepository(releaseNoteFile, serializer);
		
		assertSame(serializer.releaseNote, sut.load());
	}
	
	@Test
	public void should_return_a_default_release_notes_when_no_release_note_file_can_be_found() throws Exception {
		SerializerFake serializer = new SerializerFake(null);
		File releaseNoteFile = new File("file_that_does_not_exist");
		
		ReleaseNoteFileSystemRepository sut = new ReleaseNoteFileSystemRepository(releaseNoteFile, serializer);
		
		ReleaseNotes expectedReleaseNotes = new ReleaseNotes();
		ReleaseNotes actualReleaseNotes = sut.load();
		
		assertEquals(expectedReleaseNotes.getTitle(), actualReleaseNotes.getTitle());
		assertEquals(expectedReleaseNotes.getUrl(), actualReleaseNotes.getUrl());
		assertEquals(expectedReleaseNotes.getBullets(), actualReleaseNotes.getBullets());
	}
	
	
	
}
