package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.api.pub.mapping.ConversionContext;
import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.ApiModelWithNameToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.UserGroupToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.UserToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.unmarshal.BaseOrgResolver;
import com.n4systems.fieldid.api.pub.mapping.model.unmarshal.UserGroupResolver;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.UserMessage;
import com.n4systems.fieldid.api.pub.model.Messages.UserMessage.Builder;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import com.n4systems.security.UserType;
import com.n4systems.util.BitField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import java.util.Locale;
import java.util.stream.Collectors;

@Path("users")
@Component
public class UserResource extends CrudResource<User, UserMessage, Builder> {

	@Autowired private UserService userService;
	@Autowired private BaseOrgResolver baseOrgResolver;
	@Autowired private UserGroupResolver userGroupResolver;

	public UserResource() {
		super(Messages.users);
	}

	@Override
	protected CrudService<User> crudService() {
		return userService;
	}

	@Override
	protected User createModel(UserMessage message) {
		return new User();
	}

	@Override
	protected Builder createMessageBuilder(User model) {
		return UserMessage.newBuilder();
	}

	@Override
	protected Mapper<User, Builder> createModelToMessageBuilderMapper(TypeMapperBuilder<User, Builder> mapperBuilder) {
		return mapperBuilder
				.add(User::getPublicId, Builder::setId)
				.addDateToString(User::getCreated, Builder::setCreated)
				.addDateToString(User::getModified, Builder::setModified)
				.addModelToMessage(User::getCreatedBy, new UserToMessage<>(Builder::setCreatedByUserId, Builder::setCreatedByUserName))
				.addModelToMessage(User::getModifiedBy, new UserToMessage<>(Builder::setModifiedByUserId, Builder::setModifiedByUserName))
				.addModelToMessage(User::getOwner, new ApiModelWithNameToMessage<>(Builder::setOwnerId, Builder::setOwnerName))
				.add(User::getUserID, Builder::setUserID)
				.add(User::getFirstName, Builder::setFirstName)
				.add(User::getLastName, Builder::setLastName)
				.add(User::getEmailAddress, Builder::setEmailAddress)
				.add(User::getTimeZoneID, Builder::setTimeZoneID)
				.add(User::getPosition, Builder::setPosition)
				.add(User::getInitials, Builder::setInitials)
				.add(User::getIdentifier, Builder::setIdentifier)
				.add(User::getFailedLoginAttempts, Builder::setFailedLoginAttempts)
				.add(User::isLocked, Builder::setLocked)
				.addDateToString(User::getLockedUntil, Builder::setLockedUntil)
				.addDateToString(User::getPasswordChanged, Builder::setPasswordChanged)
				.add(User::getLanguage, Builder::setLanguage, (l) -> l.getLanguage())
				.addDateToString(User::getLastLogin, Builder::setLastLogin)
				.add(User::getUserType, Builder::setUserType, this::convertTypeToMessage)
				.add(User::getPermissions, Builder::setPermissions, this::convertPermissionsToMessage)
				.addCollection(User::getGroups, Builder::addAllGroups, new UserGroupToMessage(), Collectors.toList())
				.build();
	}

	@Override
	protected Mapper<UserMessage, User> createMessageToModelMapper(TypeMapperBuilder<UserMessage, User> mapperBuilder) {
		return mapperBuilder
				.add(UserMessage::hasOwnerId, UserMessage::getOwnerId, User::setOwner, baseOrgResolver)
				.add(UserMessage::hasUserID, UserMessage::getUserID, User::setUserID)
				.add(UserMessage::hasFirstName, UserMessage::getFirstName, User::setFirstName)
				.add(UserMessage::hasLastName, UserMessage::getLastName, User::setLastName)
				.add(UserMessage::hasEmailAddress, UserMessage::getEmailAddress, User::setEmailAddress)
				.add(UserMessage::hasTimeZoneID, UserMessage::getTimeZoneID, User::setTimeZoneID)
				.add(UserMessage::hasPosition, UserMessage::getPosition, User::setPosition)
				.add(UserMessage::hasInitials, UserMessage::getInitials, User::setInitials)
				.add(UserMessage::hasLocked, UserMessage::getLocked, User::setLocked)
				.add(UserMessage::getIdentifier, User::setIdentifier)
				.add(UserMessage::hasLanguage, UserMessage::getLanguage, User::setLanguage, (l) -> Locale.forLanguageTag(l))
				.add(UserMessage::hasPermissions, UserMessage::getPermissions, User::setPermissions, this::convertPermissionsToModel)
				.addCollection(UserMessage::getGroupsList, User::setGroups, userGroupResolver, Collectors.toSet())
				.add(UserMessage::hasUserType, UserMessage::getUserType, User::setUserType, this::convertTypeToModel) // this needs to come last as it does some permission validation
				.build();
	}

