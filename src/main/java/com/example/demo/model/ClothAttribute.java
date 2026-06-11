package com.example.demo.model;

public enum ClothAttribute {
    TOPS("トップス"),
    BOTTOMS("ボトムス"),
    SPRING("春物"),
    WINTER("冬物"),
    FAVORITE("お気に入り");

    private final String displayName;

    ClothAttribute(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}