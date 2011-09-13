package com.n4systems.fieldid.service.task;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Event;

/** EVERYTHING in this class is assumed to be a asynchronous task.  that's why this class exists. 
  if that's not what you want then refactoring out of methods required.  (or possibly just sprinkle @Async annotations on a method by method basis)
 Note : methods must Return {@link Future} or be a void if @Async
 */

@Async
public class AsyncService extends FieldIdPersistenceService {
	
	public void generateEventByTypeExport(String name, String downloadUrl, List<Event> eventsByType) {
		System.out.println("starting task in another thread");	
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("finishing task in another thread");		
	}

	
	
//	//get the quartzFactory bean
//	Scheduler scheduler = (Scheduler) ctx.getBean("scheduleFactory");
//
//	//get the task to run or it could have been injected
//	DataPollingTask dpTask = (DataPollingTask) ctx.getBean(taskName);
//
//	//this example uses a simple interval schedule, but could be done with a CRON schedule by using the correct Trigger Bean (CronTriggerBean)
//	//create job
//	jobDetail = new MethodInvokingJobDetailFactoryBean();
//	jobDetail.setTargetObject(dpTask);
//	jobDetail.setTargetMethod("run");
//	jobDetail.setName(taskName);
//	jobDetail.setConcurrent(false);
//	jobDetail.afterPropertiesSet();
//
//	//create trigger
//	SimpleTriggerBean trigger = new SimpleTriggerBean();
//	trigger.setBeanName(taskName);
//	trigger.setJobDetail((JobDetail) jobDetail.getObject());
//	trigger.setRepeatInterval(interval);
//	trigger.afterPropertiesSet();
//
//	//add to schedule
//	scheduler.scheduleJob((JobDetail) jobDetail.getObject(), trigger);	
	
	
	
	
	
	
}
