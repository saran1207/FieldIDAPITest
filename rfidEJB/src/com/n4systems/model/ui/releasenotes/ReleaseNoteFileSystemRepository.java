package com.n4systems.model.ui.releasenotes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.util.serialization.Serializer;
import com.n4systems.util.serialization.XStreamSerializer;

public class ReleaseNoteFileSystemRepository implements ReleaseNoteRepository {
	private final File releaseNoteFile;
	private final Serializer<ReleaseNotes> serializer;


	protected static Serializer<ReleaseNotes> defaultSerializer() {
		return new XStreamSerializer<ReleaseNotes>(ReleaseNotes.class);
	}
	
	public ReleaseNoteFileSystemRepository(File releaseNoteFile, Serializer<ReleaseNotes> serializer) {
		super();
		this.releaseNoteFile = releaseNoteFile;
		this.serializer = serializer;
	}
	
	public ReleaseNoteFileSystemRepository(File releaseNoteFile) {
		this(releaseNoteFile, defaultSerializer());
	}
	
	


	public ReleaseNotes load() {
		
		
		ReleaseNotes releaseNote;
		try {
			releaseNote = serializer.deserialize(new FileInputStream(releaseNoteFile));
		} catch (FileNotFoundException e) {
			releaseNote = new ReleaseNotes();
		}
		
		return releaseNote;
	}


	
	
	
	public void save(ReleaseNotes releaseNotes) {
		try {
			serializer.serialize(releaseNotes, new FileOutputStream(releaseNoteFile));
		} catch (FileNotFoundException e) {
			throw new ProcessFailureException("file to save to could not be found", e);
		}
	}
}
