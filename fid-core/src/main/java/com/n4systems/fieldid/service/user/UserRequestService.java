package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.UserRequest;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class UserRequestService extends FieldIdPersistenceService {

    public UserRequest getUserRequest(Long uniqueID) {
        return persistenceService.find(UserRequest.class, uniqueID);
    }

    public void acceptRequest(UserRequest userRequest) {
        userRequest.getUserAccount().setRegistered(true);
        userRequest.getUserAccount().setUserType(UserType.READONLY);

        persistenceService.merge(userRequest.getUserAccount());
        persistenceService.remove(userRequest);
    }

    public void denyRequest(UserRequest userRequest) {
        User user = userRequest.getUserAccount();
        persistenceService.remove(userRequest);
        persistenceService.remove(user);
    }

    public List<UserRequest> getAllUserRequests() {
        QueryBuilder<UserRequest> builder = createTenantSecurityBuilder(UserRequest.class);
        builder.addOrder("created", true);
        return persistenceService.findAll(builder);
    }

}
