package com.n4systems.fieldid.api.pub.config;

import com.n4systems.fieldid.api.pub.filters.CatchAllExceptionMapper;
import com.n4systems.fieldid.api.pub.filters.OAuthFilter;
import com.n4systems.fieldid.api.pub.mapping.model.unmarshal.*;
import com.n4systems.fieldid.api.pub.protocolbuffers.google.GoogleProtobufMessageBodyReader;
import com.n4systems.fieldid.api.pub.protocolbuffers.google.GoogleProtobufMessageBodyWriter;
import com.n4systems.fieldid.api.pub.resources.AssetResource;
import com.n4systems.fieldid.api.pub.resources.AssetTypeResource;
import com.n4systems.fieldid.api.pub.resources.OwnerResource;
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
	public OAuthFilter oAuthFilter() {
		return new OAuthFilter();
	}

    @Bean
    public GoogleProtobufMessageBodyReader googleProtobufMessageBodyReader() {
		return new GoogleProtobufMessageBodyReader();
	}

    @Bean
    public GoogleProtobufMessageBodyWriter googleProtobufMessageBodyWriter() {
		return new GoogleProtobufMessageBodyWriter();
	}

    @Bean
	public CatchAllExceptionMapper catchAllExceptionMapper() {
		return new CatchAllExceptionMapper();
	}

	@Bean
	public BaseOrgResolver baseOrgResolver() {
		return new BaseOrgResolver();
	}

	@Bean
	public UserResolver userResolver() {
		return new UserResolver();
	}

	@Bean
	public AssetTypeResolver assetTypeResolver() {
		return new AssetTypeResolver();
	}

	@Bean
	public AssetStatusResolver assetStatusResolver() {
		return new AssetStatusResolver();
	}

	@Bean
	public PredefinedLocationResolver predefinedLocationResolver() {
		return new PredefinedLocationResolver();
	}

	@Bean
	public MessageToAssetAttributes messageToAssetAttributes() {
		return new MessageToAssetAttributes();
	}

	@Bean
	public TestResource testResource() {
		return new TestResource();
	}

	@Bean(initMethod = "init")
	public OwnerResource ownerResource() {
		return new OwnerResource();
	}

	@Bean(initMethod = "init")
	public AssetResource assetResource() {
		return new AssetResource();
	}

	@Bean(initMethod = "init")
	public AssetTypeResource assetTypeResource() {
		return new AssetTypeResource();
	}

}
