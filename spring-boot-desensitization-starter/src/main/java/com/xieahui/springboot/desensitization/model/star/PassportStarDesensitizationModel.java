package com.xieahui.springboot.desensitization.model.star;

import com.xieahui.springboot.desensitization.model.DesensitizationModel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * @description: 护照脱敏模型
 * @author: hui.xie
 * @email: xiehui1956@gmail.com
 * @date: 2024/5/11 13:44
 */
@Order(30)
@Component
public class PassportStarDesensitizationModel implements DesensitizationModel {

    /**
     * 护照脱敏
     * 保留：前二后三
     */
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("(E[ABCDEFGHJKLMNPQRSTUVWXYZ]\\d{1})\\d{2}(\\d{4})");

    @Override
    public String desensitize(String input) {
        return desensitize(input, PASSPORT_PATTERN);
    }

    /**
     * 护照脱敏
     * 保留：前二后三
     */
    @Override
    public String getReplacement() {
        return "$1**$2";
    }
}
