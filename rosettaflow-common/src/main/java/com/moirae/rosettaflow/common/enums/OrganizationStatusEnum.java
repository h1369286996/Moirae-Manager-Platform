package com.moirae.rosettaflow.common.enums;

/**
 * 机构状态枚举
 *
 * @author admin
 * @date 2021/8/16
 */
public enum OrganizationStatusEnum {

    /**
     * 未知
     */
    UNKONW(0),
    /**
     * CSV类型
     */
    NORMAL(1),
    /**
     * CSV类型
     */
    UN_NORMAL(2);

    private final int value;

    OrganizationStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
