package com.n4systems.fieldid.selenium.pages;

import com.thoughtworks.selenium.Selenium;

import java.lang.reflect.Constructor;

public class PageFactory {

    public static <T extends WebPage> T createPage(Class<T> clazz, Selenium selenium) {
        try {
            Constructor<T> constr = clazz.getConstructor(Selenium.class);
            return constr.newInstance(selenium);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
