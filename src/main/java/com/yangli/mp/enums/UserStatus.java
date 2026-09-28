package com.yangli.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    NORMAL(1, "正常"),
    FROZEN(2, "冻结");

    @EnumValue          // MP：数据库存这个字段的值
    private final int value;

    private final String desc;

    // Jackson：序列化输出 1/2（而不是 "NORMAL"），保证接口格式不变
    @JsonValue
    public int getValue() {
        return value;
    }

    // Jackson：反序列化 1 -> NORMAL
    @JsonCreator
    public static UserStatus of(int value) {
        for (UserStatus s : values()) {
            if (s.value == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("未知状态: " + value);
    }
}
