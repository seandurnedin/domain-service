package com.libraryapp.domain.enums;

import lombok.Getter;

@Getter
public enum EPaymentStatus {
    PENDING("pending"),
    PAID("paid"),
    FAILED("failed"),
    REFUNDED("refunded");

    private final String value;

    EPaymentStatus(String value) {
        this.value = value;
    }

    public static EPaymentStatus fromValue(String code) {
        for (EPaymentStatus cd : EPaymentStatus.values()) {
            if (cd.getValue().equals(code)) {
                return cd;
            }
        }
        return null;
    }

}