package com.n4systems.model.builders;

import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.savers.Saver;

import java.lang.reflect.Field;
import java.util.Random;

public abstract class BaseBuilder<K> implements Builder<K> {

	protected Long id;
    protected Saver saver;
    protected Transaction trans;
	
	public BaseBuilder() {
		this(null);
	}
	
	public BaseBuilder(Long id) {
		this.id = (id == null) ? generateNewId() : id;
	}
	
	public K build() {
        K builtObject = createObject();
        if (saver != null) {
            saver.save(trans, (Saveable) builtObject);
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
        builder.setSaver(saver).setTransaction(trans);
        return builder;
    }

    public BaseBuilder<K> setSaver(Saver saver) {
        this.saver = saver;
        return this;
    }

    public BaseBuilder<K> setTransaction(Transaction trans) {
        this.trans = trans;
        return this;
    }
}
