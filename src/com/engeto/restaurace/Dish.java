package com.engeto.restaurace;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Dish {
    private String title;
    private BigDecimal price;
    private int preparationTime;
    private List<String> images;
    private DishCategory category;

    public Dish(String title, BigDecimal price, int preparationTime, String image, DishCategory category) {
        this.title = title;
        this.price = price;
        this.preparationTime = preparationTime;
        this.images = new ArrayList<>();
        if (image != null && !image.isEmpty()) {
            this.images.add(image);
        }        this.category = category;
    }

    public Dish(String title, BigDecimal price, int preparationTime, DishCategory category) {
        this.title = title;
        this.price = price;
        this.preparationTime = preparationTime;
        this.images = new ArrayList<>();
        this.images.add("blank");
        this.category = category;
    }

    public static Dish getDishByTitle(String title) {
        for (Dish dish : Menu.menuList) {
            if (dish.getTitle().equals(title)) {
                return dish;
            }
        }
        return null;
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

    public List<String> getImages() {
        return images;
    }

    public void addImage(String image) {
            this.images.add(image);
    }

    public void removeImage(String image) {
        if (this.images.size() > 1 && this.images.contains(image)) {
            this.images.remove(image);
        }
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
                title +
                ", price=" + price +
                ", preparationTime=" + preparationTime +
                ", images='" + images + '\'' +
                ", category=" + category.getDescription() +
                '}';
    }

}