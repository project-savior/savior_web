package com.jerry.savior_web.utils;

import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author 22454
 */
@Component
public class SpringContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @SneakyThrows
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        SpringContextHelper.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SneakyThrows
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    @SneakyThrows
    public static <T> T getBean(String beanName, Class<T> cls) {
        return applicationContext.getBean(beanName, cls);
    }

    @SneakyThrows
    public static <T> T getBean(Class<T> cls) {
        return applicationContext.getBean(cls);
    }

}
