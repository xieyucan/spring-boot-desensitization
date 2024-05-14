package com.xieahui.springboot.desensitization.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Bean工具类
 * @author: hui.xie
 * @email: xiehui1956@gmail.com
 * @date: 2024/5/11 15:31
 */
@Slf4j
@Configuration
public class SpringBeanUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public SpringBeanUtils() {
        log.info("SpringBeanUtils init");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        if (null != applicationContext)
            return applicationContext.getBean(beanClass);
        return null;
    }

    public static Object getBean(String beanName) {
        if (null != applicationContext)
            return applicationContext.getBean(beanName);
        return null;
    }
}
