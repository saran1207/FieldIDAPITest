package com.n4systems.util.collections;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

// gives a higher priority to items that are unique according to given property.
// for example, if the bean is person:{firstname, lastname, phone}
// and you use UniquePrioritizer("firstname")
// for the collection {bob foo, sue q, john doe, bob apple} then the second bob will be given a lower priority.
// the cardinality of this will be quite low as values become more unique.  you may want to chain with other prioritizer.
public class UniquePrioritizer<T> extends DefaultPrioritizer<T> {

    private static final Logger logger=Logger.getLogger(UniquePrioritizer.class);
    private String property;

    public UniquePrioritizer(String property) {
        super();
        this.property = property;
    }

    @Override
    protected Object getPriortyFor(T bean) {
        try {
            return PropertyUtils.getProperty(bean, property);
        } catch (Exception e) {
            logger.error("error while trying to prioritize [" + bean.getClass().getSimpleName() + "] = " + bean );
            return LOW_PRIORITY;
        }
    }
}



//    public abstract class OrgPrioritizer extends DefaultPrioritizer<BaseOrg> {
//
//        public OrgPrioritizer() {
//            super();
//        }
//
//        @Override
//        protected Object getPriortyFor(BaseOrg org) {
//            return org.isDivision() ? 0 :
//                        org.isCustomer() ? 1 :
//                            org.isSecondary() ? 2 :
//                                org.isPrimary() ? 3 : -1;
//        }
//    }

