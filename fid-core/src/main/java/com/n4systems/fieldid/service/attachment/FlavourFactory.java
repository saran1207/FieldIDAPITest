package com.n4systems.fieldid.service.attachment;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.model.attachment.Attachment;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class FlavourFactory implements ApplicationContextAware,BeanFactoryAware {

    private @Autowired ImageService imageService;

    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    private Map<String,Flavour> flavours;

    public Flavour createFlavour(Attachment attachment, String... flavourRequest) {
        // NOTE : all flavour beans are stateful (they have specific options per instance.)
        // .: we can't just inject them into this springleton class - we need to use a bean factory to create each time.
        for (String name:getAllFlavours().keySet()) {
            Flavour instance = flavours.get(name);
            FlavourOptions options = instance.isSupportedRequest(flavourRequest);
            if (options!=null) {
                Preconditions.checkState(beanFactory.isPrototype(name)," the flavour bean " + name + " must be PROTOTYPE scoped because flavours are stateful.");
                return ((Flavour) beanFactory.getBean(name)).forAttachment(attachment).withOptions(options);
            }
        }
        throw new IllegalArgumentException("can't find bean to support flavour request : " + flavourRequest);
    }

    private Map<String, Flavour> getAllFlavours() {
        if (flavours==null) {
            flavours = applicationContext.getBeansOfType(Flavour.class);
        }
        return flavours;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
