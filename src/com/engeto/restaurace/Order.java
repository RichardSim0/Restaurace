package com.engeto.restaurace;

import java.time.LocalDateTime;


public class Order implements Comparable<Order>{
    private int table;
    private Dish dish;
    private Menu menu;
    private int waiter;
    private LocalDateTime orderedTime;
    private LocalDateTime fulfilmentTime;
    private boolean isPaid;
    private String note;

    public Order(int table,Menu menu, Dish dish, int waiter, boolean isPaid, String note) throws RestaurantException {
        try{
        this.table = table;
        this.menu = menu;
        this.dish = dish;
        this.waiter = waiter;
        this.orderedTime = LocalDateTime.now();
        this.fulfilmentTime = orderedTime.plusMinutes(dish.getPreparationTime());
        this.isPaid = isPaid;
        this.note = note;

        if (!menu.isDishOnMenu(dish)) {
            throw new RestaurantException("Toto jedlo nie je v menu a nemôže byť objednané.");
        }

        System.out.println("Objednávka pre " + dish.getTitle() + " bola prijatá.");

    }catch (IllegalArgumentException e) {
        throw new RestaurantException("Toto jedlo nie je v menu a nemôže byť objednané."+e.getLocalizedMessage());
    }
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getWaiter() {
        return waiter;
    }

    public void setWaiter(int waiter) {
        this.waiter = waiter;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public LocalDateTime getFulfilmentTime() {
        return fulfilmentTime;
    }

    public void setFulfilmentTime(LocalDateTime fulfilmentTime) {
        this.fulfilmentTime = fulfilmentTime;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Order{" +
                "table=" + table +
                ", dish=" + dish +
                ", waiter=" + waiter +
                ", orderedTime=" + orderedTime +
                ", fulfilmentTime=" + fulfilmentTime +
                ", isPaid=" + isPaid +
                ", note='" + note + '\'' +
                '}';
    }

    @Override
    public int compareTo(Order o) {
        return this.orderedTime.compareTo(o.orderedTime);
    }
}