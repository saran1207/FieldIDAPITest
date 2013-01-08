package com.n4systems.fieldid.service;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.model.api.PlatformContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class StoreWsClientInformationAspect {

    @Around("@annotation(com.n4systems.fieldid.service.StorePlatformContext)")
    public Object storePlatformContext(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg instanceof PlatformContext) {
                ThreadLocalInteractionContext.getInstance().setCurrentPlatform(((PlatformContext) arg).getPlatform());
                ThreadLocalInteractionContext.getInstance().setCurrentPlatformType(((PlatformContext) arg).getPlatformType());
                break;
            }
        }
        return pjp.proceed();
    }

}
