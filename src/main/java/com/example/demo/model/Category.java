package com.example.demo.model;

public enum Category {
    HEAD("HEAD (髪飾り)"),
    TOPS("TOPS (シャツ)"),
    BOTTOMS("BOTTOMS (ズボン・スカート)"),
    LEG("LEG (靴下)"),
    ACCESSORY("ACCESSORY (アクセサリ)");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}