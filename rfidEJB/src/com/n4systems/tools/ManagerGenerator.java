package com.n4systems.tools;


import com.n4systems.ejb.wrapper.EJBTransactionEmulator;

public class ManagerGenerator {

	
	private StringBuffer buffer;
	
	
	public String generateWrappingClass(Class<?> iface, Class<?> realClass) {
		buffer = new StringBuffer();
		
		buffer.append(iface.getPackage() + ".wrapper; \n" );
		
		buffer.append(createImport(iface));
		buffer.append(createImport(EJBTransactionEmulator.class));
		
		
		
		
		
		buffer.append("class " + iface.getSimpleName() + "EJBContainer ");
		buffer.append("extends " + EJBTransactionEmulator.class.getSimpleName() + "<" + iface.getSimpleName() + "> ");
		buffer.append("implements " + iface.getSimpleName() + " " );
		
		buffer.append("{ \n" );
				
		buffer.append("private " + iface.getSimpleName() + "  manager;\n");
		appendCreateManager(iface, realClass);
		
		
		buffer.append("} \n");
		
		return buffer.toString();
	}


	

	private void appendCreateManager(Class<?> iface, Class<?> realClass) {
		buffer.append("protected " + iface.getSimpleName() + " createManager(EntityManager em) { \n");
		buffer.append("return new " + realClass.getSimpleName() + "(em); \n");
		buffer.append("} \n");
	}


	private String createImport(Class<?> classToImport) {
		return "import " +  classToImport.getName() + "; \n" ;
	}
}
