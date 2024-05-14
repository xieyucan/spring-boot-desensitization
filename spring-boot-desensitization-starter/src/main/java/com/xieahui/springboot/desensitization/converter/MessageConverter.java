package com.xieahui.springboot.desensitization.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.xieahui.springboot.desensitization.config.DesensitizationConfig;
import com.xieahui.springboot.desensitization.config.SpringBeanUtils;
import com.xieahui.springboot.desensitization.model.DesensitizationModel;
import com.xieahui.springboot.desensitization.model.DesensitizationModelFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @description: logback 脱敏转换器
 * @author: hui.xie
 * @email: xiehui1956@gmail.com
 * @date: 2024/5/11 10:01
 */
@Slf4j
@Component
@AllArgsConstructor
public class MessageConverter extends ClassicConverter {

    private DesensitizationConfig desensitizationConfig;

    private DesensitizationModelFactory desensitizationModelFactory;

    public MessageConverter() {
        log.info("MessageConverter init");
    }

    /**
     * 初始化
     *
     * @return 是否初始化成功 true:成功 false:失败
     */
    public boolean initInstance() {
        if (null == desensitizationConfig) {
            desensitizationConfig = (DesensitizationConfig) SpringBeanUtils.getBean("desensitizationConfig");
        }

        if (null == desensitizationModelFactory) {
            desensitizationModelFactory = (DesensitizationModelFactory) SpringBeanUtils.getBean("desensitizationModelFactory");
        }

        return null != desensitizationConfig || null != desensitizationModelFactory;
    }

    @Override
    public String convert(ILoggingEvent event) {

        // 获取原始的日志消息
        String message = event.getFormattedMessage();

        if (StringUtils.isBlank(message)) {
            return message;
        }

        // 初始化, 容器未初始化之前有可能获取不到对应bean实例
        if (!initInstance()) {
            return message;
        }

        // 检测开关和长度
        if (null != desensitizationConfig
                && Boolean.TRUE.equals(desensitizationConfig.getOpen())
                && message.length() <= desensitizationConfig.getLogSize()) {

            return messageConverter(desensitizationConfig, message);
        }

        return message;
    }

    /**
     * 执行message转换
     * keys为空,检查所有
     *
     * @param desensitizationConfig 配置中心配置
     * @param message               待脱敏日志
     * @return 脱敏日志
     */
    public String messageConverter(DesensitizationConfig desensitizationConfig, String message) {

        //银行卡
        Set<String> bankKeys = desensitizationConfig.getBankKeys();
        if (!ObjectUtils.isEmpty(bankKeys) && includeKey(bankKeys, message)) {
            DesensitizationModel desensitizationModel = desensitizationModelFactory.getDesensitizationModel(DesensitizationModel.BANK);
            message = desensitizationModel.desensitize(message);
        }

        //身份证
        Set<String> idNumberKeys = desensitizationConfig.getIdNumberKeys();
        if (!ObjectUtils.isEmpty(idNumberKeys) && includeKey(idNumberKeys, message)) {
            DesensitizationModel desensitizationModel = desensitizationModelFactory.getDesensitizationModel(DesensitizationModel.ID_NUMBER);
            message = desensitizationModel.desensitize(message);
        }

        //护照
        Set<String> passportKeys = desensitizationConfig.getPassportKeys();
        if (!ObjectUtils.isEmpty(passportKeys) && includeKey(passportKeys, message)) {
            DesensitizationModel desensitizationModel = desensitizationModelFactory.getDesensitizationModel(DesensitizationModel.PASSPORT);
            message = desensitizationModel.desensitize(message);
        }

        //手机号
        Set<String> phoneNumberKeys = desensitizationConfig.getPhoneNumberKeys();
        if (!ObjectUtils.isEmpty(phoneNumberKeys) && includeKey(phoneNumberKeys, message)) {
            DesensitizationModel desensitizationModel = desensitizationModelFactory.getDesensitizationModel(DesensitizationModel.PHONE_NUMBER);
            message = desensitizationModel.desensitize(message);
        }

        //自定义Model
        Map<String, String> keysModelsMappings = desensitizationConfig.getKeysModelsMappings();
        if (!ObjectUtils.isEmpty(keysModelsMappings)) {
            for (Map.Entry<String, String> keysModelEntry : keysModelsMappings.entrySet()) {
                Set<String> keys = Arrays.stream(keysModelEntry.getKey().split(",")).collect(Collectors.toSet());
                if (!ObjectUtils.isEmpty(keys) && includeKey(keys, message)) {
                    DesensitizationModel desensitizationModel = desensitizationModelFactory.getDesensitizationModel(keysModelEntry.getValue());
                    message = desensitizationModel.desensitize(message);
                }
            }
        }

        // 自定义过滤器
        if (!ObjectUtils.isEmpty(desensitizationConfig.getParams())) {
            message = desensitize(message, desensitizationConfig);
        }

        return message;
    }

    /**
     * 检测日志中是否包含指定关键字
     *
     * @param keys    关键字
     * @param message 待检测日志
     * @return 是否包含关键字
     */
    public boolean includeKey(Set<String> keys, String message) {
        for (String key : keys) {
            if (StringUtils.containsIgnoreCase(message, key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解析自定义规则
     *
     * @param input                 待脱敏字符串
     * @param desensitizationConfig 脱敏配置
     * @return 脱敏后字符串
     */
    public static String desensitize(String input, DesensitizationConfig desensitizationConfig) {
        Map<String, Pattern> compiledParams = desensitizationConfig.getCompiledParams();
        for (Map.Entry<String, Pattern> entry : compiledParams.entrySet()) {
            input = desensitize(input, entry.getValue(), desensitizationConfig.getParams().get(entry.getKey()));
        }

        return input;
    }

    private static String desensitize(String input, Pattern pattern, String replacement) {
        return pattern.matcher(input).replaceAll(replacement);
    }
}
