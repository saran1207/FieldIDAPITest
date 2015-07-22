package com.n4systems.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//This enum is similar to ImageAnnotationType we should probably merge them
public enum IsolationPointSourceType {
	/*
	NOTE: id and modified (milliseconds since epoc) are used for mobile synchronization.  Please make sure to update the modified date whenever
	identifiers are changed.  Also, id's should never be changed.
	 */
    C	(0L, 	1437436800000L,	"Chemical"),
    CP	(1L, 	1437436800000L,	"Control Panel"),
    E	(2L, 	1437436800000L,	"Electrical"),
    G	(3L, 	1437436800000L,	"Gas"),
    H	(4L, 	1437436800000L,	"Hydraulic"),
    M	(5L, 	1437436800000L,	"Mechanical"),
    N	(6L, 	1437436800000L,	"Notes"),
    P	(7L, 	1437436800000L,	"Pneumatic"),
    S	(8L, 	1437436800000L,	"Steam"),
    SP	(9L,	1437436800000L,	"Stored Pressure"),
    V	(10L, 	1437436800000L,	"Valve"),
    W	(11L, 	1437436800000L,	"Water");

    private final long id;
	private final long modified;
    private final String identifier;

    IsolationPointSourceType(Long id, long modified, String identifier) {
        this.id = id;
		this.modified = modified;
        this.identifier = identifier;
    }

    public long getId() {
        return id;
    }

	public Date getModified() {
		return new Date(modified);
	}

	public String getIdentifier() {
        return identifier;
    }

    public static IsolationPointSourceType getDefault() {
        return W;
    }

	public static Stream<IsolationPointSourceType> stream() {
		return Arrays.stream(values());
	}

	public static IsolationPointSourceType forId(Long id) {
		return stream().filter(p -> p.id == id).findFirst().orElse(null);
	}

	public static List<IsolationPointSourceType> modifiedAfter(Date modified) {
		if (modified == null) return Arrays.asList(values());

		long modifiedMillis = modified.getTime();
		return stream().filter(p -> p.modified > modifiedMillis).collect(Collectors.toList());
	}
}
