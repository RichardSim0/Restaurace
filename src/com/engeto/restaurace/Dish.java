package com.engeto.restaurace;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Dish {
    private String title;
    private BigDecimal price;
    private int preparationTime;
    private String image;
    private DishCategory category;

    public Dish(String title, BigDecimal price, int preparationTime, String image, DishCategory category) {
        this.title = title;
        this.price = price;
        this.preparationTime = preparationTime;
        this.image = image;
        this.category = category;
    }

    public Dish(String title, BigDecimal price, int preparationTime, DishCategory category) {
        this.title = title;
        this.price = price;
        this.preparationTime = preparationTime;
        this.image = "blank";
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public DishCategory getCategory() {
        return category;
    }

    public void setCategory(DishCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", preparationTime=" + preparationTime +
                ", image='" + image + '\'' +
                ", category=" + category.getDescription() +
                '}';
    }

}