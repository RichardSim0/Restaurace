package com.engeto.restaurace;

public enum DishCategory {
    SOUP("polievka"), APPETIZER("predjedlo"),
    MAIN_COURSE("hlavné jedlo"), DESSERT("dezert"),
    DRINK("nápoj");

    private String description;

    DishCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}