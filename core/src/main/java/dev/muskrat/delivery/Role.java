package dev.muskrat.delivery;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

    USER("user"),
    PARTNER("partner"),
    ADMIN("admin");

    private final String role;

    @Override
    public String toString() {
        return role;
    }
}
