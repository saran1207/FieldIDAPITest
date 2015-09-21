package com.n4systems.fieldid.ws.v2.resources.customerdata.eventattachment;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v2.resources.ApiKeyString;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ThingEvent;
import com.n4systems.util.persistence.JoinClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("eventAttachment")
public class ApiEventAttachmentResource extends ApiResource<ApiEventAttachment, FileAttachment> {
	private static Logger logger = Logger.getLogger(ApiEventAttachmentResource.class);
    @Autowired
    protected S3Service s3Service;

	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> query(@QueryParam("id") List<ApiKeyString> attachmentIds) {
		if (attachmentIds.isEmpty()) return new ArrayList<>();
		List<ApiModelHeader> headers = convertAllEntitiesToApiModels(findAttachmentsByMobileIds(unwrapKeys(attachmentIds)), a -> new ApiModelHeader(a.getMobileId(), a.getModified()));
		return headers;
	}

	@GET
	@Path("query/event")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> queryEvent(@QueryParam("eventIds") List<ApiKeyString> eventIds) {
		if (eventIds.isEmpty()) return new ArrayList<>();
		List<ApiModelHeader> headers = convertAllEntitiesToApiModels(findAttachmentsByEvents(unwrapKeys(eventIds)), a -> new ApiModelHeader(a.getMobileId(), a.getModified()));
		return headers;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiEventAttachment> findAll(@QueryParam("id") List<ApiKeyString> attachmentIds) {
		if (attachmentIds.isEmpty()) return new ArrayList<>();

		List<FileAttachment> attachments = findAttachmentsByMobileIds(unwrapKeys(attachmentIds));
		List<ApiEventAttachment> apiAttachments = convertAllEntitiesToApiModels(attachments, att -> {
			ApiEventAttachment apiAtt = convertEntityToApiModel(att);
			if (apiAtt == null) return null;

			apiAtt.setEventSid(findEventIdForAttachment(att));
			return apiAtt;
		});
		return apiAttachments;
	}

	private List<FileAttachment> findAttachmentsByEvents(List<String> eventIds) {
		QueryBuilder<ThingEvent> query = createUserSecurityBuilder(ThingEvent.class, true).addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "mobileGUID", eventIds));
		List<FileAttachment> attachments = persistenceService.findAll(query)
				.stream()
				.map(ThingEvent::getAttachments)
				.flatMap(List::stream)
				.collect(Collectors.toList());
		return attachments;
	}

	private List<FileAttachment> findAttachmentsByMobileIds(List<String> ids) {
		QueryBuilder<FileAttachment> query = createUserSecurityBuilder(FileAttachment.class).addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "mobileId", ids));
		List<FileAttachment> attachments = persistenceService.findAll(query);
		return attachments;
	}

	private String findEventIdForAttachment(FileAttachment attachment) {
		QueryBuilder<ThingEvent> query = createUserSecurityBuilder(ThingEvent.class, true);
		query.addJoin(new JoinClause(JoinClause.JoinType.INNER, "attachments", "att", false));
		query.addWhere(WhereClauseFactory.createPassthru("att = :attachment", attachment));
		ThingEvent event = persistenceService.find(query);
		return (event != null) ? event.getMobileGUID() : null;
	}


	@Override
	protected ApiEventAttachment convertEntityToApiModel(FileAttachment attachment) {
		try {
			ApiEventAttachment apiAttachment = new ApiEventAttachment();
			apiAttachment.setSid(attachment.getMobileId());
			apiAttachment.setActive(true);
			apiAttachment.setModified(attachment.getModified());
			apiAttachment.setComments(attachment.getComments());
			apiAttachment.setUrl(s3Service.getFileAttachmentUrl(attachment));
			apiAttachment.setMimeType(s3Service.getFileAttachmentContentType(attachment));
			return apiAttachment;
		} catch (AmazonS3Exception ex) {
			logger.warn("Unable to load event attachment information", ex);
			return null;
		}
	}
}
