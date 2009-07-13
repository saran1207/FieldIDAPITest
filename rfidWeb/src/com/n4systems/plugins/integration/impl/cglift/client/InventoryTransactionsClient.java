
package com.n4systems.plugins.integration.impl.cglift.client;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.Channel;
import org.codehaus.xfire.transport.TransportManager;

public class InventoryTransactionsClient {
	
	private final static String AUTH_USERNAME = "N4CommercialWS@plex.com";
	private final static String AUTH_PASSWORD = "j4katrEm";

	private static XFireProxyFactory proxyFactory = new XFireProxyFactory();

	@SuppressWarnings("unchecked")
	private HashMap endpoints = new HashMap();

	private Service service0;

	@SuppressWarnings("unchecked")
	public InventoryTransactionsClient() {
		create0();
		Endpoint InventoryTransactionsSoapEP = service0.addEndpoint(new QName(
				"http://www.plexus-online.com/Inventory",
				"InventoryTransactionsSoap"), new QName(
				"http://www.plexus-online.com/Inventory",
				"InventoryTransactionsSoap"),
				"http://mercury.plexus-online.com/Inventory/Inventory.asmx");
		endpoints.put(new QName("http://www.plexus-online.com/Inventory",
				"InventoryTransactionsSoap"), InventoryTransactionsSoapEP);
		Endpoint InventoryTransactionsSoapLocalEndpointEP = service0
				.addEndpoint(new QName(
						"http://www.plexus-online.com/Inventory",
						"InventoryTransactionsSoapLocalEndpoint"), new QName(
						"http://www.plexus-online.com/Inventory",
						"InventoryTransactionsSoapLocalBinding"),
						"xfire.local://InventoryTransactions");
		endpoints.put(new QName("http://www.plexus-online.com/Inventory",
				"InventoryTransactionsSoapLocalEndpoint"),
				InventoryTransactionsSoapLocalEndpointEP);
	}

	public Object getEndpoint(Endpoint endpoint) {
		try {
			return proxyFactory.create((endpoint).getBinding(), (endpoint)
					.getUrl());
		} catch (MalformedURLException e) {
			throw new XFireRuntimeException("Invalid URL", e);
		}
	}

	public Object getEndpoint(QName name) {
		Endpoint endpoint = ((Endpoint) endpoints.get((name)));
		if ((endpoint) == null) {
			throw new IllegalStateException("No such endpoint!");
		}
		return getEndpoint((endpoint));
	}

	@SuppressWarnings("unchecked")
	public Collection getEndpoints() {
		return endpoints.values();
	}

	@SuppressWarnings("unchecked")
	private void create0() {
		TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance()
				.getXFire().getTransportManager());
		HashMap props = new HashMap();
		props.put("annotations.allow.interface", true);
		AnnotationServiceFactory asf = new AnnotationServiceFactory(
				new Jsr181WebAnnotations(), tm, new AegisBindingProvider(
						new JaxbTypeRegistry()));
		asf.setBindingCreationEnabled(false);
		service0 = asf
				.create(
						(com.n4systems.plugins.integration.impl.cglift.client.InventoryTransactionsSoap.class),
						props);
		service0.setProperty(Channel.USERNAME, AUTH_USERNAME);
		service0.setProperty(Channel.PASSWORD, AUTH_PASSWORD);		
		{
			@SuppressWarnings("unused")
			AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0,
					new QName("http://www.plexus-online.com/Inventory",
							"InventoryTransactionsSoap"),
					"http://schemas.xmlsoap.org/soap/http");
		}
		{
			@SuppressWarnings("unused")
			AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0,
					new QName("http://www.plexus-online.com/Inventory",
							"InventoryTransactionsSoapLocalBinding"),
					"urn:xfire:transport:local");
		}
	}

	public InventoryTransactionsSoap getInventoryTransactionsSoap() {
		return ((InventoryTransactionsSoap) (this).getEndpoint(new QName(
				"http://www.plexus-online.com/Inventory",
				"InventoryTransactionsSoap")));
	}

	public InventoryTransactionsSoap getInventoryTransactionsSoap(String url) {
		InventoryTransactionsSoap var = getInventoryTransactionsSoap();
		org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
		return var;
	}

	public InventoryTransactionsSoap getInventoryTransactionsSoapLocalEndpoint() {
		return ((InventoryTransactionsSoap) (this).getEndpoint(new QName(
				"http://www.plexus-online.com/Inventory",
				"InventoryTransactionsSoapLocalEndpoint")));
	}

	public InventoryTransactionsSoap getInventoryTransactionsSoapLocalEndpoint(
			String url) {
		InventoryTransactionsSoap var = getInventoryTransactionsSoapLocalEndpoint();
		org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
		return var;
	}

	public static void main(String[] args) {

		InventoryTransactionsClient client = new InventoryTransactionsClient();

		//create a default service endpoint
		@SuppressWarnings("unused")
		InventoryTransactionsSoap service = client
				.getInventoryTransactionsSoap();

		//TODO: Add custom client code here
		//
		//service.yourServiceOperationHere();

		System.out.println("test client completed");
		System.exit(0);
	}

}
