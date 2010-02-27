package com.n4systems.model.utils;

import java.io.Closeable;
import java.io.IOException;

public class StreamUtils {

	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {}
		}
	}

}
