package com.muac.snmp.mib;

public enum ActualStatusEnum {

    GRAY(0), GREEN(1), YELLOW(2), RED(3);

    public final int value;

    ActualStatusEnum(final int value) {
        this.value = value;
    }
}
