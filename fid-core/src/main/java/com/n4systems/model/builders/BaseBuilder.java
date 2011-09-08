package com.n4systems.model.builders;

import com.n4systems.model.builders.context.BuilderCallback;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class BaseBuilder<K> implements Builder<K> {

    protected BuilderCallback builderCallback;

    private boolean alwaysUseNullId;
    private Long id;
	
	public BaseBuilder() {
		this(null);
	}

	public BaseBuilder(Long id) {
		this.id = (id == null) ? generateNewId() : id;
	}
	
	public K build() {
        if (builderCallback != null) {
            builderCallback.onBeforeBuild(this);
        }

        K builtObject = createObject();

        if (builderCallback != null) {
            builderCallback.onObjectBuilt(builtObject);
        }

        return builtObject;
    }

    public abstract K createObject();
	
	protected static Long generateNewId() {
		return Math.abs(new Random().nextLong());
	}
	
	public static void injectField(Object target, String fieldName, Object dep) throws Exception {
	     Field field = target.getClass().getDeclaredField(fieldName);
	     field.setAccessible(true);
	     field.set(target, dep);       
	}

    protected <T extends BaseBuilder> T makeBuilder(T builder) {
        builder.setAlwaysUseNullId(alwaysUseNullId);
        builder.setBuilderCallback(builderCallback);
        return builder;
    }

    public BaseBuilder<K> withId(Long id) {
        this.id = id;
        return this;
    }

    protected Long getId() {
        if (alwaysUseNullId) {
            return null;
        }
        return id;
    }

    protected <T> Set<T> setOf(T... items) {
        Set<T> set = new HashSet<T>();
        for (T item : items) {
            set.add(item);
        }
        return set;
    }

    protected <T> List<T> listOf(T... items) {
        List<T> list = new ArrayList<T>();
        for (T t : items) {
            list.add(t);
        }
        return list;
    }

    public void setAlwaysUseNullId(boolean alwaysUseNullId) {
        this.alwaysUseNullId = alwaysUseNullId;
    }

    public BaseBuilder<K> setBuilderCallback(BuilderCallback builderCallback) {
        this.builderCallback = builderCallback;
        return this;
    }

}
