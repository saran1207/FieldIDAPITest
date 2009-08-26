package com.n4systems.handlers.creator.signup;

import com.n4systems.handlers.creator.ReversableCreateHandler;
import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;

public interface AccountPlaceHolderCreateHandler extends ReversableCreateHandler<AccountPlaceHolder> {

	public AccountPlaceHolderCreateHandler forAccountInfo(AccountCreationInformation accountInfo);
	
}
