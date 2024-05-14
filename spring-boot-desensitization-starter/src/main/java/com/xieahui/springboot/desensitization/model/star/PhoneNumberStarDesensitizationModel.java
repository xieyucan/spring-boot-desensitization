package com.xieahui.springboot.desensitization.model.star;

import com.xieahui.springboot.desensitization.model.DesensitizationModel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * @description: 手机号脱敏模型
 * @author: hui.xie
 * @email: xiehui1956@gmail.com
 * @date: 2024/5/11 13:44
 */
@Order(40)
@Component
public class PhoneNumberStarDesensitizationModel implements DesensitizationModel {

    /**
     * 手机号脱敏
     * 手机号通常为11位数字，以1开头
     * 保留：前三后四
     */
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\b(1\\d{2})\\d{4}(\\d{4})\\b");

    @Override
    public String desensitize(String input) {
        return desensitize(input, PHONE_NUMBER_PATTERN);
    }

    /**
     * 手机号脱敏
     * 手机号通常为11位数字，以1开头
     * 保留：前三后四
     */
    @Override
    public String getReplacement() {
        return "$1****$2";
    }

}
