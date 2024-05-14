package com.xieahui.springboot.desensitization.model.star;

import com.xieahui.springboot.desensitization.model.DesensitizationModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * @description: 银行卡脱敏模型
 * @author: hui.xie
 * @email: xiehui1956@gmail.com
 * @date: 2024/5/11 13:35
 */
@Slf4j
@Order(10)
@Component
public class BankCardStarDesensitizationModel implements DesensitizationModel {

    public BankCardStarDesensitizationModel(){
        log.info("BankCardStarDesensitizationModel init");
    }

    /**
     * 银行卡脱敏
     * 16-19位数字
     * 保留：前三后四
     */
    private static final Pattern BANK_CARD_PATTERN = Pattern.compile("\\b([1,4,5,6,9]{1}\\d{2})\\d{9,14}(\\d{4})\\b");

    @Override
    public String desensitize(String input) {
        return desensitize(input, BANK_CARD_PATTERN);
    }

    /**
     * 银行卡脱敏
     * 16-19位数字
     * 保留：前三后四
     */
    @Override
    public String getReplacement() {
        return "$1*********$2";
    }

}
