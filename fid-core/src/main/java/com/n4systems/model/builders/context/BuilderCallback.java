package com.n4systems.model.builders.context;

import com.n4systems.model.builders.BaseBuilder;

public interface BuilderCallback {

    public void onBeforeBuild(BaseBuilder builder);

    public void onObjectBuilt(Object o);

}
