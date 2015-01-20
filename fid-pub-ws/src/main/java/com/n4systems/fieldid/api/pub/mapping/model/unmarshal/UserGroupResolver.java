package com.n4systems.fieldid.api.pub.mapping.model.unmarshal;

import com.n4systems.fieldid.api.pub.mapping.ValueConverter;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.PublicIdEncoder;
import com.n4systems.model.user.UserGroup;
import com.n4systems.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

public class UserGroupResolver extends FieldIdPersistenceService implements ValueConverter<Messages.UserGroupMessage, UserGroup> {

	@Override
	@Transactional(readOnly = true)
	public UserGroup convert(Messages.UserGroupMessage value) {
		return (StringUtils.isNotEmpty(value.getId())) ? persistenceService.findById(UserGroup.class, PublicIdEncoder.decode(value.getId())) : null;
	}
}
