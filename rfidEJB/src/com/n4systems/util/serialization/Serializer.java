package com.n4systems.util.serialization;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer<T> {
	public void serialize(T object, OutputStream out);
	public T deserialize(InputStream in);
}
