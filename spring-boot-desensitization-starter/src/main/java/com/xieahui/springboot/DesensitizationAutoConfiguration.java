package com.xieahui.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description: 脱敏自动配置类
 * @author: hui.xie
 * @email: xiehui1956@gmail.com
 * @date: 2024/5/11 14:49
 */
@Slf4j
@ComponentScan("com.xieahui.springboot.desensitization")
public class DesensitizationAutoConfiguration {

    public DesensitizationAutoConfiguration() {
        log.info("DesensitizationAutoConfiguration init");
    }

}
