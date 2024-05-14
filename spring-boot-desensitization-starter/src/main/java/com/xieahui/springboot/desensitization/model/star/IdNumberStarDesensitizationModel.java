package com.xieahui.springboot.desensitization.model.star;

import com.xieahui.springboot.desensitization.model.DesensitizationModel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 身份证
 * 18、17+X
 *
 * @description: 身份证脱敏模型
 * @author: hui.xie
 * @email: xiehui1956@gmail.com
 * @date: 2024/5/11 13:41
 */
@Order(20)
@Component
public class IdNumberStarDesensitizationModel implements DesensitizationModel {

    /**
     * 身份证号脱敏
     * 身份证号码为18位，前17位为数字，最后一位可能是数字或'X'
     * 保留：前三后四
     */
    private static final Pattern ID_NUMBER_PATTERN = Pattern.compile("\\b([1-9]{1}[0-9]{2})[0-9]{3}(?:19|20){1}[0-9]{2}(?:(?:0[1-9])|(?:1[1-2])){1}(?:(?:[0-2][1-9])|10|20|30|31){1}([0-9,xX]{4})\\b");

    @Override
    public String desensitize(String input) {
        return desensitize(input, ID_NUMBER_PATTERN);
    }

    /**
     * 身份证号脱敏
     * 身份证号码为18位，前17位为数字，最后一位可能是数字或'X'
     * 保留：前三后四
     */
    @Override
    public String getReplacement() {
        return "$1***********$2";
    }
}
