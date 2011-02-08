package com.n4systems.reporting.mapbuilders;

import static com.n4systems.model.builders.JobBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.certificate.model.Job;
import com.n4systems.util.ReportMap;
import com.n4systems.util.persistence.TestingTransaction;


public class JobCertificateDataProducerTest {

	@Test
	public void should_not_create_any_map_parameters_if_the_job_is_null() throws Exception {
		JobCertificateDataProducer sut = new JobCertificateDataProducer();
		ReportMap<Object> reportMap = new ReportMap<Object>();
		sut.addParams(reportMap, null, new TestingTransaction());
		
		assertThat(reportMap.isEmpty(), is(true));
	}

	@Test
	public void should_add_a_job_map_parameters_if_the_job_is_not_null() throws Exception {
		JobCertificateDataProducer sut = new JobCertificateDataProducer();
		ReportMap<Object> reportMap = new ReportMap<Object>();
		sut.addParams(reportMap, aJob().withTitle("").withProjectID("").build(), new TestingTransaction());
		
		assertThat(reportMap, hasKey(ReportField.JOB.getParamKey()));
	}
	
	@Test
	public void should_add_a_assign_the_title_to_the_job_param_if_the_job_is_not_null() throws Exception {
		JobCertificateDataProducer sut = new JobCertificateDataProducer();
		ReportMap<Object> reportMap = new ReportMap<Object>();
		sut.addParams(reportMap, aJob().withTitle("my job title").withProjectID("").build(), new TestingTransaction());
		Job certificateJobData = (Job)reportMap.get(ReportField.JOB.getParamKey());
		assertThat(certificateJobData.getTitle(), equalTo("my job title"));
	}
	
	@Test
	public void should_add_a_assign_the_jobID_to_the_job_param_if_the_job_is_not_null() throws Exception {
		JobCertificateDataProducer sut = new JobCertificateDataProducer();
		ReportMap<Object> reportMap = new ReportMap<Object>();
		sut.addParams(reportMap, aJob().withTitle("").withProjectID("A1000").build(), new TestingTransaction());
		Job certificateJobData = (Job)reportMap.get(ReportField.JOB.getParamKey());
		assertThat(certificateJobData.getIdentifier(), equalTo("A1000"));
	}
	
}
