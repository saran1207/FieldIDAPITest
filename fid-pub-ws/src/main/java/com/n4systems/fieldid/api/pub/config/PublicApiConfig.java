package com.n4systems.fieldid.api.pub.config;

import com.n4systems.fieldid.api.pub.ProtobufMessageBodyReader;
import com.n4systems.fieldid.api.pub.ProtobufMessageBodyWriter;
import com.n4systems.fieldid.api.pub.ProtobufParamConverterProvider;
import com.n4systems.fieldid.api.pub.filters.OAuthFilter;
import com.n4systems.fieldid.api.pub.resources.owners.OwnerResource;
import com.n4systems.fieldid.api.pub.resources.test.TestResource;
import com.n4systems.fieldid.config.FieldIdCoreConfig;
import com.n4systems.fieldid.config.FieldIdDownloadConfig;
import com.n4systems.fieldid.config.FieldIdEntityRemovalConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ FieldIdCoreConfig.class, FieldIdDownloadConfig.class, FieldIdEntityRemovalConfig.class })
public class PublicApiConfig {

	@Bean
	public TestResource testResource() {
		return new TestResource();
	}

	@Bean
	public OwnerResource ownerResource() {
		return new OwnerResource();
	}

	@Bean
	public OAuthFilter oAuthFilter() {
		return new OAuthFilter();
	}

    @Bean
    public ProtobufMessageBodyReader protobufMessageBodyReader() { return new ProtobufMessageBodyReader(); }

    @Bean
    public ProtobufMessageBodyWriter protobufMessageBodyWriter() { return new ProtobufMessageBodyWriter(); }

    @Bean
    public ProtobufParamConverterProvider protobufParamConverterProvider() { return new ProtobufParamConverterProvider(); }

	@Bean CatchAllExceptionMapper catchAllExceptionMapper() {
		return new CatchAllExceptionMapper();
	}
}
