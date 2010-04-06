package com.n4systems.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.Local;

import com.n4systems.ejb.AggregateReportManager;
import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.ConfigManager;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.NoteManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.ejb.impl.AggregateReportManagerImpl;
import com.n4systems.ejb.impl.AutoAttributeManagerImpl;
import com.n4systems.ejb.impl.ConfigManagerImpl;
import com.n4systems.ejb.impl.InspectionManagerImpl;
import com.n4systems.ejb.impl.InspectionScheduleManagerImpl;
import com.n4systems.ejb.impl.MassUpdateManagerImpl;
import com.n4systems.ejb.impl.NoteManagerImpl;
import com.n4systems.ejb.impl.OrderManagerImpl;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.impl.ProductManagerImpl;
import com.n4systems.ejb.impl.ProjectManagerImpl;
import com.n4systems.ejb.impl.ProofTestHandlerImpl;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.ejb.legacy.Option;
import com.n4systems.ejb.legacy.OrderMapping;
import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.ejb.legacy.ProductCodeMapping;
import com.n4systems.ejb.legacy.SerialNumberCounter;
import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.ejb.legacy.UnitOfMeasureManager;
import com.n4systems.ejb.legacy.User;
import com.n4systems.ejb.legacy.impl.LegacyProductSerialManager;
import com.n4systems.ejb.legacy.impl.LegacyProductTypeManager;
import com.n4systems.ejb.legacy.impl.OptionManager;
import com.n4systems.ejb.legacy.impl.OrderMappingManager;
import com.n4systems.ejb.legacy.impl.PopulatorLogManager;
import com.n4systems.ejb.legacy.impl.ProductCodeMappingManager;
import com.n4systems.ejb.legacy.impl.SerialNumberCounterManager;
import com.n4systems.ejb.legacy.impl.ServiceDTOBeanConverterImpl;
import com.n4systems.ejb.legacy.impl.UnitOfMeasureManagerImpl;
import com.n4systems.ejb.legacy.impl.UserManager;

public class CreateClasses {

	
	
	
	public static void main(String ... args) {
		Map<Class<?>, Class<?>> classes = new HashMap<Class<?>, Class<?>>(); 
		classes.put(AggregateReportManager.class, AggregateReportManagerImpl.class);
		classes.put(AutoAttributeManager.class, AutoAttributeManagerImpl.class);
		classes.put(ConfigManager.class, ConfigManagerImpl.class);
		classes.put(InspectionManager.class, InspectionManagerImpl.class);
		classes.put(InspectionScheduleManager.class, InspectionScheduleManagerImpl.class);
		classes.put(MassUpdateManager.class, MassUpdateManagerImpl.class);
		classes.put(NoteManager.class, NoteManagerImpl.class);
		classes.put(OrderManager.class, OrderManagerImpl.class);
		classes.put(PersistenceManager.class, PersistenceManagerImpl.class);
		classes.put(ProductManager.class, ProductManagerImpl.class);
		classes.put(ProjectManager.class, ProjectManagerImpl.class);
		classes.put(ProofTestHandler.class, ProofTestHandlerImpl.class);
		classes.put(LegacyProductSerial.class, LegacyProductSerialManager.class);
		classes.put(LegacyProductType.class, LegacyProductTypeManager.class);
		classes.put(Option.class, OptionManager.class);
		classes.put(OrderMapping.class, OrderMappingManager.class);
		classes.put(PopulatorLog.class, PopulatorLogManager.class);
		classes.put(ProductCodeMapping.class, ProductCodeMappingManager.class);
		classes.put(SerialNumberCounter.class, SerialNumberCounterManager.class);
		classes.put(ServiceDTOBeanConverter.class, ServiceDTOBeanConverterImpl.class);
		classes.put(UnitOfMeasureManager.class, UnitOfMeasureManagerImpl.class);
		classes.put(User.class, UserManager.class);
		
		
		
		
		
		for (Entry<Class<?>, Class<?>> clazz : classes.entrySet()) {
			if (clazz.getKey().isInterface() && clazz.getKey().getAnnotation(Local.class) != null) {
				String classContent = new ManagerGenerator().generateWrappingClass(clazz.getKey(), clazz.getValue());
				
				File dir = new File("/tmp/" + clazz.getKey().getPackage().getName());
				if (!dir.exists()) {
					dir.mkdirs();
				}
				
				File file = new File(dir.getAbsolutePath() + "/" + clazz.getKey().getSimpleName() + "EJBContainer.java");
				
				try {
					FileWriter fileWriter = new FileWriter(file);
					fileWriter.write(classContent);
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
