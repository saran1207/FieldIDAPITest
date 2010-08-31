package com.n4systems.exporting;

import com.n4systems.exporting.io.MapWriter;

public interface Exporter {
	public void export(MapWriter mapWriter) throws ExportException;
}
