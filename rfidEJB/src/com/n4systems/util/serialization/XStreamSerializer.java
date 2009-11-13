package com.n4systems.util.serialization;

import java.io.InputStream;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamSerializer<T> implements Serializer<T> {
	
	public final XStream xstream;
	
	public XStreamSerializer(XStream xstream) {
		this.xstream = xstream;
		this.xstream.autodetectAnnotations(true);
	}
	
	public XStreamSerializer() {
		this(new XStream(new DomDriver()));
	}
	
	public XStreamSerializer(Class<?> ... annotatedClasses) {
		this();
		
		for (Class<?> annotatedClass: annotatedClasses) {
			xstream.processAnnotations(annotatedClass);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(InputStream in) {
		return (T)xstream.fromXML(in);
	}

	@Override
	public void serialize(T object, OutputStream out) {
		xstream.toXML(object, out);
	}

}
