package com.xieahui.springboot.desensitization.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 预制脱敏匹配模型
 * 公司统一的日志告警匹配规则
 * 银行卡、手机号、身份证
 *
 * @description: 预制脱敏匹配模型
 * @author: hui.xie
 * @email: xiehui1956@gmail.com
 * @date: 2024/5/11 13:30
 */
public interface DesensitizationModel {

    /**
     * 脱敏入口
     *
     * @param input 待脱敏日志
     * @return 脱敏后的日志
     */
    String desensitize(String input);

    /**
     * 执行脱敏
     *
     * @param input   待脱敏日志
     * @param pattern 匹配正则
     * @return 脱敏后的日志
     */
    default String desensitize(String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        if (1 < matcher.groupCount())
            return pattern.matcher(input).replaceAll(getReplacement());
        return input;
    }

    /**
     * 替换内容
     *
     * @return 替换内容
     */
    String getReplacement();

    /**
     * 银行卡号过滤模型
     */
    String BANK = "bankCardStarDesensitizationModel";

    /**
     * 身份证号过滤模型
     */
    String ID_NUMBER = "idNumberStarDesensitizationModel";

    /**
     * 护照号过滤模型
     */
    String PASSPORT = "passportStarDesensitizationModel";

    /**
     * 手机号过滤模型
     */
    String PHONE_NUMBER = "phoneNumberStarDesensitizationModel";

}
