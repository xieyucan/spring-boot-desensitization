package com.xieahui.springboot.desensitization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 脱敏模型工厂
 * @author: hui.xie
 * @email: xiehui1956@gmail.com
 * @date: 2024/5/11 13:32
 */
@Slf4j
@Data
@Component
@AllArgsConstructor
public class DesensitizationModelFactory {

    /**
     * 脱敏模型
     */
    private Map<String, DesensitizationModel> desensitizationModels;

    /**
     * 获取脱敏模型
     *
     * @param serviceName 服务名称
     * @return 脱敏模型
     */
    public DesensitizationModel getDesensitizationModel(String serviceName) {
        DesensitizationModel tokenService = this.desensitizationModels.get(serviceName);
        if (null == tokenService) {
            throw new IllegalStateException("Invalid service name: " + serviceName);
        }
        return tokenService;
    }

}
