package com.libraryapp.domain.enums;

import lombok.Getter;

/**
 * USER    - browse catalogue, borrow/waitlist books, view own loans & wishlist, make payments
 * MANAGER - add/remove/update catalogue, check books in/out, authorise payments
 * ADMIN   - manage user roles and user groups
 */
@Getter
public enum ERole {
    USER("user"),
    MANAGER("manager"),
    ADMIN("admin");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

    public static ERole fromValue(String code) {
        for (ERole cd : ERole.values()) {
            if (cd.getValue().equals(code)) {
                return cd;
            }
        }
        return null;
    }

}