	private UserMessage.UserType convertTypeToMessage(UserType type) {
		switch (type) {
			case ADMIN:
			case FULL:
				return UserMessage.UserType.ADMIN;
			case LITE:
				return UserMessage.UserType.INSPECTION;
			case PERSON:
				return UserMessage.UserType.PERSON;
			case READONLY:
				return UserMessage.UserType.REPORTING;
			case USAGE_BASED:
				return UserMessage.UserType.USAGE_BASED;
			default:
				throw new InternalServerErrorException("Unhandled UserType: " + type.name());
		}
	}

	private UserMessage.PermissionsMessage.Builder convertPermissionsToMessage(int permissions) {
		return UserMessage.PermissionsMessage.newBuilder()
				.setCreateAssets(Permissions.hasOneOf(permissions, Permissions.TAG))
				.setManageSystemConfig(Permissions.hasOneOf(permissions, Permissions.MANAGE_SYSTEM_CONFIG))
				.setManageSystemUsers(Permissions.hasOneOf(permissions, Permissions.MANAGE_SYSTEM_USERS))
				.setManageEndUsers(Permissions.hasOneOf(permissions, Permissions.MANAGE_END_USERS))
				.setCreateEvents(Permissions.hasOneOf(permissions, Permissions.CREATE_EVENT))
				.setEditEvents(Permissions.hasOneOf(permissions, Permissions.EDIT_EVENT))
				.setManageJobs(Permissions.hasOneOf(permissions, Permissions.MANAGE_JOBS))
				.setManageSafetyNetwork(Permissions.hasOneOf(permissions, Permissions.MANAGE_SAFETY_NETWORK));
	}

	private UserType convertTypeToModel(UserMessage.UserType type, ConversionContext<UserMessage, User> context) {
		User user = context.getTo();

		// you cannot change the type of an admin user
		if (!user.isNew() && user.getUserType() == UserType.ADMIN) {
			return UserType.ADMIN;
		}

		switch (type) {
			case ADMIN:
				return UserType.FULL;
			case INSPECTION:
				return UserType.LITE;
			case USAGE_BASED:
				return UserType.USAGE_BASED;
			case PERSON:
				return UserType.PERSON;
			case REPORTING:
				return UserType.READONLY;
			default:
				throw new InternalServerErrorException("Unhandled UserMessage.UserType: " + type.name());
		}
	}

	private int convertPermissionsToModel(UserMessage.PermissionsMessage permissions) {
		BitField perms = new BitField();
		perms.set(Permissions.TAG, permissions.getCreateAssets());
		perms.set(Permissions.MANAGE_SYSTEM_CONFIG, permissions.getManageSystemConfig());
		perms.set(Permissions.MANAGE_SYSTEM_USERS, permissions.getManageSystemUsers());
		perms.set(Permissions.MANAGE_END_USERS, permissions.getManageEndUsers());
		perms.set(Permissions.CREATE_EVENT, permissions.getCreateEvents());
		perms.set(Permissions.EDIT_EVENT, permissions.getEditEvents());
		perms.set(Permissions.MANAGE_JOBS, permissions.getManageJobs());
		perms.set(Permissions.MANAGE_SAFETY_NETWORK, permissions.getManageSafetyNetwork());
		return perms.getMask();
	}
}
