/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated 
 * by the @authors tag. All rights reserved. 
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors. 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
package com.n4systems.service.ordermapper.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import rfid.ejb.session.OrderMapping;

public class SendJMSMessage {
    QueueConnection conn;
    QueueSession session;
    Queue que;
    
    
    public void setupConnection() throws JMSException, NamingException
    {
        Hashtable properties1 = new Hashtable();
		properties1.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		properties1.put(Context.URL_PKG_PREFIXES,	"org.jboss.naming:org.jnp.interfaces");
		//properties1.put(Context.PROVIDER_URL, "jnp://66.11.155.202:1099");
		properties1.put(Context.PROVIDER_URL, "66.11.155.202");
		InitialContext iniCtx = new InitialContext(properties1);

    	Object tmp = iniCtx.lookup("ConnectionFactory");
    	QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
    	conn = qcf.createQueueConnection();
    	que = (Queue) iniCtx.lookup("queue/fieldid_ordermapper_Request_gw");
    	session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
    	conn.start();
    	System.out.println("Connection Started");
    }
    
    public void stop() throws JMSException 
    { 
        conn.stop();
        session.close();
        conn.close();
    }
    
    public void sendAMessage(String msg) throws JMSException {
    	
        QueueSender send = session.createSender(que);  

        HashMap<String,Object> mainHashMap = new HashMap<String, Object>();
        /*
        mainHashMap.put(OrderMapping.ORGANIZATION_ID, "jboss");
        mainHashMap.put(OrderMapping.EXTERNAL_SOURCE_ID, "test");
        */
        
        ArrayList<HashMap<String, Object>> orders = new ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> orderHashMap = new HashMap<String, Object>();
        /*
        orderHashMap.put(OrderMapping.OrderFieldName.ORDER_NUMBER.key(), "TEST123456");
        //orderHashMap.put(OrderMapping.OrderFieldName.ITEM_GROUP.key(), "Item Group");
        //orderHashMap.put(OrderMapping.OrderFieldName.LINE_NUMBER.key(), "Line Number");
        orderHashMap.put(OrderMapping.OrderFieldName.SALES_AGENT.key(), "Sales Agent");
		orderHashMap.put(OrderMapping.OrderFieldName.ADDRESS_TITLE.key(), "Address Title");
		orderHashMap.put(OrderMapping.OrderFieldName.DEPARTMENT.key(),"Department");
		orderHashMap.put(OrderMapping.OrderFieldName.STREET_NUMBER.key(), "Street Number");
		orderHashMap.put(OrderMapping.OrderFieldName.STREET_NAME.key(), "Street Name");
        orderHashMap.put(OrderMapping.OrderFieldName.CITY.key(), "City");
        orderHashMap.put(OrderMapping.OrderFieldName.STATE.key(), "State");
        orderHashMap.put(OrderMapping.OrderFieldName.ZIP_CODE.key(), "Zip Code");
        orderHashMap.put(OrderMapping.ENDUSER_NAME, "JBOSS Manufacturing");
        //orderHashMap.put(OrderMapping.CUSTOMER_ID, "Customer ID");
        orderHashMap.put(OrderMapping.DIVISION_NAME, "Division");
        orderHashMap.put(OrderMapping.ORDER_DATE, "20080102");
		*/
        
        // Line Items
        ArrayList<HashMap<String, Object>> lineItems = new ArrayList<HashMap<String,Object>>();
        
        HashMap<String, Object> lineItemOneMap = new HashMap<String, Object>();
        //lineItemOneMap.put(OrderMapping.LineItemFieldName.ITEM_NUMBER.key(), "RCC");
        /*
        lineItemOneMap.put(OrderMapping.LineItemFieldName.PRODUCT_NAME.key(), "Rcc Thing");
        lineItemOneMap.put(OrderMapping.LineItemFieldName.JOB_NUMBER.key(), "Job Number");
        lineItemOneMap.put(OrderMapping.LineItemFieldName.PO_NUMBER.key(), "PO Number");
        lineItemOneMap.put(OrderMapping.LineItemFieldName.PART_NUMBER.key(), "Part Number");
        lineItemOneMap.put(OrderMapping.LineItemFieldName.PRODUCT_TYPE.key(), "Product Type");
        lineItemOneMap.put(OrderMapping.LineItemFieldName.CUSTOMER_PART_NUMBER.key(), "Customer Part Number");
        lineItemOneMap.put(OrderMapping.LineItemFieldName.RELEASE_NUMBER.key(), "Release Number");
        lineItemOneMap.put(OrderMapping.QUANTITY, "5");
        lineItems.add(lineItemOneMap);
        */
        
        HashMap<String, Object> lineItemTwoMap = new HashMap<String, Object>();
        /*
        //lineItemTwoMap.put(OrderMapping.LineItemFieldName.ITEM_NUMBER.key(), "SCC");
        lineItemTwoMap.put(OrderMapping.LineItemFieldName.PRODUCT_NAME.key(), "Scc Thing");
        lineItemTwoMap.put(OrderMapping.LineItemFieldName.JOB_NUMBER.key(), "Job Number");
        lineItemTwoMap.put(OrderMapping.LineItemFieldName.PO_NUMBER.key(), "PO Number");
        lineItemTwoMap.put(OrderMapping.LineItemFieldName.PART_NUMBER.key(), "Part Number");
        lineItemTwoMap.put(OrderMapping.LineItemFieldName.PRODUCT_TYPE.key(), "Product Type");
        lineItemTwoMap.put(OrderMapping.LineItemFieldName.CUSTOMER_PART_NUMBER.key(), "Customer Part Number");
        lineItemTwoMap.put(OrderMapping.LineItemFieldName.RELEASE_NUMBER.key(), "Release Number");
        lineItemTwoMap.put(OrderMapping.QUANTITY, "2");
        lineItems.add(lineItemTwoMap);
        
        orderHashMap.put(OrderMapping.LINE_ITEMS, lineItems);
        */
        
        orders.add(orderHashMap);
        mainHashMap.put("orders", orders);
                        
        ObjectMessage tm = session.createObjectMessage(mainHashMap);
        send.send(tm);        
        send.close();
    }
       
    
    public static void main(String args[]) throws Exception
    {        	    	
    	SendJMSMessage sm = new SendJMSMessage();
    	sm.setupConnection();
    	sm.sendAMessage(args[0]); 
    	sm.stop();
    	
    }
    
}
