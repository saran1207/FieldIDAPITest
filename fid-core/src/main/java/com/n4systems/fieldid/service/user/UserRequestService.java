package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.UserRequest;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;

public class UserRequestService extends FieldIdPersistenceService {

    public UserRequest getUserRequest(Long uniqueID) {
        return persistenceService.find(UserRequest.class, uniqueID);
    }

    public void acceptRequest(UserRequest userRequest) {
        UserRequest request = getUserRequest(userRequest.getId());
        userRequest.getUserAccount().setRegistered(true);
        userRequest.getUserAccount().setUserType(UserType.READONLY);

        persistenceService.merge(userRequest.getUserAccount());
        persistenceService.remove(request);
    }

    public void denyRequest(UserRequest userRequest) {
        UserRequest request = getUserRequest(userRequest.getId());
        User user = request.getUserAccount();
        persistenceService.remove(request);
        persistenceService.remove(user);
    }

}
