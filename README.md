# Logback⽇志数据脱敏⼯具：隐私和安全的守护者

### 概述

在涉及敏感数据的⽇志记录环境中，数据保护和个⼈隐私⽆疑是⾄关重要的领域。确保敏感数据不被泄露，脱敏处理成为必不可少的⼀步。数据脱敏是⼀种技术⼿段，其将敏感信息转换为不可识别或不可逆转的形式，以便在保护⽤户隐私和数据安全的同时，不影响其原有的⽤途。
Logback是⼀个深受欢迎的Java⽇志框架，⼴泛应⽤于各类应⽤程序的⽇志记录中。它的强⼤之处在于提供了丰富的配置选项和灵活性，让开发⼈员能够完全掌控⽇志的输出格式和输出⽬标。
总结来说，这⼀切旨在提升数据隐私和安全性的标准，这不仅能够满⾜数据保护法规和隐私标准的要求，更能赢得⽤户对你的应⽤程序的信任。
关于Logback的基础知识，你可以参考[深入探索 SLF4J、Log4J 和 Logback](http://www.xieahui.com/2023/06/11/java/%E6%B7%B1%E5%85%A5%E6%8E%A2%E7%B4%A2%20SLF4J%E3%80%81Log4J%20%E5%92%8C%20Logback/)。

### 为什么选择我们的组件

### 参考demo
[spring-boot-desensitization-demo](spring-boot-desensitization-demo)

### 快速⼊⻔

#### 1. 引⼊依赖
```xml
<dependency>
    <groupId>com.xieahui.springboot</groupId>
    <artifactId>spring-boot-desensitization-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

#### 2. Logback配置⽂件
```xml
<include resource="logback-desensitization-included.xml"/>
```

#### 3. 说明
- 配置中⼼配置过滤关键字
- 过滤相关⽇志的关键字，使⽤英⽂逗号进⾏分隔。在设定后，过滤器仅对含有指定关键字的⽇志进⾏过滤。如果未设定，将不执⾏相关⽇志过滤。为了提⾼效率，建议设定指定关键字(⼤⼩写不敏感)。

### 详细介绍

#### 1. 默认过滤模型
- 银⾏卡模型 BankCardStarDesensitizationModel.java
```java
/**
* 银⾏卡脱敏
* 16-19位数字
* 保留：前三后四
*/
private static final Pattern BANK_CARD_PATTERN=Pattern.compile("\\b([1,4,5,6,9]{1}\\d{2})\\d{9,14}(\\d{4})\\b");
```

- 身份证号模型 IdNumberStarDesensitizationModel.java
```java
/**
* 身份证号脱敏
* 身份证号码为18位，前17位为数字，最后⼀位可能是数字或'X'
* 保留：前三后四
*/
private static final Pattern ID_NUMBER_PATTERN=Pattern.compile("\\b([1-9]{1}[0-9]{2})[0-9]{3}(?:19|20){1}[0-9]{2}(?:(?:0[1-9])|(?:1[1-2])){1}(?:(?:[0-2][1-9])|10|20|30|31){1}([0-9,xX]{4})\\b");
```

- 护照模型 PassportStarDesensitizationModel.java
```java
/**
* 护照脱敏
* 保留：前⼆后三
*/
private static final Pattern PASSPORT_PATTERN=Pattern.compile("(E[ABCDEFGHJKLMNPQRSTUVWXYZ]\\d{1})\\d{2}(\\d{4})");
```

- ⼿机号模型 PhoneNumberStarDesensitizationModel.java
```java
/**
* ⼿机号脱敏
* ⼿机号通常为11位数字，以1开头
* 保留：前三后四
*/
private static final Pattern PHONE_NUMBER_PATTERN=Pattern.compile("\\b(1\\d{2})\\d{4}(\\d{4})\\b");
```

#### 2. ⽀持配置中⼼⾃定义模型
| 参数名称  | 数据类型  | 默认设置  | 功能描述   |
| -------- | :------ | :------- | :------- |
| desensitization.open | Boolean | true | 控制日志过滤器的开关。默认状态为开启。如需关闭，需将此设置为否（false） |
| desensitization.logSize | Integer | 1024 | 定义过滤日志的最大长度。超过此阈值的日志不会被过滤。请注意，如果此值设置过大，可能会影响系统性能。默认值为1024。 |
| desensitization.bank.keys | String | 无 | 定义-银行卡号-过滤相关日志的关键字。 |
| desensitization.idNumber.keys | String | 无 | 定义-身份证号-过滤相关日志的关键字。 |
| desensitization.passport.keys | String | 无 | 定义-护照号-过滤相关日志的关键字。 |
| desensitization.phoneNumber.keys | String | 无 | 定义-手机号-过滤相关日志的关键字。 |

- 配置中⼼ - ⾃定义过滤⻓度 此设置允许您设定⽇志内容的⻓度限制。只有在⽇志内容⻓度⼩于或等于此限制的⽇志才会被过滤。这个功能可以提升系统的过滤性能，因此强烈建议进⾏设置。
```properties
logSize=1024
```

- 配置中⼼ - ⾃定义过滤关键字 ⼀旦设定关键字，只有那些包含指定关键字的⽇志才会被过滤。如果未设置关键字，系统将默认不执⾏⽇志过滤。为了提⾼系统的运⾏效率，我们建议设置相关的关键字。
  附注：在配置关键字时，⽆需使⽤正则表达式，因为系统使⽤的是containsIgnoreCase⽅法来判断⽇志内容中是否存在关键字。
```properties
desensitization.bank.keys=你的关键字
desensitization.idNumber.keys=你的关键字
desensitization.passport.keys=你的关键字
desensitization.phoneNumber.keys=你的关键字
desensitization.keysModelsMappings=你的关键字和模型对应关系
```

#### 3. ⽀持业务代码⾃定义模型
- 业务编码⾃定义模型 在业务编码中，只需实现以下所示的接⼝，并使⽤相应的注解（例如@Service）将实现类注⼊到Spring容器中即可。
```java
com.xieahui.springboot.desensitization.model.DesensitizationModel
```

- 实现实例，可参考银⾏卡号过滤代码 BankCardStarDesensitizationModel.java
```java
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
```
- 说明：为什么我们建议通过编码⽅式添加匹配和替换模型呢？这是因为正则表达式匹配通常耗时较⻓。预设匹配模型的主要⽬的是优化程序性能，同时为业务⾃定义扩展逻辑提供⼀个⼊⼝点。

### 性能损耗测试
- 总结：性能损耗非常小
- 未使用脱敏组件   
![未脱敏.png](spring-boot-desensitization-starter%2Fsrc%2Fmain%2Fresources%2Fimg%2F%E6%9C%AA%E8%84%B1%E6%95%8F.png)

- 使用脱敏组件全匹配   
 ![已脱敏.png](spring-boot-desensitization-starter%2Fsrc%2Fmain%2Fresources%2Fimg%2F%E5%B7%B2%E8%84%B1%E6%95%8F.png)


### 功能测试报告
![测试报告.png](spring-boot-desensitization-starter%2Fsrc%2Fmain%2Fresources%2Fimg%2F%E6%B5%8B%E8%AF%95%E6%8A%A5%E5%91%8A.png)

### 友情提示
请注意，我们的测试场景仅覆盖了⼀些常⻅的简单情况。在使⽤该组件时，你需要根据⾃⼰的具体业务场景进⾏效果测试。如果在使⽤过程中遇到任何问题，请及时联系。谢谢。
