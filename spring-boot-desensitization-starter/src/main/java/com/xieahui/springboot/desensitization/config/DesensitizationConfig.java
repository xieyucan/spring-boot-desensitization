package com.xieahui.springboot.desensitization.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xieahui.springboot.desensitization.utils.JSON;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 配置可进一步拆分为设置和规则
 * 设置：开关、长度、关键字
 * 规则：正则、模型
 *
 * @description: 脱敏配置
 * @author: hui.xie
 * @email: xiehui1956@gmail.com
 * @date: 2024/5/11 10:00
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "desensitization")
public class DesensitizationConfig {

    public DesensitizationConfig() {
        log.info("DesensitizationConfig init");
    }

    /**
     * 是否开启日志过滤
     * 默认为开启状态
     */
    private Boolean open = true;


    private Integer logSize = 1024;

    /**
     * 日志过滤正则
     * <p>
     * {"(1\\d{2})\\d*(\\d{4})":"$1****$2","(\\d{3})\\d*(\\d{4})":"$1****$2"}
     */
    private String params;

    /**
     * 过滤模型-并发锁
     */
    private final ReadWriteLock compiledLock = new ReentrantReadWriteLock();

    /**
     * 过滤模型
     */
    private Map<String, Pattern> compiledParams;

    /**
     * 关键字和定义Model-并发锁
     */
    private final ReadWriteLock keysModelsLock = new ReentrantReadWriteLock();

    /**
     * 关键和自定义Model映射
     */
    private Map<String, String> keysModelsMappings;

    /**
     * 日志关键字-银行卡号
     * 多个关键字,英文逗号分隔
     * 过滤指定关键字，不指定过滤所有
     * 既不指定也不想过滤所有，就把开关关掉就行了
     *
     * @see #getOpen()
     */
    private String bankKeys;

    /**
     * 日志关键字-身份证号
     * 多个关键字,英文逗号分隔
     * 过滤指定关键字，不指定过滤所有
     * 既不指定也不想过滤所有，就把开关关掉就行了
     *
     * @see #getOpen()
     */
    private String idNumberKeys;

    /**
     * 日志关键字-护照号
     * 多个关键字,英文逗号分隔
     * 过滤指定关键字，不指定过滤所有
     * 既不指定也不想过滤所有，就把开关关掉就行了
     *
     * @see #getOpen()
     */
    private String passportKeys;

    /**
     * 日志关键字-手机号
     * 多个关键字,英文逗号分隔
     * 过滤指定关键字，不指定过滤所有
     * 既不指定也不想过滤所有，就把开关关掉就行了
     *
     * @see #getOpen()
     */
    private String phoneNumberKeys;

    /**
     * 关键字与自定义Model映射
     * {"mobile,phone":"myPhoneNumberStarDesensitizationModel"}
     */
    private String keysModelsMapping;

    /**
     * 更新入口
     *
     * @param keysModelsMapping 关键字与自定义Model映射
     */
    public void setKeysModelsMapping(String keysModelsMapping) {
        keysModelsLock.writeLock().lock();
        try {
            this.keysModelsMapping = keysModelsMapping;
            updateKeysModelsMappings();
        } catch (Exception e) {
            log.error("Error updating keys models mappings", e);
        } finally {
            keysModelsLock.writeLock().unlock();
        }
    }

    /**
     * 获取规则配置
     *
     * @return 规则配置
     */
    public Map<String, String> getParams() {
        if (StringUtils.isNotBlank(this.params)) {
            TypeReference<Map<String, String>> typeRef = new TypeReference<>() {
            };
            return JSON.parseToObject(this.params, typeRef);
        }

        return null;
    }

    /**
     * 更新入口
     *
     * @param params 规则配置
     */
    public void setParams(String params) {
        compiledLock.writeLock().lock();
        try {
            this.params = params;
            updateCompiledParams();
        } catch (Exception e) {
            log.error("Error updating compiled params", e);
        } finally {
            compiledLock.writeLock().unlock();
        }
    }

    /**
     * 更新缓存Pattern
     */
    private void updateCompiledParams() {
        if (StringUtils.isNotBlank(this.params)) {
            TypeReference<Map<String, String>> typeRef = new TypeReference<>() {
            };
            Map<String, String> paramsMap = JSON.parseToObject(this.params, typeRef);
            compiledParams = paramsMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> Pattern.compile(e.getKey())));
        } else {
            compiledParams = null;
        }
    }

    /**
     * 获取Pattern映射
     *
     * @return Pattern映射
     */
    public Map<String, Pattern> getCompiledParams() {
        compiledLock.readLock().lock();
        try {
            if (null == compiledParams) {
                updateCompiledParams();
            }
            return compiledParams;
        } finally {
            compiledLock.readLock().unlock();
        }
    }

    /**
     * 更新缓存KeysModels
     */
    private void updateKeysModelsMappings() {
        if (StringUtils.isNotBlank(this.keysModelsMapping)) {
            TypeReference<Map<String, String>> typeRef = new TypeReference<>() {
            };
            keysModelsMappings = JSON.parseToObject(this.keysModelsMapping, typeRef);
        } else {
            keysModelsMappings = null;
        }
    }

    /**
     * 获取缓存KeysModels
     *
     * @return 缓存KeysModels
     */
    public Map<String, String> getKeysModelsMappings() {
        keysModelsLock.readLock().lock();
        try {
            if (null == keysModelsMappings) {
                updateKeysModelsMappings();
            }
            return keysModelsMappings;
        } finally {
            keysModelsLock.readLock().unlock();
        }
    }

    /**
     * 获取银行卡号关键字列表
     *
     * @return 银行卡号关键字列表
     */
    public Set<String> getBankKeys() {

        // 获取银行卡号关键字列表
        if (StringUtils.isNotBlank(this.bankKeys)) {
            return Arrays.stream(this.bankKeys.split(",")).collect(Collectors.toSet());
        }

        return null;
    }

    /**
     * 获取身份证号关键字列表
     *
     * @return 身份证号关键字列表
     */
    public Set<String> getIdNumberKeys() {

        // 获取身份证号关键字列表
        if (StringUtils.isNotBlank(this.idNumberKeys)) {
            return Arrays.stream(this.idNumberKeys.split(",")).collect(Collectors.toSet());
        }

        return null;
    }

    /**
     * 获取护照号码关键字列表
     *
     * @return 护照号码关键字列表
     */
    public Set<String> getPassportKeys() {

        // 获取护照号码关键字列表
        if (StringUtils.isNotBlank(this.passportKeys)) {
            return Arrays.stream(this.passportKeys.split(",")).collect(Collectors.toSet());
        }

        return null;
    }

    /**
     * 获取手机号关键字列表
     *
     * @return 手机号关键字列表
     */
    public Set<String> getPhoneNumberKeys() {
        // 获取手机号关键字列表
        if (StringUtils.isNotBlank(this.phoneNumberKeys)) {
            return Arrays.stream(this.phoneNumberKeys.split(",")).collect(Collectors.toSet());
        }

        return null;
    }
}
